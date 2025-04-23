package com.example.project1729.ui.activity

import android.app.Application
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
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.navigation_graph)
        val bottomNavigationView = binding.bottomNavigationView
        val sharedPrefs = getSharedPreferences(PROJECT1729_PREFERENCES, Application.MODE_PRIVATE)
        val userName = sharedPrefs.getString(USERNAME, NOT_AUTHORIZED)

        if (userName == NOT_AUTHORIZED){
            graph.setStartDestination(R.id.startFragment)
        }
        else{
            graph.setStartDestination(R.id.menuFragment)
        }

        navController.graph = graph
        bottomNavigationView.setupWithNavController(navController)

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

    companion object{
        const val PROJECT1729_PREFERENCES = "PROJECT1729_PREFERENCES"
        const val USERNAME = "USERNAME"
        const val NOT_AUTHORIZED = "NOT_AUTHORIZED"
    }

}