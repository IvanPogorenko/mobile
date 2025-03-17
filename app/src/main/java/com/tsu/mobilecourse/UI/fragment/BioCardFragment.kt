package com.tsu.mobilecourse.UI.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tsu.mobilecourse.databinding.FragmentBioCardBinding

class BioCardFragment : Fragment() {
    private var blockImg: Int = 0
    private var blockName: String = ""
    private var blockDescription: String = ""
    private var buttonClickListener: (() -> Unit)? = null

    private lateinit var binding: FragmentBioCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            blockImg = it.getInt(ARG_BLOCK_IMG)
            blockName = it.getString(ARG_BLOCK_NAME, "")
            blockDescription = it.getString(ARG_BLOCK_DESCRIPTION, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBioCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.blockImg.setImageResource(blockImg)
        binding.blockName.text = blockName
        binding.blockDescription.text = blockDescription

        binding.button.setOnClickListener{
            buttonClickListener?.invoke()
        }
    }

    fun setOnButtonClickListener(listener: () -> Unit) {
        buttonClickListener = listener
    }

    companion object {
        private const val ARG_BLOCK_IMG = "blockImg"
        private const val ARG_BLOCK_NAME = "blockName"
        private const val ARG_BLOCK_DESCRIPTION = "blockDescription"
        @JvmStatic
        fun newInstance(blockImg: Int, blockName: String, blockDescription: String) =
            BioCardFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_BLOCK_IMG, blockImg)
                    putString(ARG_BLOCK_NAME, blockName)
                    putString(ARG_BLOCK_DESCRIPTION, blockDescription)
                }
            }
    }
}