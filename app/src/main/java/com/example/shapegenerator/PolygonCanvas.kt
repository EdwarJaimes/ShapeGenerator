package com.example.shapegenerator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.io.path.Path
import kotlin.math.cos
import kotlin.math.sin

class PolygonCanvas @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint().apply {
        color = 0xFF6200EE.toInt() // Color del polígono
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val path = android.graphics.Path()
    private var scale = 1.0f
    private var sides = 4

    // Método para configurar el número de lados
    fun setSides(newSides: Int) {
        sides = newSides
        invalidate()
    }

    // Método para ajustar la escala
    fun setScale(newScale: Float) {
        scale = newScale
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (sides < 3) return

        // Tamaño y centro del polígono
        val radius = width.coerceAtMost(height) / 2 * scale
        val centerX = width / 2f
        val centerY = height / 2f

        // Crear el camino del polígono
        path.reset()
        for (i in 0 until sides) {
            val angle = 2 * Math.PI * i / sides
            val x = (centerX + radius * cos(angle)).toFloat()
            val y = (centerY + radius * sin(angle)).toFloat()

            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()

        // Dibujar el polígono
        canvas.drawPath(path, paint)
    }
}