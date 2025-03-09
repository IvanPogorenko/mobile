package com.tsu.mobilecourse.UI.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tsu.mobilecourse.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySearchBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.filter.setOnClickListener {
            binding.filterOverlay.overlay.visibility = View.VISIBLE

            binding.filterOverlay.close.setOnClickListener {
                binding.filterOverlay.overlay.visibility = View.GONE
            }

            for (i in 0 until binding.filterOverlay.interestsContainer.childCount) {
                val child = binding.filterOverlay.interestsContainer.getChildAt(i)
                if (child is TextView) {
                    child.setOnClickListener {
                        it.isActivated = !it.isActivated
                    }
                }
            }

            for (i in 0 until binding.filterOverlay.languageContainer.childCount) {
                val child = binding.filterOverlay.languageContainer.getChildAt(i)
                if (child is TextView) {
                    child.setOnClickListener {
                        it.isActivated = !it.isActivated
                    }
                }
            }

            for (i in 0 until binding.filterOverlay.bioContainer.childCount) {
                val child = binding.filterOverlay.bioContainer.getChildAt(i)
                if (child is TextView) {
                    child.setOnClickListener {
                        it.isActivated = !it.isActivated
                    }
                }
            }
        }

        binding.search.setOnClickListener {
            binding.searchOverlay.overlay.visibility = View.VISIBLE

            binding.searchOverlay.close.setOnClickListener {
                binding.searchOverlay.overlay.visibility = View.GONE
            }

            binding.searchOverlay.write.setOnClickListener {
                val intent = Intent(this, ChatActivity()::class.java)
                startActivity(intent)
            }
        }
    }
}