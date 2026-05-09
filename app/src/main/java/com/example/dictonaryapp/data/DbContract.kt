package com.example.dictonaryapp.data

import android.provider.BaseColumns

object DbContract {
    const val DB_NAME = "dictionary.db"
    const val DB_VERSION = 1

    object Words : BaseColumns {
        const val _ID = BaseColumns._ID
        const val TABLE_NAME = "words"
        const val COLUMN_TERM = "term"
        const val COLUMN_DEFINITION = "definition"
        const val COLUMN_PART_OF_SPEECH = "part_of_speech"
        const val COLUMN_EXAMPLE = "example"
        const val COLUMN_CREATED_AT = "created_at"
    }
}
