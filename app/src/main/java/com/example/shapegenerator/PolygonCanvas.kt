package com.example.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class PolygonCanvas @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val points = mutableListOf<PointF>()
    private val pointPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }
    private val linePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private var draggingPoint: PointF? = null
    private val touchRadius = 50f

    // Método para configurar los puntos desde canvaActivity
   fun setPolygonSides(sides: Int, radius: Float) {
        if (sides < 3) return // Asegura que al menos sea un triángulo

        points.clear()

        // Aseguramos que el ancho y alto de la vista están definidos
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Calculamos el centro del canvas
                val centerX = width / 2f
                val centerY = height / 2f

                // Calcular los puntos en el polígono
                for (i in 0 until sides) {
                    val angle = 2 * Math.PI * i / sides  // Ángulo en radianes
                    val x = (centerX + radius * cos(angle)).toFloat()
                    val y = (centerY + radius * sin(angle)).toFloat()
                    points.add(PointF(x, y))
                }

                invalidate()  // Redibuja el canvas

                // Removemos el listener para evitar múltiples llamadas
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    /*private fun centerPolygon() {
        if (points.isEmpty()) return

        // 1. Calcular el centro del polígono
        val polygonCenter = calculatePolygonCenter()

        // 2. Calcular el centro del lienzo
        val canvasCenter = PointF(width / 2f, height / 2f)

        // 3. Desplazamiento necesario para centrar el polígono
        val offsetX = canvasCenter.x - polygonCenter.x
        val offsetY = canvasCenter.y - polygonCenter.y

        // 4. Trasladar todos los puntos
        points.forEach { point ->
            point.x += offsetX
            point.y += offsetY
        }
    }*/

    private fun calculatePolygonCenter(): PointF {
        val centerX = points.map { it.x }.average().toFloat()
        val centerY = points.map { it.y }.average().toFloat()
        return PointF(centerX, centerY)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//dibujar líneas
        if (points.size > 1) {
            for (i in points.indices) {
                val start = points[i]
                val end = points[(i + 1) % points.size]  // Conectar al siguiente punto y cerrar el polígono
                canvas.drawLine(start.x, start.y, end.x, end.y, linePaint)
            }
        }

//dibujar los puntos
        points.forEach { point ->
            canvas.drawCircle(point.x, point.y, 10f, pointPaint)
        }
    }
//gestiona los gestos sobre los puntos
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                draggingPoint = getTouchedPoint(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                draggingPoint?.let {
                    it.x = event.x
                    it.y = event.y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                draggingPoint = null
            }
        }
        return true
    }

    private fun getTouchedPoint(x: Float, y: Float): PointF? {
        // Detectar cuál punto está cerca del toque
        return points.find { isNearPoint(x, y, it) }
    }

    private fun isNearPoint(x: Float, y: Float, point: PointF): Boolean {
        //radio de proximidad
        val dx = x - point.x
        val dy = y - point.y
        return dx * dx + dy * dy <= touchRadius * touchRadius
    }
}
