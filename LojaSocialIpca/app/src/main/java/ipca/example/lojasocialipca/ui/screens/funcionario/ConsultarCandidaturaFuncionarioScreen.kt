package ipca.example.lojasocialipca.ui

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
import ipca.example.lojasocialipca.models.Candidatura
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConsultarCandidaturaFuncionarioScreen(
    candidaturas: List<Candidatura>,
    onAvaliar: (Candidatura) -> Unit = {}
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

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            candidaturas.forEach { candidatura ->
                CandidaturaFuncionarioCard(
                    candidatura = candidatura,
                    onAvaliar = { onAvaliar(candidatura) }
                )
            }
        }
    }
}

@Composable
fun CandidaturaFuncionarioCard(
    candidatura: Candidatura,
    onAvaliar: () -> Unit
) {
    val estadoLower = candidatura.estadoCandidatura.lowercase()

    val corFundo = when {
        estadoLower.contains("aprovado") || estadoLower.contains("aprovada") -> Color(0xFF00B050)
        estadoLower.contains("reprovado") || estadoLower.contains("reprovada") -> Color(0xFFFF4C4C)
        else -> Color(0xFFE0E0E0)
    }

    val textoEstado = when {
        estadoLower.contains("aprovado") || estadoLower.contains("aprovada") -> "Aprovado"
        estadoLower.contains("reprovado") || estadoLower.contains("reprovada") -> "Reprovada"
        else -> "Em fila\nde espera"
    }

    val dataFormatada = formatDataSubmissao(candidatura.dataSubmissao)
    val mostrarBotao = !estadoLower.contains("aprovado") &&
            !estadoLower.contains("reprovada") &&
            !estadoLower.contains("reprovado")

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
                text = dataFormatada,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = textoEstado,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                Text("Nome: ${candidatura.nome}", fontSize = 16.sp)
                Text("Nºaluno: ${candidatura.numAluno}", fontSize = 16.sp)
                Text("Ano Letivo: ${candidatura.anoLetivo}", fontSize = 16.sp)
                Text("Curso: ${candidatura.curso}", fontSize = 16.sp)
            }

            if (mostrarBotao) {
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onAvaliar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006837),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Avaliar", fontSize = 18.sp)
                }
            }
        }
    }
}



// UTILITÁRIOS
private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

private fun formatDataSubmissao(data: Date): String {
    return try {
        dateFormatter.format(data)
    } catch (e: Exception) {
        "Data inválida"
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarCandidaturaFuncionarioScreenPreview() {
    MaterialTheme {
        ConsultarCandidaturaFuncionarioScreen(
            candidaturas = listOf(
                Candidatura(
                    anoLetivo = "2025/2026",
                    nome = "Donald J. Trump",
                    cartaoCidadao = "00000000",
                    dataNascimento = Date(),
                    telemovel = "000000000",
                    email = "email@email.com",
                    grau = "1",
                    curso = "Contabilidade",
                    numAluno = 22340,
                    tipologiaPedido = listOf(""),
                    faes = false,
                    bolseiro = false,
                    valorBolsa = null,
                    dataSubmissao = Date(),
                    estadoCandidatura = "Aprovado"
                ),
                Candidatura(
                    anoLetivo = "2025/2026",
                    nome = "Fabio Sandro",
                    cartaoCidadao = "00000000",
                    dataNascimento = Date(),
                    telemovel = "000000000",
                    email = "email@email.com",
                    grau = "1",
                    curso = "Design Grafico",
                    numAluno = 1340,
                    tipologiaPedido = listOf(""),
                    faes = false,
                    bolseiro = false,
                    valorBolsa = null,
                    dataSubmissao = Date(),
                    estadoCandidatura = "Em fila de espera"
                ),
                Candidatura(
                    anoLetivo = "2025/2026",
                    nome = "Janiqua Feliz",
                    cartaoCidadao = "00000000",
                    dataNascimento = Date(),
                    telemovel = "000000000",
                    email = "email@email.com",
                    grau = "1",
                    curso = "Solicitadoria",
                    numAluno = 28333,
                    tipologiaPedido = listOf(""),
                    faes = false,
                    bolseiro = false,
                    valorBolsa = null,
                    dataSubmissao = Date(),
                    estadoCandidatura = "Reprovada"
                )
            )
        ) { }
    }
}


