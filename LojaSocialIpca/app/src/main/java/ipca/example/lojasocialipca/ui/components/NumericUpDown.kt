package ipca.example.lojasocialipca.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun NumericUpDown(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { if (value > 1) onValueChange(value - 1) }) {
            Icon(Icons.Default.Remove, contentDescription = null)
        }
        Text(value.toString(), fontSize = 16.sp)
        IconButton(onClick = { onValueChange(value + 1) }) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NumericUpDownPreview(){
    NumericUpDown(
        value = 1
    ) {}
}