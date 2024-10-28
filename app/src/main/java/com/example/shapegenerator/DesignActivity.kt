package com.example.shapegenerator
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
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

class DesignActivity : AppCompatActivity(), ShapeAdapter.OnShapeClickListener {

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

        if (isInternetAvailable()) {
            // Si hay conexión a internet, carga desde la API
            shapeViewModel.shapes.observe(this) { shapes ->
                shapes?.let {
                    // Guardar cada forma en la base de datos
                    for (shape in shapes) {
                        dbHelper.insertShape(shape)
                    }
                    Toast.makeText(this, "datos recuperados y guardados bd", Toast.LENGTH_SHORT).show()

                    // Actualiza el RecyclerView con los nuevos datos
                    shapeAdapter = ShapeAdapter(shapes.map { it.name }.toTypedArray(), this)
                    recyclerView.adapter = shapeAdapter
                }
            }
        } else if (shapeNamesArray.isNotEmpty()) {
            // Si no hay conexión y hay datos en la base de datos, usa esos datos
            shapeAdapter = ShapeAdapter(shapeNamesArray, this)
            recyclerView.adapter = shapeAdapter
            Toast.makeText(this, "sin conexión, datos cargados desde bd", Toast.LENGTH_SHORT).show()
        } else {
            // Si no hay conexión y tampoco hay datos en la base de datos, muestra un mensaje
            Toast.makeText(this, "No hay conexión a internet y no hay datos locales disponibles", Toast.LENGTH_SHORT).show()
        }

    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    override fun onShapeClick(shape: String) {
        //SelectionDialogFragment.newInstance().show(supportFragmentManager, "selectionDialog")
    }
}