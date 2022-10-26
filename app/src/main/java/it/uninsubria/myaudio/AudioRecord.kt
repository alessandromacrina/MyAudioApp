package it.uninsubria.myaudio

import android.provider.Settings.NameValueTable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName= "audioRecords")
data class AudioRecord(
    var filaname : String,
    var filePath : String,
    var timestamp : Long,
    var duration : String,
    var amspath: String
){
    @PrimaryKey(autoGenerate = true)
    var id =0
    @Ignore
    var checked=false
}
