package ipca.example.lojasocialipca.ui.screens.beneficiario


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Entrega
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

@Composable
fun RemarcarPedidoScreen(
    entrega: Entrega,
    onBack: () -> Unit = {}
) {
    val firestore = AppModule.firestore
    val context = LocalContext.current

    var novoDia by remember { mutableStateOf("") }
    var novoMes by remember { mutableStateOf("") }
    var novoAno by remember { mutableStateOf("") }

    val estadoTexto = when (entrega.estadoEntrega) {
        "PENDENTE" -> "Pendente"
        "POR_ENTREGAR" -> "Por Entregar"
        "POR_REMARCAR" -> "Por Remarcar"
        "ENTREGUE" -> "Entregue"
        else -> entrega.estadoEntrega
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(true, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Remarcar Pedido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            // CARD INFO PEDIDO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Pedido nº ${entrega.numEntrega}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Data atual de entrega: ${entrega.dataEntrega?.format() ?: "Não definida"}", fontSize = 14.sp)
                    Text("Estado: $estadoTexto", fontSize = 14.sp)
                }
            }

            Text(
                "Remarcar entrega",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                "Nova data de entrega:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = novoDia,
                    onValueChange = { input ->
                        novoDia = input.filter { it.isDigit() }
                            .take(2)
                            .let { if (it.toIntOrNull() in 1..31 || it.isEmpty()) it else novoDia }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Dia") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = novoMes,
                    onValueChange = { input ->
                        novoMes = input.filter { it.isDigit() }
                            .take(2)
                            .let { if (it.toIntOrNull() in 1..12 || it.isEmpty()) it else novoMes }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Mês") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = novoAno,
                    onValueChange = { input ->
                        novoAno = input.filter { it.isDigit() }.take(4)
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Ano") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        // Validação da nova data
                        val dia = novoDia.toIntOrNull()
                        val mes = novoMes.toIntOrNull()
                        val ano = novoAno.toIntOrNull()

                        if (dia == null || mes == null || ano == null) {
                            Toast.makeText(context, "Preencha a data corretamente", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val novaData = try {
                            criarData(ano = ano, mes = mes - 1, dia = dia)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Data inválida", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (!novaData.after(entrega.dataEntrega)) {
                            Toast.makeText(context, "A nova data deve ser posterior à data atual", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Atualizar entrega no Firestore
                        firestore.collection("entregas")
                            .whereEqualTo("numEntrega", entrega.numEntrega)
                            .whereEqualTo("destinatario", entrega.destinatario)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                if (!snapshot.isEmpty) {
                                    val docRef = snapshot.documents.first().reference
                                    docRef.update(
                                        mapOf(
                                            "dataRemarcacao" to novaData,
                                            "estadoEntrega" to "POR_REMARCAR"
                                        )
                                    ).addOnSuccessListener {
                                        Toast.makeText(context, "Entrega remarcada com sucesso!", Toast.LENGTH_SHORT).show()
                                        onBack()
                                    }.addOnFailureListener {
                                        Toast.makeText(context, "Erro ao atualizar entrega", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Entrega não encontrada", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Erro ao buscar entrega", Toast.LENGTH_SHORT).show()
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF037C49)),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Confirmar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9F0D00)),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Cancelar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true,)
@Composable
fun PreviewRemarcarPedido() {
    RemarcarPedidoScreen(
        entrega = Entrega(
            numEntrega = 12345,
            dataEntrega = Date(),
            estadoEntrega = "POR_ENTREGAR"
        )
    )
}
