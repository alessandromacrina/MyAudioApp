package it.uninsubria.myaudio

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_archivio.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ArchivioActivity : AppCompatActivity() , OnItemClickListenerInterface {
    private lateinit var records : ArrayList<AudioRecord>
    private lateinit var myAdapter : AudioRecyclerAdapter
    //private lateinit var db : AppDatabase
    private lateinit var db : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archivio)
        db = DBHelper(this)
        //var cursor: Cursor
        val audioRecords: ArrayList<AudioRecord> = db.readData()
        if(audioRecords.size == 0)
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()

        myAdapter = AudioRecyclerAdapter(audioRecords , this)


        rv_archivio.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(context)
        }

        fetchAll()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun fetchAll(){
        GlobalScope.launch {
            records.clear()
            var queryResult = db.readData()
            records.addAll(queryResult)

            myAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClickLister(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClickListener(position: Int) {
        TODO("Not yet implemented")
    }

}