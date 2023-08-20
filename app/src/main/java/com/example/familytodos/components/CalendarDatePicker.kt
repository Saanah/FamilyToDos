import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.familytodos.ui.theme.spacing
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun CalendarDatePicker(
    onDateSelected : (Timestamp) -> Unit
){

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDateText by remember { mutableStateOf("") }

    //Get the current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]


    //Datepickerdialog, takes in current context, lambda function and date
    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int -> //_: is a placeholder for Datepicker, as it's not used in the lambda
            val selectedDate = SimpleDateFormat("dd/MM/yyyy").parse("$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear")
            val timestamp = Timestamp(selectedDate)
            onDateSelected(timestamp)
            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        },
        year, month, dayOfMonth
    )

    datePicker.datePicker.minDate = calendar.timeInMillis //Disable days that are in the past

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (selectedDateText.isNotEmpty()) {
                "Selected date is $selectedDateText"
            } else {
                "Please pick a date"
            },
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium),
            onClick = {
                datePicker.show()
            }
        ) {
            Text(text = "Select a date")
        }
    }

}