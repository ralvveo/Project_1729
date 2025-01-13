package com.example.project1729.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.project1729.R
import com.example.project1729.databinding.ActivityRootBinding
import com.example.project1729.ui.view_model.RootViewModel

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private lateinit var viewModel: RootViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, RootViewModel.factory())[RootViewModel::class.java]
        viewModel.initializeTheme()

        supportFragmentManager.beginTransaction()
            .add(R.id.rootFragmentContainerView, MainFragment())
            .commit()
    }
}