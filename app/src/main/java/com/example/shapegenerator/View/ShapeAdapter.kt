package com.example.shapegenerator.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shapegenerator.Model.Shape
import com.example.shapegenerator.R

class ShapeAdapter(private val shapes: List<Shape>) : RecyclerView.Adapter<ShapeAdapter.ShapeViewHolder>() {

    inner class ShapeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.itemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShapeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shape_button, parent, false)
        return ShapeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShapeViewHolder, position: Int) {
        // aca puedo llamar a dibujar cuando toque el boton
        val shape = shapes[position]
        holder.button.text = shape.name

        holder.button.setOnClickListener {

            Toast.makeText(holder.itemView.context, "Clicked: ${shape.name}", Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int = shapes.size
}
