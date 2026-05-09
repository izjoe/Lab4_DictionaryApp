package com.example.dictonaryapp.ui.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dictonaryapp.R
import com.example.dictonaryapp.data.DbContract
import com.example.dictonaryapp.model.Word

// RecyclerView adapter that reads from a Cursor. Provides item click callbacks.
class WordAdapter(private val context: Context) : RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    private var cursor: Cursor? = null
    var onItemClick: ((id: Long) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val termText: TextView = view.findViewById(R.id.tvTerm)
        val defText: TextView = view.findViewById(R.id.tvDefinition)

        init {
            view.setOnClickListener {
                cursor?.let {
                    if (it.moveToPosition(bindingAdapterPosition)) {
                        val id = it.getLong(it.getColumnIndexOrThrow(DbContract.Words._ID))
                        onItemClick?.invoke(id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_word, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = cursor?.count ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val c = cursor ?: return
        if (!c.moveToPosition(position)) return
        val term = c.getString(c.getColumnIndexOrThrow(DbContract.Words.COLUMN_TERM))
        val definition = c.getString(c.getColumnIndexOrThrow(DbContract.Words.COLUMN_DEFINITION))
        holder.termText.text = term
        holder.defText.text = if (definition.length > 120) definition.substring(0, 120) + "..." else definition
    }

    fun swapCursor(newCursor: Cursor?) {
        if (cursor === newCursor) return
        cursor?.close()
        cursor = newCursor
        notifyDataSetChanged()
    }

    // Safe mapping from cursor row to Word
    fun getWordAt(position: Int): Word? {
        val c = cursor ?: return null
        if (!c.moveToPosition(position)) return null
        val id = c.getLong(c.getColumnIndexOrThrow(DbContract.Words._ID))
        val term = c.getString(c.getColumnIndexOrThrow(DbContract.Words.COLUMN_TERM))
        val definition = c.getString(c.getColumnIndexOrThrow(DbContract.Words.COLUMN_DEFINITION))
        val pos = c.getString(c.getColumnIndexOrThrow(DbContract.Words.COLUMN_PART_OF_SPEECH))
        val example = c.getString(c.getColumnIndexOrThrow(DbContract.Words.COLUMN_EXAMPLE))
        val createdAt = c.getLong(c.getColumnIndexOrThrow(DbContract.Words.COLUMN_CREATED_AT))
        return Word(id = id, term = term, definition = definition, partOfSpeech = pos, example = example, createdAt = createdAt)
    }
}

