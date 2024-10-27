package com.example.shapegenerator
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shapegenerator.Model.ShapesDatabaseHelper
import com.example.shapegenerator.Repository.RetrofitInstance
import com.example.shapegenerator.Repository.ShapeRepository
import com.example.shapegenerator.View.SelectionDialogFragment
import com.example.shapegenerator.View.ShapeAdapter
import com.example.shapegenerator.ViewModel.ShapeViewModel
import com.example.shapegenerator.ViewModel.ShapeViewModelFactory
import com.example.shapegenerator.databinding.ActivityDesignBinding

class DesignActivity : AppCompatActivity() {

    private lateinit var shapeAdapter: ShapeAdapter
    private  lateinit var binding: ActivityDesignBinding

    private val shapeViewModel: ShapeViewModel by viewModels {
        ShapeViewModelFactory(ShapeRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_design)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityDesignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDesign.setOnClickListener {
            SelectionDialogFragment.newInstance().show(supportFragmentManager, "selectionDialog")
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = ShapesDatabaseHelper(this)
        val shapeNamesArray = dbHelper.getShapeNames().toTypedArray()

//si esta vacio o la app borra los datos, se hace la consulta a la api
        if (shapeNamesArray.isEmpty()) {
            // Observa los datos en el ViewModel
            shapeViewModel.shapes.observe(this) { shapes ->
                for (shape in shapes) {
                    dbHelper.insertShape(shape)
                    println("Guardado en la base de datos: $shape")
                }
            }
//si no esta vacio, se muestran los datos de la base de datos
        } else {
            shapeViewModel.shapes.observe(this) { shapes ->
                shapeAdapter = ShapeAdapter(shapes)
                recyclerView.adapter = shapeAdapter
            }
        }
    }
}