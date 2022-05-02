package com.example.memoredum.dao

import androidx.room.*
import com.example.kotlinnote.entity.NoteBean

@Dao
interface NoteDao {
    @Insert
    suspend fun insertNote(noteBean: NoteBean)

    @Update
    suspend fun updateNote(noteBean: NoteBean)

    @Delete
    suspend fun deleteNote(noteBean: NoteBean)

    @Query("select * from table_note where id = :id")
    suspend fun getNote(id:Int):NoteBean?

    @Query("select * from table_note where title like :title")
    suspend fun getNotes(title:String):List<NoteBean>

    @Query("select * from table_note")
    suspend fun getAllNotes():List<NoteBean>
}