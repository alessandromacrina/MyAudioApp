package it.uninsubria.myaudio

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DB_NAME = "sqlite_data.db"
val DB_VERSION = 1
val TABLE_NAME = "AudioRecords"
val filename ="FileName"
val filePath ="Path"
var timestamp = "TimeStamp"
var duration ="Duration"
var amspath = "Amspath"

class DBHelper (var context : Context) : SQLiteOpenHelper(context , DB_NAME , null , DB_VERSION) {



    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE" + TABLE_NAME + " ("+
                        filename + "VARCHAR(128)," +
                filePath + "VARCHAR(512)" +
                timestamp + "BIGINT" +
                duration + "VARCHAR(15)" +
                amspath + "VARCHAR(512)" +
                ")"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}