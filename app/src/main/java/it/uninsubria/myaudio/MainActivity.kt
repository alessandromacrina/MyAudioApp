package it.uninsubria.myaudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //connessione con il database
        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "audioRecords"
        ).build()

        for(i in 1..3){
            var j = 1;
            var a= AudioRecord("filaname" + j, "filePath" +j, 11 , "11.22", "amspath"+j)
            db.audioRecordDao().insert()
            j++;
        }


    }


}