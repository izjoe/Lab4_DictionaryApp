package com.example.dictonaryapp.model

// Simple data model for a dictionary word
data class Word(
    val id: Long = 0,
    val term: String,
    val definition: String,
    val partOfSpeech: String? = null,
    val example: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
