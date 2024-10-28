package com.example.shapegenerator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recycler.PolygonCanvas
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
        //enviar datos de ceracion de poligono.
        polygonCanvas.setPolygonSides(lados, scale)
    }
}