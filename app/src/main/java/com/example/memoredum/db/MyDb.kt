package com.example.memoredum.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlinnote.entity.NoteBean
import com.example.memoredum.dao.NoteDao

@Database(entities = [NoteBean::class],version = 1)
abstract class MyDb constructor(): RoomDatabase(){
    abstract fun getNoteDao(): NoteDao
}