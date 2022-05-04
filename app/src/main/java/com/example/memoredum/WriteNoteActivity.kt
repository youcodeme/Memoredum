package com.example.memoredum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.example.memoredum.entity.NoteBean
import com.example.memoredum.vm.NoteViewModel
import com.example.memoredum.vm.WriteNoteViewModel
import kotlinx.android.synthetic.main.activity_write_note.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class WriteNoteActivity : AppCompatActivity()  , View.OnClickListener, View.OnFocusChangeListener {

    companion object {
        val EXTRA_ADD = 1
        val EXTRA_UPDATE = 2
        val EXTRA_TYPE = "EXTRA_TYPE" //是新增还是修改
        val EXTRA_TITLE = "EXTRA_TITLE" //标题
        val EXTRA_CONTENT = "EXTRA_CONTENT" //内容
        val EXTRA_ID = "EXTRA_ID" //备忘录id
    }
    private var mIsShowOk = false //是否正在显示完成的按钮--说明这个时候在编辑
    private var mType = 0
    private var mTitle: String? = null
    private var mContent: kotlin.String? = null
    private var mId: Int? = null

    val viewModel by lazy { ViewModelProviders.of(this).get(WriteNoteViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_note)
        initView()
        initListener()
    }

    fun initView(){
        mType = intent.getIntExtra(EXTRA_TYPE, mType)
        tv_title?.visibility = View.GONE
        if (mType == EXTRA_ADD) {
            iv_right?.visibility = View.VISIBLE
            iv_right?.setImageResource(R.mipmap.icon_ok)
            mIsShowOk = true
        } else if (mType == EXTRA_UPDATE) {
            iv_right?.visibility = View.GONE
            mTitle = intent.getStringExtra(EXTRA_TITLE)
            mContent = intent.getStringExtra(EXTRA_CONTENT)
            mId = intent.getIntExtra(EXTRA_ID,-1)
            edit_title?.setText(mTitle)
            edit_content?.setText(mContent)
        }
        iv_left?.visibility = View.VISIBLE
        iv_left?.setImageResource(R.mipmap.icon_back)
    }

    fun initListener(){
        iv_right?.setOnClickListener(this)
        iv_left?.setOnClickListener(this)
        edit_title?.setOnFocusChangeListener(this)
        edit_content?.setOnFocusChangeListener(this)
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v.id) {
            R.id.edit_title, R.id.edit_content ->
                if (!mIsShowOk && hasFocus) {
                    iv_right?.visibility = View.VISIBLE
                    iv_right?.setImageResource(R.mipmap.icon_ok)
                    mIsShowOk = true
                }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.iv_left -> {
                finish()
            }
            R.id.iv_right -> {
                if (mType == EXTRA_ADD){
                    addNote()
                }else if (mType == EXTRA_UPDATE){
                    updateNote()
                }
                finish()
            }
        }
    }

    fun addNote(){
        viewModel.insertNote(edit_title?.text.toString(),edit_content?.text.toString(),
                SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()),SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))
    }

    fun updateNote(){
        var note = viewModel.getNoteById(mId!!)
        val title = edit_title?.text.toString()
        val content = edit_content?.text.toString()
        val updateTime = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
        note.title = title
        note.content = content
        note.update_time = updateTime
        viewModel.updateNote(note)
    }

}