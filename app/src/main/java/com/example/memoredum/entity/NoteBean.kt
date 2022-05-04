package com.example.memoredum.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_note")
class NoteBean() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var title: String? = null
    var content: String? = null
    var add_time: String? = null
    var update_time: String? = null
    var excute_time: String? = null
    var year: String? = null
    var month: String? = null
    var day: String? = null
    var hour: String? = null
    var min: String? = null
}