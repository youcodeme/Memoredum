package com.example.memoredum.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.memoredum.db.NoteDb
import com.example.memoredum.entity.NoteBean
import com.example.memoredum.ioThread

class WriteNoteViewModel(app: Application) : AndroidViewModel(app) {
    val dao = NoteDb.get(app).noteDao()
    fun insertNote(title: String, content: String, add_time: String, update_time: String) = ioThread{
        val note = NoteBean()
        note.title = title
        note.content = content
        note.add_time = add_time
        note.update_time = update_time
        dao?.insertNote(note)
        Log.d("onClick","insertNote")
    }
    fun getNoteById(id:Int): NoteBean{
        var note = dao.getNoteById(id)
        return note
    }
    fun updateNote(noteBean: NoteBean)  = ioThread{
        dao?.updateNote(noteBean)
    }
}