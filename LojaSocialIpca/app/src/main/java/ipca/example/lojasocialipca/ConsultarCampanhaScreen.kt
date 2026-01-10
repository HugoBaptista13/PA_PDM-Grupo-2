package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Campanha
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme
import java.util.Calendar

@Composable
fun ConsultarCampanhaScreen(
    campanhas: List<Campanha>,
    onFinalizarCampanha: (Campanha) -> Unit = {}
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


        // BOTÃO CRIAR CAMPANHA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006837)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Criar Campanha",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        // LISTA DE CAMPANHAS
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(campanhas) { campanha ->
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
    campanha: Campanha,
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
                text = campanha.dataInicio.format(),
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
                    .clip(RoundedCornerShape(8.dp))
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
                    Text("Finalizar", fontSize = 20.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarCampanhaScreenPreview() {
    val data1 = criarData(2025, Calendar.JANUARY, 12)
    val data2 = criarData(2025, Calendar.DECEMBER, 5)


    LojaSocialIpcaTheme {
        ConsultarCampanhaScreen(
            campanhas = listOf(
                Campanha(
                    nome = "Recolha de Roupa",
                    dataInicio = data1,
                    dataFim = data1,
                    descricao = "Recolha de roupas de inverno",
                    tipo = "Interna",
                    concluida = false,
                    responsavel = ""
                ),
                Campanha(
                    nome = "Recolha de Alimentos",
                    dataInicio = data2,
                    dataFim = data2,
                    descricao = "Recolha de bens alimentares",
                    tipo = "Interna",
                    concluida = true,
                    responsavel = ""
                ),
                Campanha(
                    nome = "Recolha de Alimentos",
                    dataInicio = data2,
                    dataFim = data2,
                    descricao = "Recolha de bens alimentares",
                    tipo = "Interna",
                    concluida = false,
                    responsavel = ""
                ),
                Campanha(
                    nome = "Recolha de Alimentos",
                    dataInicio = data2,
                    dataFim = data2,
                    descricao = "Recolha de bens alimentares",
                    tipo = "Interna",
                    concluida = false,
                    responsavel = ""
                ),
                Campanha(
                    nome = "Recolha de Alimentos",
                    dataInicio = data2,
                    dataFim = data2,
                    descricao = "Recolha de bens alimentares",
                    tipo = "Interna",
                    concluida = false,
                    responsavel = ""
                ),
                Campanha(
                    nome = "Recolha de Alimentos",
                    dataInicio = data2,
                    dataFim = data2,
                    descricao = "Recolha de bens alimentares",
                    tipo = "Interna",
                    concluida = false,
                    responsavel = ""
                )
            )
        )
    }
}

