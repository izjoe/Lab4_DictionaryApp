package com.example.dictonaryapp.data

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.dictonaryapp.model.Word
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class DictionaryDbHelper(private val ctx: Context) : SQLiteOpenHelper(ctx, DbContract.DB_NAME, null, DbContract.DB_VERSION) {

    private val prefs: SharedPreferences = ctx.getSharedPreferences("dic_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TAG = "DictionaryDbHelper"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val create = """
            CREATE TABLE ${DbContract.Words.TABLE_NAME} (
                ${DbContract.Words._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DbContract.Words.COLUMN_TERM} TEXT NOT NULL UNIQUE,
                ${DbContract.Words.COLUMN_DEFINITION} TEXT NOT NULL,
                ${DbContract.Words.COLUMN_PART_OF_SPEECH} TEXT,
                ${DbContract.Words.COLUMN_EXAMPLE} TEXT,
                ${DbContract.Words.COLUMN_CREATED_AT} INTEGER NOT NULL
            );
        """.trimIndent()
        db.execSQL(create)
        db.execSQL("CREATE INDEX idx_words_term ON ${DbContract.Words.TABLE_NAME}(${DbContract.Words.COLUMN_TERM} COLLATE NOCASE);")

        // Note: we don't seed here directly; seed is triggered once on app start to allow using assets.
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Simple strategy for lab: drop & recreate
        db.execSQL("DROP TABLE IF EXISTS ${DbContract.Words.TABLE_NAME}")
        onCreate(db)
    }

    // Insert a single word
    fun insertWord(word: Word): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DbContract.Words.COLUMN_TERM, word.term.trim())
            put(DbContract.Words.COLUMN_DEFINITION, word.definition.trim())
            put(DbContract.Words.COLUMN_PART_OF_SPEECH, word.partOfSpeech)
            put(DbContract.Words.COLUMN_EXAMPLE, word.example)
            put(DbContract.Words.COLUMN_CREATED_AT, word.createdAt)
        }
        return db.insertWithOnConflict(DbContract.Words.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE)
    }

    // Update existing
    fun updateWord(id: Long, word: Word): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DbContract.Words.COLUMN_TERM, word.term.trim())
            put(DbContract.Words.COLUMN_DEFINITION, word.definition.trim())
            put(DbContract.Words.COLUMN_PART_OF_SPEECH, word.partOfSpeech)
            put(DbContract.Words.COLUMN_EXAMPLE, word.example)
        }
        return db.update(DbContract.Words.TABLE_NAME, values, "${DbContract.Words._ID} = ?", arrayOf(id.toString()))
    }

    // Delete
    fun deleteWord(id: Long): Int {
        val db = writableDatabase
        return db.delete(DbContract.Words.TABLE_NAME, "${DbContract.Words._ID} = ?", arrayOf(id.toString()))
    }

    // Query all - returns a Cursor (lab requirement to use Cursor)
    fun getAllWordsCursor(): Cursor {
        val db = readableDatabase
        return db.query(
            DbContract.Words.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${DbContract.Words.COLUMN_TERM} COLLATE NOCASE ASC"
        )
    }

    // Search by keyword in term or definition
    fun searchWordsCursor(keyword: String): Cursor {
        val db = readableDatabase
        val like = "%${keyword.trim()}%"
        return db.rawQuery(
            "SELECT * FROM ${DbContract.Words.TABLE_NAME} WHERE ${DbContract.Words.COLUMN_TERM} LIKE ? OR ${DbContract.Words.COLUMN_DEFINITION} LIKE ? ORDER BY ${DbContract.Words.COLUMN_TERM} COLLATE NOCASE ASC",
            arrayOf(like, like)
        )
    }

    // Get single by id
    fun getWordByIdCursor(id: Long): Cursor {
        val db = readableDatabase
        return db.query(
            DbContract.Words.TABLE_NAME,
            null,
            "${DbContract.Words._ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
    }

    // Seed database from assets/seed.json on first run
    fun seedDatabaseIfNeeded() {
        val seeded = prefs.getBoolean("PREF_DB_SEEDED", false)
        if (seeded) return

        try {
            ctx.assets.open("seed.json").use { ins ->
                val reader = BufferedReader(InputStreamReader(ins))
                val sb = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                val arr = JSONArray(sb.toString())

                val db = writableDatabase
                db.beginTransaction()
                try {
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        val term = obj.optString("term")
                        val def = obj.optString("definition")
                        val pos = if (obj.has("partOfSpeech")) obj.optString("partOfSpeech") else null
                        val example = if (obj.has("example")) obj.optString("example") else null
                        if (term.isNotBlank() && def.isNotBlank()) {
                            val values = ContentValues().apply {
                                put(DbContract.Words.COLUMN_TERM, term.trim())
                                put(DbContract.Words.COLUMN_DEFINITION, def.trim())
                                put(DbContract.Words.COLUMN_PART_OF_SPEECH, pos)
                                put(DbContract.Words.COLUMN_EXAMPLE, example)
                                put(DbContract.Words.COLUMN_CREATED_AT, System.currentTimeMillis())
                            }
                            db.insertWithOnConflict(DbContract.Words.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE)
                        }
                    }
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }

                prefs.edit().putBoolean("PREF_DB_SEEDED", true).apply()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error seeding database", e)
        }
    }

    // Utility to map a Cursor row into Word model (caller must ensure cursor positioned)
    fun cursorToWord(cursor: Cursor): Word {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Words._ID))
        val term = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Words.COLUMN_TERM))
        val definition = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Words.COLUMN_DEFINITION))
        val pos = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Words.COLUMN_PART_OF_SPEECH))
        val example = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Words.COLUMN_EXAMPLE))
        val createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Words.COLUMN_CREATED_AT))
        return Word(id = id, term = term, definition = definition, partOfSpeech = pos, example = example, createdAt = createdAt)
    }
}

