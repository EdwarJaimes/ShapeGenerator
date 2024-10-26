package com.example.shapegenerator.Model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ShapesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla Shape
        val createShapeTableSQL = """
            CREATE TABLE $TABLE_SHAPE (
                $COLUMN_SHAPE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT
            )
        """
        db.execSQL(createShapeTableSQL)

        // Tabla Points
        val createPointsTableSQL = """
            CREATE TABLE $TABLE_POINTS (
                $COLUMN_POINT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SHAPE_ID INTEGER,
                $COLUMN_X REAL,
                $COLUMN_Y REAL,
                FOREIGN KEY($COLUMN_SHAPE_ID) REFERENCES $TABLE_SHAPE($COLUMN_SHAPE_ID) ON DELETE CASCADE
            )
        """
        db.execSQL(createPointsTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_POINTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SHAPE")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "shapes.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_SHAPE = "shape"
        const val TABLE_POINTS = "points"
        const val COLUMN_SHAPE_ID = "shape_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_POINT_ID = "point_id"
        const val COLUMN_X = "x"
        const val COLUMN_Y = "y"
    }
    fun insertShape(shape: Shape): Long {
        val db = writableDatabase
        val shapeValues = ContentValues().apply {
            put(COLUMN_NAME, shape.name)
        }
        val shapeId = db.insert(TABLE_SHAPE, null, shapeValues)

        for (point in shape.points) {
            val pointValues = ContentValues().apply {
                put(COLUMN_SHAPE_ID, shapeId)
                put(COLUMN_X, point.x)
                put(COLUMN_Y, point.y)
            }
            db.insert(TABLE_POINTS, null, pointValues)
        }

        return shapeId
    }
    fun getShapeNames(): List<String> {
        val db = readableDatabase
        val shapeNames = mutableListOf<String>()
        val query = "SELECT $COLUMN_NAME FROM $TABLE_SHAPE"

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                shapeNames.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return shapeNames
    }
}
