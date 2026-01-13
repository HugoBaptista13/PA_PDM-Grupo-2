package ipca.example.lojasocialipca.ui.screens.beneficiario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PedidoUi(
    val id: Int,
    val data: String,
    val linhas: List<String>,
    val entregue: Boolean,
    val remarcado: Boolean = false
)

@Composable
fun ConsultarPedidoScreen(
    pedidos: List<PedidoUi>,
    onRealizaEntrega: (PedidoUi) -> Unit = {},
    onRealizaRemarcar: (PedidoUi) -> Unit = {}
) {
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
            Text(
                text = "Loja Social",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "IPCA",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // LISTA DE PEDIDOS
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            pedidos.forEach { pedido ->
                PedidoCard(
                    pedido = pedido,
                    onEntregar = { onRealizaEntrega(pedido) },
                    onRemarcar = { onRealizaRemarcar(pedido) }
                )
            }
        }
    }
}

@Composable
fun PedidoCard(
    pedido: PedidoUi,
    onEntregar: () -> Unit,
    onRemarcar: () -> Unit
) {
    val corFundo = if (pedido.entregue) Color(0xFF00B050)
    else if (pedido.remarcado) Color(0xFFFFC000)  // amarelo
    else Color(0xFFE0E0E0)

    val textoEstado = if (pedido.entregue) "Entregue"
    else if (pedido.remarcado) "Por remarcar"
    else "Por entregar"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(corFundo)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = pedido.data,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = textoEstado,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                pedido.linhas.forEach { linha ->
                    Text(
                        text = linha,
                        fontSize = 14.sp
                    )
                }
            }

            if (!pedido.entregue) {

                if (!pedido.remarcado) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onRemarcar,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Remarcar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarPedidoScreenPreview() {
    MaterialTheme {
        ConsultarPedidoScreen(
            pedidos = listOf(
                PedidoUi(
                    id = 1,
                    data = "12/01/2026",
                    linhas = listOf(
                        "Lata de Feijão Vermelho Cozido x 4",
                        "Lata de Cavala em óleo x 6",
                        "Massa Fettuccine 1 Kg x 2"
                    ),
                    entregue = false,
                    remarcado = false  // Por entregar → botão "Remarcar"
                ),
                PedidoUi(
                    id = 2,
                    data = "15/01/2026",
                    linhas = listOf(
                        "Cobertores x 3",
                        "Casacos de Inverno x 2"
                    ),
                    entregue = false,
                    remarcado = true   // Por remarcar → botão "Entregar"
                ),
                PedidoUi(
                    id = 3,
                    data = "05/12/2025",
                    linhas = listOf(
                        "Arroz x 5",
                        "massa x 5",
                        "Arroz x 5",
                    ),
                    entregue = true    // Entregue → sem botão
                )
            )
        )
    }
}