package ec.edu.epn.pruebapractica

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "Eventos_Noviembre.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "eventos"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "nombre"
        const val COLUMN_ADDRESS = "direccion"
        const val COLUMN_DATE = "fecha"
        const val COLUMN_ATTENDEES = "asistentes"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
        CREATE TABLE $TABLE_NAME (
        $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_NAME TEXT NOT NULL,
        $COLUMN_ADDRESS TEXT NOT NULL,
        $COLUMN_DATE TEXT NOT NULL,
        $COLUMN_ATTENDEES TEXT NOT NULL
        )
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertPuntoInteres(nombre: String, direccion: String, fecha: String, asistentes: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, nombre)
            put(COLUMN_ADDRESS, direccion)
            put(COLUMN_DATE, fecha)
            put(COLUMN_ATTENDEES, asistentes)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllPuntosInteres(): Cursor {
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_NAME, COLUMN_ADDRESS, COLUMN_DATE, COLUMN_ATTENDEES)
        return db.query(TABLE_NAME, null, null, null, null, null, null)
    }


}