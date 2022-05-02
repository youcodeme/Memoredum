package com.example.kotlinnote.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_note")
class NoteBean() : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var title: String? = null
    var content: String? = null
    var add_time: String? = null
    var update_time: String? = null

    constructor(id: Int, title: String, content: String, add_time: String, update_time: String) : this() {
        this.id = id
        this.title = title
        this.content = content
        this.add_time = add_time
        this.update_time = update_time
    }

}