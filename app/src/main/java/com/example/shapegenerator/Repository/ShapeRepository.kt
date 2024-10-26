package com.example.shapegenerator.Repository

import com.example.shapegenerator.Model.Shape

class ShapeRepository(private val api: ShapeApiService) {
    suspend fun fetchShapes(): List<Shape> {
        return api.getShapes()
    }
}
