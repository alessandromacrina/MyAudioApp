package it.uninsubria.myaudio

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db)
    }

    fun insertData(fn:String , fp:String , ts:Long,
                    dur:String , aP:String): Boolean{
        val db=this.readableDatabase
        var cv = ContentValues()
        cv.put(filename , fn)
        cv.put(filePath , fp)
        cv.put(timestamp , ts)
        cv.put(duration, dur)
        cv.put(amspath , aP)
        var result = db.insert(TABLE_NAME , null , cv)
        if(result<0)
            return false
        return true

    }

    fun readData(): ArrayList<AudioRecord>{
        var list = ArrayList<AudioRecord>()
        val db=this.readableDatabase
        val query ="Select * from $TABLE_NAME"
        val cursor= db.rawQuery(query , null)
        val fp = cursor.getColumnIndex(filePath)
        val fn = cursor.getColumnIndex(filename)
        val ts = cursor.getColumnIndex(timestamp)
        val dur = cursor.getColumnIndex(duration)
        val ap = cursor.getColumnIndex(amspath)

        do{
            val audiorecord = AudioRecord(cursor.getString(fp),cursor.getString(fn),
                    cursor.getLong(ts) , cursor.getString(dur), cursor.getString(ap))
            list.add(audiorecord)
        }while(cursor.moveToNext())
        return list
    }

    fun deleteData(path:String):Int{
        val db = this.readableDatabase
        return db.delete(TABLE_NAME, filePath +" = "+ path , null)
    }
}