package it.uninsubria.myaudio

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AudioRecordDao {
    @Query("SELECT * FROM audioRecords")
    fun getAll(): List<AudioRecord> //quando si chiama la funzione si esegue la query

    @Insert //room saprà cosa fare con l'argomento passato perchè data la notazione @insert
    fun insert(vararg audioRecord: AudioRecord)

    @Delete
    fun delete(audioRecord: AudioRecord)

    @Delete
    fun delete(audioRecord: Array<AudioRecord>)

    @Update //riconosce l'id e aggiorna la giusta colonna nel db
    fun updata(audioRecord: AudioRecord)

}