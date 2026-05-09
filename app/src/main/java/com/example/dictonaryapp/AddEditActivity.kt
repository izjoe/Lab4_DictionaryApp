package com.example.dictonaryapp

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dictonaryapp.data.DictionaryDbHelper
import com.example.dictonaryapp.model.Word

class AddEditActivity : AppCompatActivity() {

    private lateinit var etTerm: EditText
    private lateinit var etDefinition: EditText
    private lateinit var etPart: EditText
    private lateinit var etExample: EditText
    private lateinit var btnSave: Button

    private var editingId: Long = -1L
    private lateinit var dbHelper: DictionaryDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etTerm = findViewById(R.id.etTerm)
        etDefinition = findViewById(R.id.etDefinition)
        etPart = findViewById(R.id.etPart)
        etExample = findViewById(R.id.etExample)
        btnSave = findViewById(R.id.btnSave)

        dbHelper = DictionaryDbHelper(this)

        editingId = intent.getLongExtra("edit_id", -1L)
        if (editingId != -1L) {
            title = "Edit Word"
            loadExisting(editingId)
        } else {
            title = "Add Word"
        }

        btnSave.setOnClickListener {
            save()
        }
    }

    private fun loadExisting(id: Long) {
        val c = dbHelper.getWordByIdCursor(id)
        c.use {
            if (it.moveToFirst()) {
                etTerm.setText(it.getString(it.getColumnIndexOrThrow(com.example.dictonaryapp.data.DbContract.Words.COLUMN_TERM)))
                etDefinition.setText(it.getString(it.getColumnIndexOrThrow(com.example.dictonaryapp.data.DbContract.Words.COLUMN_DEFINITION)))
                etPart.setText(it.getString(it.getColumnIndexOrThrow(com.example.dictonaryapp.data.DbContract.Words.COLUMN_PART_OF_SPEECH)))
                etExample.setText(it.getString(it.getColumnIndexOrThrow(com.example.dictonaryapp.data.DbContract.Words.COLUMN_EXAMPLE)))
            }
        }
    }

    private fun save() {
        val term = etTerm.text.toString().trim()
        val definition = etDefinition.text.toString().trim()
        val part = etPart.text.toString().trim().ifEmpty { null }
        val example = etExample.text.toString().trim().ifEmpty { null }

        if (TextUtils.isEmpty(term)) {
            etTerm.error = "Required"
            return
        }
        if (TextUtils.isEmpty(definition)) {
            etDefinition.error = "Required"
            return
        }

        val word = Word(term = term, definition = definition, partOfSpeech = part, example = example)

        if (editingId != -1L) {
            val rows = dbHelper.updateWord(editingId, word)
            if (rows > 0) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            val id = dbHelper.insertWord(word)
            if (id > 0) {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Save failed (duplicate term?)", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

