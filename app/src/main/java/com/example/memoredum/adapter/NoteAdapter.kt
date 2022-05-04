package com.example.memoredum.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memoredum.MainActivity
import com.example.memoredum.R
import com.example.memoredum.WriteNoteActivity
import com.example.memoredum.entity.NoteBean
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoteAdapter(context: MainActivity) : PagedListAdapter<NoteBean, NoteAdapter.NoteViewHolder>(NoteAdapter.diffCallback){
    var isEdit : Boolean = false
    var selectAll : Boolean = false
    var deleteList : ArrayList<NoteBean> = ArrayList()
    val context = context

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<NoteBean>() {
            override fun areItemsTheSame(oldItem: NoteBean, newItem: NoteBean): Boolean =
                    oldItem.id == newItem.id
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: NoteBean, newItem: NoteBean): Boolean =
                    oldItem == newItem
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.tv_note_title.text = note?.title
        holder.tv_note_content.text = note?.content
        holder.rl_item_view.setOnClickListener {
            val intent = Intent(context, WriteNoteActivity::class.java)
            intent.putExtra(WriteNoteActivity.EXTRA_TYPE, WriteNoteActivity.EXTRA_UPDATE)
            intent.putExtra(WriteNoteActivity.EXTRA_ID,note?.id)
            intent.putExtra(WriteNoteActivity.EXTRA_TITLE,note?.title)
            intent.putExtra(WriteNoteActivity.EXTRA_CONTENT,note?.content)
            context.startActivity(intent)
        }
        holder.rl_item_view.setOnLongClickListener {
            val list = ArrayList<NoteBean>()
            list.add(note!!)
            context.showDialog(list)
            false
        }
        if (isEdit){
            holder.cb_check.visibility = View.VISIBLE
        }else{
            holder.cb_check.visibility = View.GONE
        }
        holder.cb_check.isChecked = selectAll
        holder.cb_check.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                deleteList.add(note!!)
            }else{
                deleteList.remove(note)
            }
        }
        if (Date() > SimpleDateFormat("yyyy-MM-dd HH:mm").parse(note?.excute_time)){
            holder.iv_over_time.visibility = View.VISIBLE
        }else{
            holder.iv_over_time.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(note?.excute_time)) {
            var time = note?.excute_time?.split(" ")
            var date = Date()
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            var tvLastTime = holder.tv_note_last_time
            if (simpleDateFormat.format(date).equals(time!![0])) {
                tvLastTime?.text = time[1]
            } else {
                tvLastTime?.text = note?.excute_time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(parent)

    inner class NoteViewHolder(parent :ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)) {
        val tv_note_title:TextView = itemView.findViewById(R.id.tv_note_title)
        val tv_note_last_time: TextView = itemView.findViewById(R.id.tv_note_last_time)
        val tv_note_content: TextView = itemView.findViewById(R.id.tv_note_content)
        val cb_check: CheckBox = itemView.findViewById(R.id.cb_check)
        val rl_item_view: RelativeLayout = itemView.findViewById(R.id.rl_item_view)
        val iv_over_time: ImageView = itemView.findViewById(R.id.iv_over_time)
    }

}


