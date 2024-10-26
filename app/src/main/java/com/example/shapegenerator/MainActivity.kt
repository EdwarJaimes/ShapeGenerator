package com.example.shapegenerator

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shapegenerator.Repository.RetrofitInstance
import com.example.shapegenerator.Repository.ShapeRepository
import com.example.shapegenerator.ViewModel.ShapeViewModel
import com.example.shapegenerator.ViewModel.ShapeViewModelFactory
import com.example.shapegenerator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val shapeViewModel: ShapeViewModel by viewModels {
        ShapeViewModelFactory(ShapeRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelection.setOnClickListener {
            val newIntent = Intent(this, DesignActivity::class.java)
            startActivity(newIntent)
        }





    }
}
