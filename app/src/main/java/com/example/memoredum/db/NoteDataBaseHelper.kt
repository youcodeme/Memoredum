package com.example.memoredum.db

import android.content.Context
import androidx.room.Room
import com.example.memoredum.dao.NoteDao

class NoteDataBaseHelper(context: Context) {
    private val build:MyDb = Room
        .databaseBuilder(context, MyDb::class.java, "my_db_test")
        .fallbackToDestructiveMigration()//破坏式迁移数据库（第三步那个version改变的时候触发。这里仅做测试，实际开发需要做数据库的迁移）
//        .allowMainThreadQueries()//允许主线程中操作。默认不允许，如果直接在主线程中操作会崩溃。我们这里用了协程，所以不需要声明这个，实际开发中也保证不要在主线程做这种耗时操作
        .build()

    companion object {
        @Volatile
        private var instance: NoteDataBaseHelper? = null
        fun getInstance(context: Context): NoteDataBaseHelper? {
            if (instance == null) {
                synchronized(NoteDataBaseHelper::class.java) {
                    if (instance == null) {
                        instance = NoteDataBaseHelper(context)
                    }
                }
            }
            return instance
        }
    }

    fun getNoteDao(): NoteDao {
        return build.getNoteDao()
    }

}