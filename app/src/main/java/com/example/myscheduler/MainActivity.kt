package com.example.myscheduler

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.myscheduler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val naviController = findNavController(R.id.nav_host_fragment_container) // 画面遷移を行うためのNavControllerを取得する
        //appBarConfiguration = AppBarConfiguration(naviController.graph)
        setupActionBarWithNavController(naviController)   // 画面上部に表示されるアクションバーに戻るボタンを追加する, ボタンを押すとonSupportNavigateUp()が実行される

        binding.fab.setOnClickListener { view ->
            naviController.navigate(R.id.action_to_scheduleEditFragment)
        }
    }

    override fun onSupportNavigateUp()
        = findNavController(R.id.nav_host_fragment_container).navigateUp()  // navigateUp: 前画面に画面遷移する

    fun setFabVisible(visibility: Int){  // fabボタンの表示/非表示を切り替えるための処理
        binding.fab.visibility = visibility
    }
}