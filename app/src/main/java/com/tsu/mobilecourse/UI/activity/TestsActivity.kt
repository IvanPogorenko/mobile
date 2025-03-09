package com.tsu.mobilecourse.UI.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tsu.mobilecourse.UI.fragment.TestCardFragment
import com.tsu.mobilecourse.databinding.ActivityTestsBinding

class TestsActivity : AppCompatActivity(), TestCardFragment.OnButtonClickListener{

    private lateinit var binding: ActivityTestsBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val blocks = listOf(
            mapOf(
                "blockImg" to com.tsu.mobilecourse.R.drawable.mbti,
                "blockName" to "MBTI",
                "descriptionSmall" to "Узнайте ваш тип  личности и помогите в подборе собеседника",
                "descriptionBig" to "Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание",
                "id" to "empty"
            ),
            mapOf(
                "blockImg" to com.tsu.mobilecourse.R.drawable.mbti,
                "blockName" to "MBTI",
                "descriptionSmall" to "Узнайте ваш тип  личности и помогите в подборе собеседника",
                "descriptionBig" to "Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание Подробное описание ",
                "head" to "Ваш тип личности - INFJ",
                "id" to "filled"
            )
        )

        if (savedInstanceState == null) {
            for (card in blocks) {
                val fragment = TestCardFragment()
                val bundle = Bundle().apply {
                    putInt("blockImg", card["blockImg"] as Int)
                    putString("blockName", card["blockName"] as String)
                    putString("descriptionSmall", card["descriptionSmall"] as String)
                    putString("descriptionBig", card["descriptionBig"] as String)
                    card["head"]?.let {
                        putString("head", it as String)
                    }
                    putString("id", card["id"] as String)
                }
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .add(binding.fragmentsContainer.id, fragment)
                    .commit()
            }
        }
    }

    override fun onTestButtonClicked(buttonId: String?) {
        when (buttonId) {
            "MBTI" -> {
                binding.testOverlay.overlay.visibility = View.VISIBLE
                binding.testOverlay.close.setOnClickListener {
                    binding.testOverlay.overlay.visibility = View.GONE
                }
                for (i in 0 until binding.testOverlay.circleContainer.childCount){
                    val child = binding.testOverlay.circleContainer.getChildAt(i) as LinearLayout
                    val circle = child.getChildAt(0)
                    if (circle is View){
                        circle.setOnClickListener {
                            it.isActivated = !it.isActivated
                        }
                    }
                }
            } else ->{

            }
        }
    }
}