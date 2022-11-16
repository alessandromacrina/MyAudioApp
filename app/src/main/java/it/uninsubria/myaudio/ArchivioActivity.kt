package it.uninsubria.myaudio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_archivio.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ArchivioActivity : AppCompatActivity() {
    private lateinit var records : ArrayList<AudioRecord>
    private lateinit var myAdapter : AudioRecyclerAdapter
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archivio)

        records = ArrayList()
        myAdapter = AudioRecyclerAdapter(records)

        //inizializzazione database
        db = Room.databaseBuilder(
            this,
            AppDatabase :: class.java,
            "audioRecords"
        ).build()

        db= Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "audioRecords"
        ).build()

        //alla recycleview servono due informazioni: l'adapter e il layout manager
        //che dice come posizionare e riciclare le view

        rv_archivio.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(context)
        }
        fetchAll()
    }

    private fun fetchAll(){
        GlobalScope.launch {
            records.clear()
            var queryResult = db.audioRecordDao().getAll()
            records.addAll(queryResult)

            myAdapter.notifyDataSetChanged()
        }

    }
}