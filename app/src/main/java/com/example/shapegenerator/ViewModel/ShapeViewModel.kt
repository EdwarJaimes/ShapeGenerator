package com.example.shapegenerator.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.shapegenerator.Model.Shape
import com.example.shapegenerator.Repository.ShapeRepository
import com.example.shapegenerator.View.ShapeAdapter
import kotlinx.coroutines.Dispatchers

class ShapeViewModel(private val repository: ShapeRepository): ViewModel(), ShapeAdapter.OnShapeClickListener {
    val shapes = liveData(Dispatchers.IO) {
        try {
            val shapeList = repository.fetchShapes()
            emit(shapeList)
        } catch (e: Exception){
            emit(emptyList<Shape>())
        }
    }
    override fun onShapeClick(shape: String) {
        //SelectionDialogFragment.newInstance().show(supportFragmentManager, "selectionDialog")
    }
}