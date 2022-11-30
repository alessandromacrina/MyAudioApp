package it.uninsubria.myaudio

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity(), Timer.OnTimerTickListener {
    private var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false

    private lateinit var recorder: MediaRecorder
    private var dirPath = ""
    private var fileName = ""
    private var isRecording = false
    private var isPaused = false

    private lateinit var timer: Timer
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //verifico se il permesso è stato dato
        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED
        //se non ho i permessi li chiedo
        if(!permissionGranted)
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        //altezza quando la bottom sheet è collapsed
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        timer = Timer(this)

        btn_record.setOnClickListener{
            when{
                isPaused -> resumeRecorder()
                isRecording -> pauseRecorder()
                else -> startRecording()
            }
        }

        btn_list.setOnClickListener {
            //lista
            startActivity(Intent(this, ArchivioActivity::class.java))
            Toast.makeText(this, "List button", Toast.LENGTH_LONG).show()
        }

        btn_done.setOnClickListener {
            stopRecorder()
            Toast.makeText(this, "Record saved", Toast.LENGTH_LONG).show()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBackgroud.visibility = View.VISIBLE
            filenameInput.setText(fileName)
        }

        btn_cancel.setOnClickListener {
            File("$dirPath$fileName.mp3").delete()
            dismiss()
        }

        btn_ok.setOnClickListener{
            dismiss()
            save()
        }

        bottomSheetBackgroud.setOnClickListener{
            File("$dirPath$fileName.mp3").delete()
            dismiss()
        }

        btn_delete.setOnClickListener {
            stopRecorder()
            File("$dirPath$fileName.mp3")
            Toast.makeText(this, "Record deleted", Toast.LENGTH_LONG).show()
        }

        btn_delete.isClickable = false
    }

    //metodo per salvare la registrazione, se l'utente cambia il nome del file viene salvato con il nuovo nome
    private fun save(){
        val newFilename = filenameInput.text.toString()
        if(newFilename != fileName){
            var newFile = File("$dirPath$newFilename.mp3")
            File("$dirPath$fileName.mp3").renameTo(newFile)
        }
    }

    //metodo per nascondere la bottomsheet
    private fun dismiss(){
        bottomSheetBackgroud.visibility = View.GONE
        //bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        hideKeyboard(filenameInput)
        //ritardo l'operazione perchè fatta subito dopo spesso viene saltata
        Handler(Looper.getMainLooper()).postDelayed({
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }, 100)
    }

    //metodo per nascondere la tastiera
    private fun hideKeyboard(view: View){
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //metodo per chiedere il permesso, se viene dato modifico permissionGranted a true
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE)
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
    }


    private fun pauseRecorder(){
        recorder.pause()
        isPaused = true
        btn_record.setImageResource(R.drawable.ic_record)

        timer.pause()
    }

    private fun resumeRecorder(){
        recorder.pause()
        isPaused = false
        btn_record.setImageResource(R.drawable.ic_pause)
        timer.start()
    }

    //metodo per registrare
    private fun startRecording(){
        //controllo se ho i permessi per registrare
        if(!permissionGranted){
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
            return
        }
        //inizio a registrare
        recorder = MediaRecorder()
        dirPath = "${externalCacheDir?.absolutePath}/"
        var simpleDateFormat = SimpleDateFormat("DD.MM.yy_hh.mm.ss")
        var date = simpleDateFormat.format(Date())
        fileName = "audio_record_$date"

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$dirPath$fileName.mp3")

            try {
                prepare()
            }catch (e: IOException){}

            start()
        }


        btn_record.setImageResource(R.drawable.ic_pause)
        isRecording = true
        isPaused = false

        timer.start()

        btn_delete.isClickable = true
        btn_delete.setImageResource(R.drawable.ic_delete)

        btn_list.visibility = View.GONE
        btn_done.visibility = View.VISIBLE
    }

    private fun stopRecorder(){
        timer.stop()

        recorder.apply {
            stop()
            release()
        }

        isPaused = false
        isRecording = false

        //modifico i bottoni e la text view

        btn_list.visibility = View.VISIBLE
        btn_done.visibility = View.GONE

        btn_delete.isClickable = false
        btn_delete.setImageResource(R.drawable.ic_delete_disabled)
        btn_record.setImageResource(R.drawable.ic_record)
        tv_timer.text = "00:00.00"
    }

    override fun onTimerTick(duration: String) {
        tv_timer.text = duration
    }
}