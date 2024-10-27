package com.example.shapegenerator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CanvaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_canva)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val polygonCanvas = findViewById<PolygonCanvas>(R.id.polygonCanvas)

        var  escala: String = intent.getStringExtra("seekBarValue1").toString()
        var  lados: String = intent.getStringExtra("seekBarValue2").toString()

        //polygonCanvas.setSides(lados.toInt())
        //polygonCanvas.setScale(0.5f)



        Toast.makeText(this, "Lados: " + lados + " Escala: " + escala, Toast.LENGTH_SHORT).show()
    }
}