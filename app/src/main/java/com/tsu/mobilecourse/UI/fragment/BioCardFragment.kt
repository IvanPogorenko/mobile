package com.tsu.mobilecourse.UI.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tsu.mobilecourse.databinding.FragmentBioCardBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BioCardFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentBioCardBinding
    private var listener: OnButtonClickListener? = null
    interface OnButtonClickListener {
        fun onBioButtonClicked(buttonId: String?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        val args = arguments
        if (args != null) {
            val blockImgResId = args.getInt("blockImg")
            val blockName = args.getString("blockName", "Название")
            val blockDescription = args.getString("blockDescription", "Описание")
            val btnId = args.getString("id")

            binding.blockImg.setImageResource(blockImgResId)
            binding.blockName.text = blockName
            binding.blockDescription.text = blockDescription

            binding.button.setOnClickListener{
                listener?.onBioButtonClicked(btnId)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnButtonClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BioCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}