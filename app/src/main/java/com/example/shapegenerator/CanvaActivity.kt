package com.example.shapegenerator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recycler.PolygonCanvas
import com.example.shapegenerator.Model.ShapesDatabaseHelper
import com.example.shapegenerator.databinding.ActivityCanvaBinding

class CanvaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCanvaBinding
    lateinit var polygonCanvas: PolygonCanvas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_canva)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        polygonCanvas = findViewById<PolygonCanvas>(R.id.polygonCanvas)
        binding = ActivityCanvaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveShape.setOnClickListener{
            polygonCanvas.savePointsToPreferences()
        }
        binding.btnLoadShape.setOnClickListener{
        }

        val polygonCanvas = findViewById<PolygonCanvas>(R.id.polygonCanvas)

        var scale: Float = intent.getIntExtra("seekBarValue1", 200).toFloat()
        var lados: Int = intent.getIntExtra("seekBarValue2", 3)
        var id: Int = intent.getIntExtra("id", 0)

        if( id == 1){
            //enviar datos de creacion de poligono.
            polygonCanvas.setPolygonSides(lados, scale)
        } else {
            val dbHelper = ShapesDatabaseHelper(this)

            var shapeId: String? = intent.getStringExtra("shapeId").toString()

            if (shapeId != null) {
                // Obtén los puntos asociados a la figura usando el ID
                val points = dbHelper.getPointsByShapeId(shapeId.toInt())

                if (points.isNotEmpty()) {
                    // Aquí puedes mostrar los puntos como prefieras, por ejemplo, en un Toast
                    polygonCanvas.setDatabasePoints(points)

                    val pointsString = points.joinToString(", ") { "(${it.x}, ${it.y})" }
                    //Toast.makeText(this, "Puntos de $shape: $pointsString", Toast.LENGTH_SHORT).show()

                } else {
                    //Toast.makeText(this, "No se encontraron puntos para $shape", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se encontró la figura $shapeId en la base de datos", Toast.LENGTH_SHORT).show()
            }


            //polygonCanvas.setDatabasePoints(points)

        }


    }
}