package it.uninsubria.myaudio

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btnPlay: ImageButton
    private lateinit var seekBar: SeekBar

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var delay = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        btnPlay = findViewById(R.id.btn_play)
        var filepath = intent.getStringExtra("filepath")
        var filename = intent.getStringExtra("filename")



        mediaPlayer = MediaPlayer()
        seekBar = findViewById(R.id.seekBar_play)
        mediaPlayer.apply {
            setDataSource(filepath)
            prepare()
        }

        //playPause()

        btnPlay.setOnClickListener{
            playPause()
        }

        handler= Handler(Looper.getMainLooper())
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
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


            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun stop(){
        btnPlay.background = ResourcesCompat.getDrawable(resources , R.drawable.ic_round_play_circle_filled_24 , theme)
        handler.removeCallbacks(runnable) //rimuove il runnable dalla lista degli oggetti per il loop
    }
}