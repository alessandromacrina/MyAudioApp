package it.uninsubria.myaudio

import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_archivio.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.FieldPosition

class ArchivioActivity : AppCompatActivity() , OnItemClickListener {
    private lateinit var records : ArrayList<AudioRecord>
    private lateinit var myAdapter : AudioRecyclerAdapter
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archivio)

        records = ArrayList()
        myAdapter = AudioRecyclerAdapter(records , this)

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



    //va a prendere tutti i dati
    private fun fetchAll(){
        GlobalScope.launch {
            records.clear()
            val queryResult = db.audioRecordDao().getAll()
            records.addAll(queryResult)

            myAdapter.notifyDataSetChanged()
        }

    }

    override fun onClickListener(position: Int) {
        Toast.makeText(this, "Tocco semplice", Toast.LENGTH_SHORT).show()
    }

    override fun onLongClickListener(position: Int) {
        TODO("Not yet implemented")
    }
}