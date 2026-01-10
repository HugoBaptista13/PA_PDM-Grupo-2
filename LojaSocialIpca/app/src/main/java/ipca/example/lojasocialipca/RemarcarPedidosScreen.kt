package ipca.example.lojasocialipca


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RemarcarPedidoScreen() {
    var novoDia by remember { mutableStateOf("") }
    var novoMes by remember { mutableStateOf("") }
    var novoAno by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF006837))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Loja Social",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notificações",
                    tint = Color.White
                )
                Text(
                    "IPCA",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // CONTEÚDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Remarcar Pedido",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // CARD INFO PEDIDO (adapta ao teu mockup)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Pedido nº 12345", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Nome do utente: João Silva", fontSize = 14.sp)
                    Text("Data atual de entrega: 10/01/2026", fontSize = 14.sp)
                    Text("Hora: 15:30", fontSize = 14.sp)
                    Text("Estado: Agendado", fontSize = 14.sp)
                }
            }

            Text(
                "Remarcar entrega",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                "Nova data de entrega:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = novoDia,
                    onValueChange = { novoDia = it },
                    label = { Text("Dia") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = novoMes,
                    onValueChange = { novoMes = it },
                    label = { Text("Mês") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = novoAno,
                    onValueChange = { novoAno = it },
                    label = { Text("Ano") },
                    modifier = Modifier.weight(1f)
                )
            }




            Spacer(Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { /* confirmar remarcação */ },
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
                    onClick = { /* cancelar / voltar */ },
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
    RemarcarPedidoScreen()
}
