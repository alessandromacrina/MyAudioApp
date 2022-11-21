package it.uninsubria.myaudio

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.icu.text.AlphabeticIndex
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class AudioRecyclerAdapter(var records : ArrayList<AudioRecord>) : RecyclerView.Adapter<AudioRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener , View.OnLongClickListener{
        var tvItem : TextView = itemView.findViewById(R.id.tv_tvItem)
        var tvItem2 : TextView = itemView.findViewById(R.id.tv_tvItem2)
        var checkbox : CheckBox = itemView.findViewById(R.id.checkbox)

        init{
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

        override fun onLongClick(p0: View?): Boolean {
            TODO("Not yet implemented")
        }


    }

    //ritorna un oggetto viewholder e gli passa un oggetto view
    //che è il contenuto del layout. ogni elemento viene associato ad una variabile
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position !=RecyclerView.NO_POSITION){ //quando cerchiamo di inserire mentre sta ancora caricando
            val record : AudioRecord = records[position]
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val date = Date(record.timestamp)
            var strDate = sdf.format(date)

            holder.tvItem.text = record.filename
            holder.tvItem2.text = record.duration

        }
    }

    override fun getItemCount(): Int {
        return records.size;
    }

}