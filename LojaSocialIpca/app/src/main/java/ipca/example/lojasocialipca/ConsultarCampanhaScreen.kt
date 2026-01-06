package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CampanhaUi(
    val id: Int,
    val data: String,
    val nome: String,
    val tipo: String,
    val descricao: String,
    val concluida: Boolean
)

@Composable
fun ConsultarCampanhaScreen(
    campanhas: List<CampanhaUi>,
    onFinalizarCampanha: (CampanhaUi) -> Unit = {}
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

        // LISTA DE CAMPANHAS
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            campanhas.forEach { campanha ->
                CampanhaCard(
                    campanha = campanha,
                    onFinalizar = { onFinalizarCampanha(campanha) }
                )
            }
        }
    }
}

@Composable
fun CampanhaCard(
    campanha: CampanhaUi,
    onFinalizar: () -> Unit
) {
    val corFundo = if (campanha.concluida) Color(0xFF00B050) else Color(0xFFE0E0E0)
    val textoEstado = if (campanha.concluida) "Concluída" else "Em Curso"

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
                text = campanha.data,
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
                Text("Nome: ${campanha.nome}", fontSize = 14.sp)
                Text("Tipo: ${campanha.tipo}", fontSize = 14.sp)
                Text("Descrição: ${campanha.descricao}", fontSize = 14.sp)
            }

            if (!campanha.concluida) {
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onFinalizar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006837)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Finalizar")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarCampanhaScreenPreview() {
    MaterialTheme {
        ConsultarCampanhaScreen(
            campanhas = listOf(
                CampanhaUi(
                    id = 1,
                    data = "12/01/2026",
                    nome = "Recolha de Roupa",
                    tipo = "Doação",
                    descricao = "Roupa de inverno",
                    concluida = false
                ),
                CampanhaUi(
                    id = 2,
                    data = "05/12/2025",
                    nome = "Recolha de Alimentos",
                    tipo = "Solidária",
                    descricao = "Bens alimentares",
                    concluida = true
                )
            )
        )
    }
}

