package com.tsu.mobilecourse.UI.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tsu.mobilecourse.databinding.ActivityTestsBinding

class TestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestsBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}