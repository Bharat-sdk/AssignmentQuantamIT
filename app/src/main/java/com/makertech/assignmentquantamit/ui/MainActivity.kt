package com.makertech.assignmentquantamit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.makertech.assignmentquantamit.R
import com.makertech.assignmentquantamit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}