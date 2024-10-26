package com.example.shapegenerator
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shapegenerator.Repository.RetrofitInstance
import com.example.shapegenerator.Repository.ShapeRepository
import com.example.shapegenerator.View.ShapeAdapter
import com.example.shapegenerator.ViewModel.ShapeViewModel
import com.example.shapegenerator.ViewModel.ShapeViewModelFactory

class DesignActivity : AppCompatActivity() {


    private lateinit var shapeAdapter: ShapeAdapter

    private val shapeViewModel: ShapeViewModel by viewModels {
        ShapeViewModelFactory(ShapeRepository(RetrofitInstance.api))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_design)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicializa el ViewModel
        //shapeViewModel = ViewModelProvider(this).get(ShapeViewModel::class.java)


        // Configura el RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observa los datos en el ViewModel
        shapeViewModel.shapes.observe(this) { shapes ->
            shapeAdapter = ShapeAdapter(shapes)
            recyclerView.adapter = shapeAdapter
        }
    }
}