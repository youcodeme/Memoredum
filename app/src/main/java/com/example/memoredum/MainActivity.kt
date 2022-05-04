package com.example.memoredum

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.memoredum.WriteNoteActivity.Companion.EXTRA_ADD
import com.example.memoredum.WriteNoteActivity.Companion.EXTRA_TYPE
import com.example.memoredum.adapter.NoteAdapter
import com.example.memoredum.entity.NoteBean
import com.example.memoredum.impl.TextWatcherImpl
import com.example.memoredum.vm.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() , View.OnClickListener{


    val viewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }
    val context = this

    private var adapter: NoteAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        initListener()

        adapter = NoteAdapter(this)//继承PagedListAdapter的类对象
        recyclerView?.adapter = adapter //为RecyclerView添加适配器
        initDataSource()
        edit_search_context.addTextChangedListener(object : TextWatcherImpl() {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 0){
                    initDataSource()
                }else{
                    viewModel.getNoteByContentOrTitle(s.toString())
                    viewModel.allNotes.observe(context, Observer {
                        adapter!!.submitList(it)
                        tv_note_count?.text = "${it.size}个备忘录"
                    })
                }

            }
        })
    }

    fun initDataSource(){
        viewModel.initDataSource()
        viewModel.allNotes.observe(context, Observer {
            adapter!!.submitList(it)
            tv_note_count?.text = "${it.size}个备忘录"
        })
    }

    fun initListener(){
        iv_switch_view?.setOnClickListener(this)
        tv_order_by?.setOnClickListener(this)
        ll_select_all?.setOnClickListener(this)
        rl_delete?.setOnClickListener(this)
        iv_add_note?.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        Log.d("onClick","onClick");
        when (v?.id) {
            R.id.iv_switch_view -> {
                setEditState()
                Log.d("onClick","iv_switch_view")
//                viewModel.insertNote("标题","内容",
//                    SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()),SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))
            }
            R.id.tv_order_by -> {
                Log.d("onClick","tv_order_by");

            }
            R.id.rl_delete -> {
                showDialog(adapter!!.deleteList)
            }
            R.id.ll_select_all -> {
                setSelectAllState()
            }
            R.id.iv_add_note -> {
                val intent = Intent(this,WriteNoteActivity::class.java)
                intent.putExtra(EXTRA_TYPE,EXTRA_ADD)
                startActivity(intent)
            }
        }
    }

    fun deleteNotes(notes : ArrayList<NoteBean>){
        viewModel.deleteNotes(ArrayList(notes))
    }

    fun setEditState(){
        if (adapter?.isEdit!!){
            adapter?.isEdit = false
            ll_select_all?.visibility = View.GONE
            rl_delete?.visibility = View.GONE
            ll_bottom?.visibility = View.VISIBLE

            adapter?.selectAll = false
            iv_select_all?.setImageResource(R.mipmap.ic_checked_gray)
            clearDeleteList()
        }else{
            adapter?.isEdit = true
            ll_select_all?.visibility = View.VISIBLE
            rl_delete?.visibility = View.VISIBLE
            ll_bottom?.visibility = View.GONE
        }
        adapter!!.notifyDataSetChanged()
    }
    fun setSelectAllState(){
        if (adapter?.selectAll!!){
            adapter?.selectAll = false
            iv_select_all?.setImageResource(R.mipmap.ic_checked_gray)
        }else{
            adapter?.selectAll = true
            iv_select_all?.setImageResource(R.drawable.icon_checked)
        }
        adapter!!.notifyDataSetChanged()
    }

    /**
     * 清除删除列表
     */
    fun clearDeleteList(){
        adapter!!.deleteList.clear()
    }

    public fun showDialog(list : ArrayList<NoteBean>) {
        val builder = AlertDialog.Builder(this);
        builder.setTitle("提示")
        builder.setMessage("确定要删除吗？")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    deleteNotes(list)
                    Log.e("TAG", "click yes")
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    Log.e("TAG", "click no")
                }
            }
        }
        builder.setPositiveButton("YES", dialogClickListener)
        builder.setNegativeButton("No", dialogClickListener)
        builder.create().show();
    }


}