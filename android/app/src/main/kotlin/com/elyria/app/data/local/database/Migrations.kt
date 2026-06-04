package com.elyria.app.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE mood_entries ADD COLUMN emotions_json TEXT NOT NULL DEFAULT '[]'",
        )
        db.execSQL(
            "ALTER TABLE mood_entries ADD COLUMN triggers_json TEXT NOT NULL DEFAULT '[]'",
        )
    }
}
