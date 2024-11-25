package ec.edu.epn.pruebapractica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.epn.pruebapractica.ui.theme.PruebaPracticaTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseHelper = DatabaseHelper(this)
        insertarDatosIniciales() // Llamada para inicializar datos en SQLite

        enableEdgeToEdge()
        setContent {
            PruebaPracticaTheme {
                val events = remember { mutableStateOf(loadEventsFromDatabase()) }
                EventGrid(events = events.value)
            }
        }
    }

    private fun insertarDatosIniciales() {
        val cursor = databaseHelper.getAllPuntosInteres()
        if (cursor.count == 0) {
            databaseHelper.insertPuntoInteres(
                "Trail en el Mirador de la Perdiz",
                "Mirador de la Perdiz",
                "31/10/2024",
                "30"
            )
            databaseHelper.insertPuntoInteres(
                "Concurso de la mejor colada morada",
                "Estadio de San José",
                "31/10/2024",
                "100"
            )
            databaseHelper.insertPuntoInteres(
                "Conmemoración día de los difuntos",
                "Cementerio de San José",
                "02/11/2024",
                "30"
            )
            databaseHelper.insertPuntoInteres(
                "Concurso de guagua de pan",
                "Estadio de San José",
                "03/11/2024",
                "100"
            )
        }
        cursor.close()
    }

    private fun loadEventsFromDatabase(): List<Event> {
        val cursor = databaseHelper.getAllPuntosInteres()
        val events = mutableListOf<Event>()

        // Lista de imágenes en el orden deseado
        val imageResources = listOf(
            R.drawable.chimborazo,
            R.drawable.coladamorada,
            R.drawable.cruz,
            R.drawable.guaguadepan
        )

        var index = 0 // Índice para recorrer las imágenes

        while (cursor.moveToNext()) {
            events.add(
                Event(
                    title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                    location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)),
                    icon = imageResources[index % imageResources.size], // Asignar la imagen en orden
                    attendees = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ATTENDEES)).toInt()
                )
            )
            index++
        }
        cursor.close()
        return events
    }
}

val imageResources = listOf(
    R.drawable.chimborazo,
    R.drawable.coladamorada,
    R.drawable.cruz,
    R.drawable.guaguadepan
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventGrid(events: List<Event>, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Eventos en San José por el feriado de Noviembre") })
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = paddingValues
        ) {
            items(events) { event ->
                EventCard(event)
            }
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = event.icon),
                contentDescription = event.title,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
            Text(text = event.location, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: ${event.date}", style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Asistentes: ${event.attendees}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

data class Event(
    val title: String,
    val location: String,
    val date: String,
    val icon: Int,
    val attendees: Int
)

/*
val events = listOf(
    Event(
        title = "Trail en el Mirador de la Perdiz",
        location = "Mirador de la Perdiz",
        date = "31/10/2024",
        icon = R.drawable.chimborazo,
        attendees = 30
    ),
    Event(
        title = "Concurso de la 'Mejor colada morada'",
        location = "Estadio de San José",
        date = "31/10/2024",
        icon = R.drawable.coladamorada,
        attendees = 100
    ),
    Event(
        title = "Conmemoración Día de los Difuntos",
        location = "Cementerio de San José",
        date = "02/11/2024",
        icon = R.drawable.cruz,
        attendees = 30
    ),
    Event(
        title = "Concurso de guagua de pan",
        location = "Estadio de San José",
        date = "03/11/2024",
        icon = R.drawable.guaguadepan,
        attendees = 100
    )
)
*/
