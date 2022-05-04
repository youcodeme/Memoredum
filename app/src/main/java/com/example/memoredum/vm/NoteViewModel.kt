package com.example.memoredum.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.paging.*
import com.example.memoredum.db.NoteDb
import com.example.memoredum.entity.NoteBean
import com.example.memoredum.ioThread


class NoteViewModel(app: Application) : AndroidViewModel(app) {
    val dao = NoteDb.get(app).noteDao()

    var allNotes = dao.getAllNotes().toLiveData(Config(
            pageSize = 30,
            enablePlaceholders = true,
            maxSize = 200))


    fun insertNote(title: String, content: String, add_time: String, update_time: String) = ioThread{
        val note = NoteBean()
        note.title = title
        note.content = content
        note.add_time = add_time
        note.update_time = update_time
        dao?.insertNote(note)
        Log.d("onClick","insertNote")
    }

    fun deleteNotes(notes : ArrayList<NoteBean>){
        notes.forEach {
            deleteNote(it)
        }
    }

    fun deleteNote(noteBean: NoteBean) = ioThread{
        dao?.deleteNote(noteBean)
    }

    fun updateNote(noteBean: NoteBean) = ioThread{
        dao?.updateNote(noteBean)
    }

    fun getNoteById(id:Int): NoteBean {
        var note = dao.getNoteById(id)
        return note
    }

    fun getNoteByContentOrTitle(content: String) {
        allNotes = dao.searchNotes(content).toLiveData(Config(
            pageSize = 30,
            enablePlaceholders = true,
            maxSize = 200))
    }

    fun initDataSource(){
        allNotes = dao.getAllNotes().toLiveData(Config(
            pageSize = 30,
            enablePlaceholders = true,
            maxSize = 200))
    }

}



