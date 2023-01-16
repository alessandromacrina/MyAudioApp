package it.uninsubria.myaudio

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_archivio.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet_archivio.*
import kotlinx.android.synthetic.main.bottom_sheet_archivio.btn_delete
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ArchivioActivity : AppCompatActivity() , OnItemClickListenerInterface {
    private val authorities ="com.restart.shareaudiofiles.fileprovider"
    private lateinit var records : ArrayList<AudioRecord>
    private lateinit var myAdapter : AudioRecyclerAdapter
    private lateinit var db : DBHelper
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archivio)
        db = DBHelper(this)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetArchivio)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

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

    //metodo per nascondere la bottomsheet
    private fun dismiss(){
        bottomSheetArchivioBG.visibility = View.GONE
        //ritardo l'operazione perchè fatta subito dopo spesso viene saltata
        Handler(Looper.getMainLooper()).postDelayed({
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }, 100)
    }

    //metodo per gestire il click lungo su un elemento della list view
    override fun onItemLongClickListener(position: Int) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetArchivioBG.visibility = View.VISIBLE
        fileNameRename.setText(records[position].filename)

        //elimino la registrazione selezionata
        btn_delete.setOnClickListener {
            db.deleteData(records[position].filePath)
            records.remove(records[position])
            Toast.makeText(this, "Registrazione eliminata", Toast.LENGTH_SHORT).show()
            myAdapter.notifyDataSetChanged()
            dismiss()
        }

        //rinomino la registrazione
        btn_rename.setOnClickListener {
            if (fileNameRename.text.toString() != records[position].filename){
                db.updateName(records[position].filename, fileNameRename.text.toString())
                Toast.makeText(this, "Registrazione rinominata", Toast.LENGTH_SHORT).show()
                myAdapter.notifyDataSetChanged()
                dismiss()
            }
            Toast.makeText(this, "Nome file non modificato", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        //condivido la registrazione
        btn_share.setOnClickListener {
            val path = FileProvider.getUriForFile(this, authorities, File(records[position].filePath))

            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, path)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.type = "audio/mp3"
            startActivity(Intent.createChooser(shareIntent, "Condividi Registrazione"))
            dismiss()
        }
    }

}