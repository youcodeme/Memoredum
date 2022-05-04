package com.example.memoredum.vm

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.memoredum.db.NoteDb
import com.example.memoredum.entity.NoteBean
import com.example.memoredum.ioThread
import com.example.memoredum.receiver.MyBroadcast
import kotlinx.android.synthetic.main.activity_write_note.*
import java.text.SimpleDateFormat
import java.util.*

class WriteNoteViewModel(app: Application) : AndroidViewModel(app) {
    val context = app
    val dao = NoteDb.get(app).noteDao()
    fun insertNote(title: String, content: String, add_time: String, update_time: String,
                   year: String, month: String, day: String, hour: String, min: String,mIsDefaultTime: Boolean
                    ,defaultDate:Date) = ioThread{
        val note = NoteBean()
        if (mIsDefaultTime){
            note.title = title
            note.content = content
            note.add_time = add_time
            note.update_time = update_time
            note.excute_time = SimpleDateFormat("yyyy-MM-dd HH:mm").format(defaultDate)
            note.year = SimpleDateFormat("yyyy").format(defaultDate)
            note.month = SimpleDateFormat("MM").format(defaultDate)
            note.day = SimpleDateFormat("dd").format(defaultDate)
            note.hour = SimpleDateFormat("HH").format(defaultDate)
            note.min = SimpleDateFormat("mm").format(defaultDate)
            dao?.insertNote(note)
        }else{
            val excute_time = year + "-" +
                    month + "-" +
                    day + " " +
                    hour + ":" +
                    min
            note.title = title
            note.content = content
            note.add_time = add_time
            note.update_time = update_time
            note.excute_time = excute_time
            note.year = year
            note.month = month
            note.day = day
            note.hour = hour
            note.min = min
            dao?.insertNote(note)
        }
        setNotesNotification(note)
    }
    fun getNoteById(id:Int): NoteBean{
        var note = dao.getNoteById(id)
        return note
    }
    fun updateNote(noteBean: NoteBean)  = ioThread{
        dao?.updateNote(noteBean)
        setNotesNotification(noteBean)
    }
    fun setNotesNotification(it : NoteBean){
        val current = System.currentTimeMillis()
        val triggerAtMillis = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(it?.excute_time).time
        if (triggerAtMillis - current > 0) {
            val intent = Intent(context, MyBroadcast::class.java)
            intent.action = "NOTIFICATION"
            intent.putExtra("title", it.title)
            intent.putExtra("content", it.content)
            val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
            val manager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val type = AlarmManager.RTC_WAKEUP
            Log.d("NOTIFICATION", triggerAtMillis.toString())
            Log.d("NOTIFICATION", (triggerAtMillis - current).toString())
            Log.d("NOTIFICATION", SimpleDateFormat("yyyy-MM-dd HH:mm").format(triggerAtMillis))
            manager.set(type, triggerAtMillis - current, pi)
        }
    }
}