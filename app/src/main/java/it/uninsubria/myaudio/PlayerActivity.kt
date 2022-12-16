package it.uninsubria.myaudio

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btnPlay : ImageButton
    private lateinit var seekBar : SeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)

        var filepath = intent.getStringExtra("filepath")
        var filename = intent.getStringExtra("filename")

        mediaPlayer = MediaPlayer()
        mediaPlayer.apply {
            setDataSource("filepath")
            prepare()
        }
        btnPlay = findViewById(R.id.btn_play)
        seekBar = findViewById(R.id.seekBar_play)

        btnPlay.setOnClickListener{



        }




    }
    private fun playPause(){
        if(!mediaPlayer.isPlaying){
            mediaPlayer.start()
            btnPlay.background = ResourcesCompat.getDrawable(resources , R.drawable.ic_round_pause_circle_filled_24 , theme)

        }
        else
        {
            mediaPlayer.pause()
            btnPlay.background = ResourcesCompat.getDrawable(resources , R.drawable.ic_round_play_circle_filled_24 , theme)
        }
    }
}