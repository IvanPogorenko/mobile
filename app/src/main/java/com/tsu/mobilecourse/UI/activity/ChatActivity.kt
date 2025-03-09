package com.tsu.mobilecourse.UI.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tsu.mobilecourse.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity(){

    private lateinit var binding: ActivityChatBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}