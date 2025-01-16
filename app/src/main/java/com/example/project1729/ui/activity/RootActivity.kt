package com.example.project1729.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.project1729.R
import com.example.project1729.databinding.ActivityRootBinding


class RootActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRootBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController


        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.startFragment, R.id.loginFragment, R.id.registerFragment, R.id.dopInfoFragment, R.id.menuFragment-> {
//                    bottomNavigationView.visibility = View.GONE
//                }
//                else -> {
//                    bottomNavigationView.visibility = View.VISIBLE
//                }
//            }
//        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.resultsFragment, R.id.checkFragment, R.id.settingsFragment-> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }

}