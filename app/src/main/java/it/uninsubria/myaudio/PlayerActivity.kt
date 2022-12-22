package it.uninsubria.myaudio

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.player_activity.*
import java.text.DecimalFormat
import java.text.NumberFormat

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btnPlay: ImageButton
    private lateinit var seekBar: SeekBar

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var delay = 0L

    private lateinit var tvTimeLeft: TextView
    private lateinit var tvTimeRight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        btnPlay = findViewById(R.id.btn_play)
        var filepath = intent.getStringExtra("filepath")
        var filename = intent.getStringExtra("filename")



        mediaPlayer = MediaPlayer()
        seekBar = findViewById(R.id.seekBar_play)
        tvTimeLeft= findViewById(R.id.tv_timeleft)
        tvTimeRight=findViewById(R.id.tv_timeright)
        mediaPlayer.apply {
            setDataSource(filepath)
            prepare()
        }
        tvTimeRight.text= durationFormat(mediaPlayer.duration)

        btnPlay.setOnClickListener{
            playPause()
        }

        handler= Handler(Looper.getMainLooper())
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            tvTimeLeft.text = durationFormat(mediaPlayer.currentPosition)
            handler.postDelayed(runnable , delay)
        }
        playPause()
        seekBar.max = mediaPlayer.duration

        mediaPlayer.setOnCompletionListener {
            stop()
        }

    }
    private fun playPause(){
        if(!mediaPlayer.isPlaying){
            mediaPlayer.start()
            btnPlay.background = ResourcesCompat.getDrawable(resources , R.drawable.ic_round_pause_circle_filled_24 , theme)
            handler.postDelayed(runnable,delay) //chiama la funzione in loop ogni secondo

        }
        else
        {
            mediaPlayer.pause()
            stop()
        }


        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{

            //se cambiamento avvenuto per via dell'utente p2->true e la seekbar prenderà il valore p1
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2)
                    mediaPlayer.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }


        })
    }

    private fun stop(){
        btnPlay.background = ResourcesCompat.getDrawable(resources , R.drawable.ic_round_play_circle_filled_24 , theme)
        handler.removeCallbacks(runnable) //rimuove il runnable dalla lista degli oggetti per il loop
    }

    private fun durationFormat(duration: Int) : String{
        var dur = duration / 1000
        var s= dur%60
        var m = (dur/60)%60
        var h = (((dur-m*60)/60)/60).toInt()

        val format : NumberFormat = DecimalFormat("00")
        var str = "$m : ${format.format(s)}"

        if(h>0)
        {
            str= "$h : $m"
        }
        return str
    }
}