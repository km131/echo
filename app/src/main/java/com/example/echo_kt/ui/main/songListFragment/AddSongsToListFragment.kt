package com.example.echo_kt.ui.main.songListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.echo_kt.databinding.AddSongsToListFragmentBinding

class AddSongsToListFragment : Fragment() {

    private var _binding: AddSongsToListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= AddSongsToListFragmentBinding.inflate(inflater,container,false)
        initToolBar()
        initOnClick()
        return binding.root
    }
    private fun initOnClick() {
        binding.apply {
            //跳转搜索
            setJumpSearchAdd {
                //TODO
            }
            clLocal.setOnClickListener {
                val action =
                    AddSongsToListFragmentDirections.actionAddSongsToListFragmentToAddSongsFragment(
                        SongListType.LOCAL
                    )
                it.findNavController().navigate(action)
            }
            clHistory.setOnClickListener {
                val action =
                    AddSongsToListFragmentDirections.actionAddSongsToListFragmentToAddSongsFragment(
                        SongListType.HISTORY
                    )
                it.findNavController().navigate(action)
            }
            clLike.setOnClickListener {
                val action =
                    AddSongsToListFragmentDirections.actionAddSongsToListFragmentToAddSongsFragment(
                        SongListType.LIKE
                    )
                it.findNavController().navigate(action)
            }
        }
    }

    private fun initToolBar() {
        binding.apply {
            //返回键
            addToolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
        }
    }

}

enum class SongListType{
    LOCAL,HISTORY,LIKE
}