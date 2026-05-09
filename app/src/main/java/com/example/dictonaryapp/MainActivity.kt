package com.example.dictonaryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dictonaryapp.data.DictionaryDbHelper
import com.example.dictonaryapp.ui.adapter.WordAdapter
import com.example.dictonaryapp.ui.theme.DictonaryAppTheme
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DictionaryDbHelper
    private lateinit var adapter: WordAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var searchView: SearchView
    private lateinit var swipe: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DictionaryDbHelper(this)

        // Seed DB if needed
        dbHelper.seedDatabaseIfNeeded()

        recyclerView = findViewById(R.id.recyclerView)
        tvEmpty = findViewById(R.id.tvEmpty)
        searchView = findViewById(R.id.searchView)
        swipe = findViewById(R.id.swipe)

        adapter = WordAdapter(this)
        adapter.onItemClick = { id ->
            val i = Intent(this, DetailActivity::class.java)
            i.putExtra("id", id)
            startActivityForResult(i, 1000)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadAll()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val i = Intent(this, AddEditActivity::class.java)
            startActivityForResult(i, 1001)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val q = newText ?: ""
                if (q.isBlank()) loadAll() else search(q)
                return true
            }
        })

        swipe.setOnRefreshListener {
            loadAll()
            swipe.isRefreshing = false
        }
    }

    private fun loadAll() {
        val c = dbHelper.getAllWordsCursor()
        adapter.swapCursor(c)
        tvEmpty.visibility = if (c.count == 0) View.VISIBLE else View.GONE
    }

    private fun search(q: String) {
        val c = dbHelper.searchWordsCursor(q)
        adapter.swapCursor(c)
        tvEmpty.visibility = if (c.count == 0) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1000 || requestCode == 1001) && resultCode == Activity.RESULT_OK) {
            loadAll()
        }
    }
}