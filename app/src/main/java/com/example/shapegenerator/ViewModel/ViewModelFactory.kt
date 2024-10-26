package com.example.shapegenerator.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shapegenerator.Repository.ShapeRepository

class ShapeViewModelFactory(private val repository: ShapeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShapeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShapeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}