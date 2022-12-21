package it.uninsubria.myaudio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_archivio.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ArchivioActivity : AppCompatActivity() , OnItemClickListenerInterface {
    private lateinit var records : ArrayList<AudioRecord>
    private lateinit var myAdapter : AudioRecyclerAdapter
    private lateinit var db : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archivio)
        db = DBHelper(this)
        records = db.readData()
        if(records.size <= 0)
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()

        myAdapter = AudioRecyclerAdapter(records , this)

        rv_archivio.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(context)
        }
        fetchAll()
    }

    private fun fetchAll(){
        GlobalScope.launch {
            records.clear()
            var queryResult = db.readData()
            records.addAll(queryResult)
            myAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClickLister(position: Int) {
        var audioRecord = records[position]
        var intent = Intent(this , PlayerActivity::class.java )
        intent.putExtra("filepath" , audioRecord.filePath)
        Log.i("SEGNALAZIONE_FPATH" , audioRecord.filePath)
        intent.putExtra("filename" , audioRecord.filename)
        Log.i("SEGNALAZIONE_FNAME" , audioRecord.filename)
        startActivity(intent)

    }

    override fun onItemLongClickListener(position: Int) {
        Toast.makeText(this, "click prolungato", Toast.LENGTH_SHORT).show()
    }

}