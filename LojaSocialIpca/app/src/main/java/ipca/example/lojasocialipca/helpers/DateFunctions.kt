package ipca.example.lojasocialipca.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun criarData(ano: Int, mes: Int, dia: Int): Date =
    Calendar.getInstance().apply {
        set(ano, mes, dia, 0, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

fun Date.format(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)

fun Date.formatId(): String =
    SimpleDateFormat("ddMMyyyyHHmmssSS", Locale.getDefault()).format(this)