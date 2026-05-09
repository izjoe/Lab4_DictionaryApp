package com.example.dictonaryapp

import android.app.AlertDialog
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dictonaryapp.data.DictionaryDbHelper

class DetailActivity : AppCompatActivity() {

    private lateinit var tvTerm: TextView
    private lateinit var tvDef: TextView
    private lateinit var tvPart: TextView
    private lateinit var tvExample: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button

    private var itemId: Long = -1L
    private lateinit var dbHelper: DictionaryDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvTerm = findViewById(R.id.tvTermDetail)
        tvDef = findViewById(R.id.tvDefinitionDetail)
        tvPart = findViewById(R.id.tvPart)
        tvExample = findViewById(R.id.tvExample)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)

        dbHelper = DictionaryDbHelper(this)

        itemId = intent.getLongExtra("id", -1L)
        if (itemId == -1L) {
            finish()
            return
        }

        loadDetails()

        btnEdit.setOnClickListener {
            val i = Intent(this, AddEditActivity::class.java)
            i.putExtra("edit_id", itemId)
            startActivityForResult(i, 1001)
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Delete this word?")
                .setPositiveButton("Delete") { _, _ ->
                    val rows = dbHelper.deleteWord(itemId)
                    if (rows > 0) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun loadDetails() {
        val c = dbHelper.getWordByIdCursor(itemId)
        c.use {
            if (it.moveToFirst()) {
                tvTerm.text = it.getString(it.getColumnIndexOrThrow(com.example.dictonaryapp.data.DbContract.Words.COLUMN_TERM))
                tvDef.text = it.getString(it.getColumnIndexOrThrow(com.example.dictonaryapp.data.DbContract.Words.COLUMN_DEFINITION))
                tvPart.text = it.getString(it.getColumnIndexOrThrow(com.example.dictonaryapp.data.DbContract.Words.COLUMN_PART_OF_SPEECH)) ?: ""
                tvExample.text = it.getString(it.getColumnIndexOrThrow(com.example.dictonaryapp.data.DbContract.Words.COLUMN_EXAMPLE)) ?: ""
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            loadDetails()
            setResult(Activity.RESULT_OK)
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
