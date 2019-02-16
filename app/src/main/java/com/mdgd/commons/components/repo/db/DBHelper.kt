package com.mdgd.commons.components.repo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Max
 * on 30-Apr-17.
 */

class DBHelper internal constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(EARTHQUAKES_TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUAKES")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "quakes.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_QUAKES = "earthquakes"
        const val COLUMN_ID = "_id"
        const val COLUMN_QUAKE_ID = "quake_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_URL = "url"
        const val COLUMN_MAGNITUDE = "mag"
        const val COLUMN_TIME = "time"
        const val COLUMN_LNG = "lng"
        const val COLUMN_LAT = "lat"


        // Database creation sql statement
        private const val EARTHQUAKES_TABLE_CREATE = "create table " +
                TABLE_QUAKES + "( " +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_QUAKE_ID + " text not null, " +
                COLUMN_TITLE + " text, " +
                COLUMN_URL + " text, " +
                COLUMN_MAGNITUDE + " text, " +
                COLUMN_TIME + " integer, " +
                COLUMN_LNG + " real, " +
                COLUMN_LAT + " real " +
                ");"
    }
}

