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
import kotlin.math.log

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
    private var mIsDefaultTime = true
    private var mType = 0
    private var mId: Int? = null
    private var note: NoteBean? = null

    val triggerAtMillis = System.currentTimeMillis() + 1000 * 60 * 2
    val defaultDate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(triggerAtMillis)

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
//            val triggerAtMillis = System.currentTimeMillis() + 1000 * 60 * 60 * 10
            tv_time_limit.text = "执行时限默认为10小时内有效" + "\n" +
                    "即截止时间为" + "$defaultDate" + "\n" +
                    "若要更改，请在下方输入正确格式时间"

            mIsShowOk = true
        } else if (mType == EXTRA_UPDATE) {
            iv_right?.visibility = View.GONE
            tv_time_limit?.visibility = View.GONE
            mId = intent.getIntExtra(EXTRA_ID,-1)
            note = viewModel.getNoteById(mId!!)
            edit_title?.setText(note?.title)
            edit_content?.setText(note?.content)
            edit_year?.setText(note?.year)
            edit_month?.setText(note?.month)
            edit_day?.setText(note?.day)
            edit_hour?.setText(note?.hour)
            edit_min?.setText(note?.min)
        }
        iv_left?.visibility = View.VISIBLE
        iv_left?.setImageResource(R.mipmap.icon_back)
    }

    fun initListener(){
        iv_right?.setOnClickListener(this)
        iv_left?.setOnClickListener(this)
        edit_title?.setOnFocusChangeListener(this)
        edit_content?.setOnFocusChangeListener(this)
        edit_year?.setOnFocusChangeListener(this)
        edit_month?.setOnFocusChangeListener(this)
        edit_day?.setOnFocusChangeListener(this)
        edit_hour?.setOnFocusChangeListener(this)
        edit_min?.setOnFocusChangeListener(this)
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v.id) {
            R.id.edit_title, R.id.edit_content, R.id.edit_year, R.id.edit_month, R.id.edit_day, R.id.edit_hour, R.id.edit_min ->
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
        Log.d("addNote","addNote")
        mIsDefaultTime = (edit_year.text.toString() == ""
                && edit_month.text.toString() == ""
                && edit_day.text.toString() == ""
                && edit_hour.text.toString() == ""
                && edit_min.text.toString() == "")


        viewModel.insertNote(edit_title?.text.toString(),edit_content?.text.toString(),
                SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()),
                SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()),
                edit_year.text.toString(),edit_month.text.toString(),edit_day.text.toString(),
                edit_hour.text.toString(),edit_min.text.toString(),mIsDefaultTime,SimpleDateFormat("yyyy-MM-dd HH:mm").parse(defaultDate))
    }

    fun updateNote(){
        Log.d("updateNote","updateNote")
        var note = viewModel.getNoteById(mId!!)
        val title = edit_title?.text.toString()
        val content = edit_content?.text.toString()
        val updateTime = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
        val excute_time = edit_year.text.toString() + "-" +
                edit_month.text.toString() + "-" +
                edit_day.text.toString() + " " +
                edit_hour.text.toString() + ":" +
                edit_min.text.toString()
        note.title = title
        note.content = content
        note.update_time = updateTime
        note.excute_time = excute_time
        note.year = edit_year.text.toString()
        note.month = edit_month.text.toString()
        note.day = edit_day.text.toString()
        note.hour = edit_hour.text.toString()
        note.min = edit_min.text.toString()
        viewModel.updateNote(note)
    }

}