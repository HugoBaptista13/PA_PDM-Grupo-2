package ipca.example.lojasocialipca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import ipca.example.lojasocialipca.models.Candidatura
import java.text.SimpleDateFormat
import java.util.*
import ipca.example.lojasocialipca.ui.components.TopBar

@Composable
fun AvaliarCandidaturaScreen(
    candidatura: Candidatura,
    onAprovar: () -> Unit = {},
    onReprovar: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var mostrarMotivo by remember { mutableStateOf(false) }
    var motivo by remember { mutableStateOf("") }
    var resultadoSelecionado by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR
        TopBar(onBack)
        

        // CONTEÚDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // DADOS DA CANDIDATURA
            val dataFormatada = formatDataSubmissao(candidatura.dataSubmissao)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Data submissão: $dataFormatada",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Nome: ${candidatura.nome}")
                Text("Nº aluno: ${candidatura.numAluno}")
                Text("Ano letivo: ${candidatura.anoLetivo}")
                Text("Curso: ${candidatura.curso}")
                Text("Estado candidatura: Em fila de espera")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÕES APROVAR / REPROVAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        resultadoSelecionado = "APROVADO"
                        mostrarMotivo = false
                        motivo = ""
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B050)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Aprovar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        resultadoSelecionado = "REPROVADO"
                        mostrarMotivo = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4C4C)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reprovar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // MOTIVO DE REPROVAÇÃO
            if (mostrarMotivo) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Se reprovar, indique o motivo:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = motivo,
                        onValueChange = { motivo = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        label = { Text("Motivo da reprovação") },
                        maxLines = 4
                    )
                }
            }

            // SUBMETER RESULTADO
            if (
                resultadoSelecionado == "APROVADO" ||
                (resultadoSelecionado == "REPROVADO" && motivo.isNotBlank())
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (resultadoSelecionado == "APROVADO") {
                            onAprovar()
                        } else {
                            onReprovar(motivo)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "Submeter resultado",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// UTILITÁRIOS
private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

private fun formatDataSubmissao(data: Date): String =
    try {
        dateFormatter.format(data)
    } catch (e: Exception) {
        "Data inválida"
    }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AvaliarCandidaturaScreenPreview() {
    MaterialTheme {
        AvaliarCandidaturaScreen(
            candidatura = Candidatura(
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
            )
        )
    }
}

