package it.uninsubria.myaudio

import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_archivio.*

class ArchivioActivity : AppCompatActivity() {
    private lateinit var records : ArrayList<AudioRecord>
    private lateinit var myAdapter : AudioRecyclerAdapter
    //private lateinit var db : AppDatabase
    private lateinit var db : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archivio)
        db = DBHelper(this)

        var cursor: Cursor
        db.readData().also { cursor = it }

        if(cursor.count==0)
            Toast.makeText(this, "NO ELEMENTS", Toast.LENGTH_SHORT).show()
        else {
            var buf: StringBuffer = StringBuffer()
            while(cursor.moveToNext())
            {
                buf.append("filepath :" + cursor.getString(0)+ "\n")


            }

        }
    }

}