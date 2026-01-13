package ipca.example.lojasocialipca.ui.screens.candidato

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import ipca.example.lojasocialipca.models.Candidatura
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme
import java.util.Calendar

@Composable
fun ConsultarCandidaturaScreen(
    candidaturas: List<Candidatura>,
    onFinalizarCandidatura: (Candidatura) -> Unit = {}
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

        // LISTA DE CANDIDATURAS
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(candidaturas) { candidatura ->
                CandidaturaCard(
                    candidatura = candidatura,
                    onFinalizar = { onFinalizarCandidatura(candidatura) }
                )
            }
        }
    }
}

@Composable
fun CandidaturaCard(
    candidatura: Candidatura,
    onFinalizar: () -> Unit,
) {
    val corFundo = if (candidatura.estadoCandidatura == "Concluída") Color(0xFF00B050) else Color(0xFFE0E0E0)
    val textoEstado = if (candidatura.estadoCandidatura == "Concluída") "Concluída" else "Em fila de espera"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(corFundo)
            .padding(16.dp)
    ) {
        // Cabeçalho com data e estado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Data submissão:",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = candidatura.dataSubmissao.format(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Estado da candidatura
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Estado Candidatura:",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = textoEstado,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (candidatura.estadoCandidatura == "Concluída") Color(0xFF006837) else Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Informações da candidatura
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Primeira linha: Nome e Nº aluno
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nome:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = candidatura.nome,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nº aluno:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = candidatura.numAluno.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Segunda linha: Ano letivo e Grau
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ano letivo:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = candidatura.anoLetivo, // Note: anoletivo em minúsculo
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Grau:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = candidatura.grau,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Curso
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Curso:",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = candidatura.curso, // Note: corso em vez de curso
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Informações adicionais (opcional)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "FAES:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = if (candidatura.faes) "Sim" else "Não",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Bolseiro:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = if (candidatura.bolseiro) "Sim" else "Não",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Valor da bolsa (se aplicável)
            if (candidatura.bolseiro && candidatura.valorBolsa != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Valor Bolsa:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${candidatura.valorBolsa} €",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarCandidaturaScreenPreview() {
    val dataSubmissao = criarData(2026, Calendar.JANUARY, 15)
    val dataNascimento = criarData(2000, Calendar.MAY, 20)

    LojaSocialIpcaTheme {
        ConsultarCandidaturaScreen(
            candidaturas = listOf(
                Candidatura(
                    nome = "Fabio Sandro",
                    numAluno = 1340,
                    dataSubmissao = dataSubmissao,
                    dataNascimento = dataNascimento,
                    anoLetivo = "2025/2026",
                    grau = "Licenciatura",
                    curso = "Design Gráfico",
                    estadoCandidatura = "Em fila de espera",
                    cartaoCidadao = "12345678",
                    telemovel = "912345678",
                    email = "fabio@example.com",
                    tipologiaPedido = listOf("Roupa", "Alimentação"),
                    faes = true,
                    bolseiro = false,
                    valorBolsa = null
                ),
            )
        )
    }
}