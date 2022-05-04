package com.example.memoredum.dao

import androidx.room.*
import androidx.paging.DataSource
import com.example.memoredum.entity.NoteBean

@Dao
interface NoteDao {
    @Insert
    fun insertNote(noteBean: NoteBean)

    @Update
    fun updateNote(noteBean: NoteBean)

    @Delete
    fun deleteNote(noteBean: NoteBean)

    @Query("select * from table_note where id = :id")
    fun getNoteById(id:Int): NoteBean

    @Query("select * from table_note where title like :title")
    fun getNotesByTitle(title:String):List<NoteBean>

    @Query("select * from table_note")
    fun getAllNotes(): DataSource.Factory<Int, NoteBean>

    @Query("select * from table_note where title like '%'||:content||'%' or content like '%'||:content||'%'")
    fun searchNotes(content: String): DataSource.Factory<Int, NoteBean>


}