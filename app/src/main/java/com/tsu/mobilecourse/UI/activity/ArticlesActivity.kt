package com.tsu.mobilecourse.UI.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tsu.mobilecourse.databinding.ActivitySearchBinding

class ArticlesActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySearchBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}