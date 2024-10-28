package com.example.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import com.example.shapegenerator.Model.Points
import kotlin.math.cos
import kotlin.math.sin

class PolygonCanvas @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val points = mutableListOf<PointF>()
    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }
    private val linePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private var draggingPoint: PointF? = null
    private val touchRadius = 50f

    fun setDatabasePoints(dbPoints: List<Points>) {
        points.clear()

        // Asegura que el ancho y el alto de la vista están definidos antes de escalar
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val canvasWidth = width.toFloat()
                val canvasHeight = height.toFloat()

                dbPoints.forEach { dbPoint ->
                    val x = dbPoint.x * canvasWidth   // Escalar x de [0, 1] a [0, canvasWidth]
                    val y = dbPoint.y * canvasHeight  // Escalar y de [0, 1] a [0, canvasHeight]
                    points.add(PointF(x.toFloat(), y.toFloat()))
                }

                invalidate()  // Redibuja el canvas
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }


    fun setPolygonSides(sides: Int, radius: Float) {
        if (sides < 3) return //al menos sea un triángulo

        points.clear()

//aseguramos que el ancho y alto de la vista están definidos
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

                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
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
                Toast.makeText(context, "Click en :"+ draggingPoint, Toast.LENGTH_SHORT).show()
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
    fun savePointsToPreferences() {
        val prefs = context.getSharedPreferences("PolygonPrefs", Context.MODE_PRIVATE)
        val pointsString = points.joinToString(separator = ";") { "${it.x},${it.y}" }


        prefs.edit()
            .putString("polygon_points", pointsString)
            .apply()

        Toast.makeText(context, "Puntos guardados en preferencias", Toast.LENGTH_SHORT).show()
    }

}
