package com.example.shapegenerator

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
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {

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
        shapeViewModel.shapes.observe(this) { shapes ->
            // Aquí puedes actualizar la UI con la lista de figuras.
            // Por ejemplo, mostrarlas en un RecyclerView.
            for (shape in shapes) {
                println(shape)


            }
        }
    }
}