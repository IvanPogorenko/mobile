package com.tsu.mobilecourse.UI.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.tsu.mobilecourse.databinding.FragmentTestCardBinding
import com.tsu.mobilecourse.databinding.FragmentTestCardResultBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TestCardFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: ViewBinding
    private var listener: OnButtonClickListener? = null
    interface OnButtonClickListener {
        fun onTestButtonClicked(buttonId: String?)
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
        val id = arguments?.getString("id")

        binding = if (id == "empty") {
            FragmentTestCardBinding.inflate(inflater, container, false)
        } else {
            FragmentTestCardResultBinding.inflate(inflater, container, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        if (args != null) {
            val blockImgResId = args.getInt("blockImg")
            val blockName = args.getString("blockName", "Название")
            val descriptionSmall = args.getString("descriptionSmall", "Описание")
            val descriptionBig = args.getString("descriptionBig", "Описание")
            val blockId = args.getString("id")
            val head = args.getString("head")

            if (blockId == "empty") {
                val bindingEmpty = binding as FragmentTestCardBinding
                bindingEmpty.mainImage.setImageResource(blockImgResId)
                bindingEmpty.name.text = blockName
                bindingEmpty.descriptionSmall.text = descriptionSmall
                bindingEmpty.descriptionBig.text = descriptionBig

                bindingEmpty.goTest.setOnClickListener {
                    listener?.onTestButtonClicked(bindingEmpty.name.text.toString())
                }

                bindingEmpty.moreBtn.setOnClickListener {
                    if (bindingEmpty.more.visibility == View.VISIBLE){
                        bindingEmpty.more.visibility == View.GONE
                    } else{
                        bindingEmpty.more.visibility = View.VISIBLE
                    }
                    val rotation = ObjectAnimator.ofFloat(bindingEmpty.moreBtn, "rotation", bindingEmpty.moreBtn.rotation, bindingEmpty.moreBtn.rotation + 180f)
                    rotation.duration = 300
                    rotation.start()
                }

            } else {
                val bindingResult = binding as FragmentTestCardResultBinding
                bindingResult.mainImage.setImageResource(blockImgResId)
                bindingResult.name.text = blockName
                bindingResult.descriptionSmall.text = descriptionSmall
                bindingResult.descriptionBig.text = descriptionBig
                bindingResult.head.text = head

                bindingResult.moreBtn.setOnClickListener {
                    if (bindingResult.more.visibility == View.VISIBLE){
                        bindingResult.more.visibility == View.GONE
                    } else{
                        bindingResult.more.visibility = View.VISIBLE
                    }
                    val rotation = ObjectAnimator.ofFloat(bindingResult.moreBtn, "rotation", bindingResult.moreBtn.rotation, bindingResult.moreBtn.rotation + 180f)
                    rotation.duration = 300
                    rotation.start()
                }
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
            TestCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}