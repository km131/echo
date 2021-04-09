package com.example.echo_kt.ui

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.NavHostFragment
import com.example.echo_kt.R
import com.example.echo_kt.databinding.FragmentSongListDialogBinding

/**
 * 此处其实应该直接继承Dialog，但因为nav导航组件跳转需要传入fragment，
 * dialog似乎不能获取fragment故而选择继承DialogFragment
 */
class SongListDialogFragment: DialogFragment() {

    private var _binding: FragmentSongListDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_song_list_dialog, null, false)
        binding.setOnClick {
            when(it.id){
                R.id.ll_sort -> Toast.makeText(this.context,"功能待添加",Toast.LENGTH_SHORT).show()
                R.id.ll_add -> NavHostFragment.findNavController(this).navigate(R.id.action_customSongListFragment_to_searchFragment)
                R.id.ll_edit -> Toast.makeText(this.context,"功能待添加",Toast.LENGTH_SHORT).show()
                R.id.btn_cancel -> dismiss()
            }
        }
        return binding.root
    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //点击返回键关闭对话框
        isCancelable = true
        //点击对话框外部关闭对话框
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val metrics = resources.displayMetrics
            //宽度设为屏幕宽度的90%
            dialog.window?.setLayout( (metrics.widthPixels * 0.9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }
    }
}