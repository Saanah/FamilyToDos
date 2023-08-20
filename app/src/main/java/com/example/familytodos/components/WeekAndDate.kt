import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*


//Show weekday and date
@Composable
fun WeekdayAndDate() {
    var currentWeekday by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }


    //Calendar class, system local date and time
    val calendar = Calendar.getInstance()
    //Format weekday and date in English
    val weekdayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)  //'EEEE' for full name of the week 'Monday', 'Tuesday' ...
    val dateFormat = SimpleDateFormat("d 'of' MMMM", Locale.ENGLISH)

    //Get the current weekday and time and format them accordingly
    currentWeekday = weekdayFormat.format(calendar.time)
    currentDate = dateFormat.format(calendar.time)

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = currentWeekday,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = currentDate,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview
fun WeekdayAndDatePreview() {
    Surface {
        WeekdayAndDate()
    }
}
