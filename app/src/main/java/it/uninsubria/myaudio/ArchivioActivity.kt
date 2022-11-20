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
        var audioRecords : ArrayList<AudioRecord> = ArrayList()
        var cursor: Cursor
        audioRecords = db.readData()
        if(audioRecords.size == 0)
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()

        else {


        }
    }

}