package ipca.example.lojasocialipca.ui.screens.funcionario

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Candidatura
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

@Composable
fun ConsultarCandidaturaFuncionarioScreen(
    onAvaliar: (Candidatura) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var candidaturas by remember { mutableStateOf<List<Candidatura>>(emptyList()) }

    // Buscar candidaturas do Firestore em tempo real
    LaunchedEffect(Unit) {
        val collectionRef = AppModule.firestore.collection("candidaturas")

        collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("Firestore", "Erro ao buscar candidaturas", error)
                Toast.makeText(context, "Erro ao buscar candidaturas", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val lista = snapshot.documents.mapNotNull { doc ->
                    try {
                        Candidatura(
                            idCandidatura = doc.id,
                            nome = doc.getString("nome") ?: "",
                            numAluno = doc.getLong("numAluno")?.toInt() ?: 0,
                            anoLetivo = doc.getString("anoLetivo") ?: "",
                            curso = doc.getString("curso") ?: "",
                            cartaoCidadao = doc.getString("cartaoCidadao") ?: "",
                            dataNascimento = doc.getTimestamp("dataNascimento")?.toDate() ?: Date(),
                            telemovel = doc.getString("telemovel") ?: "",
                            email = doc.getString("email") ?: "",
                            grau = doc.getString("grau") ?: "",
                            tipologiaPedido = doc.get("tipologiaPedido") as? List<String> ?: emptyList(),
                            faes = doc.getBoolean("faes") ?: false,
                            bolseiro = doc.getBoolean("bolseiro") ?: false,
                            valorBolsa = doc.getDouble("valorBolsa"),
                            dataSubmissao = doc.getTimestamp("dataSubmissao")?.toDate() ?: Date(),
                            estadoCandidatura = doc.getString("estadoCandidatura") ?: "FILA_ESPERA"
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                // Define prioridade dos estados
                val prioridadeEstado = mapOf(
                    "FILA_ESPERA" to 0,
                    "APROVADA" to 1,
                    "REPROVADA" to 2
                )

                // Ordena por estado e data de submissão
                candidaturas = lista.sortedWith(
                    compareBy<Candidatura> { prioridadeEstado[it.estadoCandidatura] ?: 3 }
                        .thenByDescending { it.dataSubmissao }
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR
        TopBar(true, onBack)

        // LISTA DE CANDIDATURAS
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(candidaturas) { candidatura ->
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
    val (corFundo, textoEstado) = when (candidatura.estadoCandidatura) {
        "APROVADA" -> Color(0xFF00B050) to "Aprovada"
        "REPROVADA" -> Color(0xFFFF4C4C) to "Reprovada"
        "FILA_ESPERA" -> Color(0xFFE0E0E0) to "Em fila\nde espera"
        else -> Color(0xFFE0E0E0) to "Desconhecido"
    }

    val dataFormatada = candidatura.dataSubmissao.format()
    val mostrarBotao = candidatura.estadoCandidatura == "FILA_ESPERA"

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
                Text("Nº aluno: ${candidatura.numAluno}", fontSize = 16.sp)
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarCandidaturaFuncionarioScreenPreview() {
    MaterialTheme {
        ConsultarCandidaturaFuncionarioScreen()
    }
}


