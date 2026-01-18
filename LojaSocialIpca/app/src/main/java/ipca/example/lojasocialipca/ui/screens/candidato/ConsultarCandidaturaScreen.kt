package ipca.example.lojasocialipca.ui.screens.candidato

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import com.google.firebase.firestore.FieldPath
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Candidatura
import ipca.example.lojasocialipca.ui.components.TopBar
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme
import java.util.Calendar
import java.util.Date

@Composable
fun ConsultarCandidaturaScreen(
    onBack: () -> Unit = {}
) {
    val firestore = AppModule.firestore
    val auth = AppModule.auth
    val context = LocalContext.current

    var candidaturas by remember { mutableStateOf<List<Candidatura>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Buscar candidaturas do utilizador logado
    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("beneficiarios")
                .whereEqualTo("email", auth.currentUser?.email)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty) {
                        val beneficiarioDoc = snapshot.documents.first()
                        val candidaturaIds = beneficiarioDoc.get("candidaturas") as? List<String> ?: emptyList()

                        if (candidaturaIds.isEmpty()) {
                            candidaturas = emptyList()
                            loading = false
                            return@addOnSuccessListener
                        }

                        firestore.collection("candidaturas")
                            .whereIn(FieldPath.documentId(), candidaturaIds.take(10))
                            .get()
                            .addOnSuccessListener { candidaturasSnapshot ->
                                candidaturas = candidaturasSnapshot.documents.mapNotNull { doc ->
                                    Candidatura(
                                        idCandidatura = doc.id,
                                        anoLetivo = doc.getString("anoLetivo") ?: "",
                                        nome = doc.getString("nome") ?: "",
                                        email = doc.getString("email") ?: "",
                                        grau = doc.getString("grau") ?: "",
                                        curso = doc.getString("curso") ?: "",
                                        numAluno = doc.getLong("numAluno")?.toInt() ?: 0,
                                        tipologiaPedido = doc.get("tipologiaPedido") as? List<String> ?: emptyList(),
                                        faes = doc.getBoolean("faes") ?: false,
                                        bolseiro = doc.getBoolean("bolseiro") ?: false,
                                        valorBolsa = doc.getDouble("valorBolsa"),
                                        dataSubmissao = doc.getTimestamp("dataSubmissao")?.toDate() ?: Date(),
                                        estadoCandidatura = doc.getString("estadoCandidatura") ?: "Pendente"
                                    )
                                }
                                loading = false
                            }
                    } else {
                        candidaturas = emptyList()
                        loading = false
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Erro ao buscar candidaturas", Toast.LENGTH_SHORT).show()
                    loading = false
                }
        } else {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(true, onBack)

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF006837))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(candidaturas) { candidatura ->
                    CandidaturaCard(candidatura)
                }
            }
        }
    }
}

@Composable
fun CandidaturaCard(
    candidatura: Candidatura
) {
    val corFundo = Color(0xFFE0E0E0)

    val corTextoEstado = when (candidatura.estadoCandidatura) {
        "APROVADA" -> Color(0xFF006837)
        "REPROVADA" -> Color.Red
        "FILA_ESPERA" -> Color(0xFFB8860B)
        else -> Color.Gray
    }

    val textoEstado = when (candidatura.estadoCandidatura) {
        "APROVADA" -> "Aprovada"
        "REPROVADA" -> "Reprovada"
        "FILA_ESPERA" -> "Em fila de espera"
        else -> "Pendente"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(corFundo)
            .padding(16.dp)
    ) {
        // Cabeçalho: Data e Estado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Data submissão:", fontSize = 12.sp, color = Color.Gray)
                Text(candidatura.dataSubmissao.format(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("Estado Candidatura:", fontSize = 12.sp, color = Color.Gray)
                Text(textoEstado, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = corTextoEstado)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Informações detalhadas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text("Nome: ${candidatura.nome}", fontSize = 14.sp)
            Text("Nº aluno: ${candidatura.numAluno}", fontSize = 14.sp)
            Text("Ano letivo: ${candidatura.anoLetivo}", fontSize = 14.sp)
            Text("Grau: ${candidatura.grau}", fontSize = 14.sp)
            Text("Curso: ${candidatura.curso}", fontSize = 14.sp)
            Text("Tipologia: ${candidatura.tipologiaPedido.joinToString(", ")}", fontSize = 14.sp)
            Text("FAES: ${if (candidatura.faes) "Sim" else "Não"}", fontSize = 14.sp)
            Text("Bolseiro: ${if (candidatura.bolseiro) "Sim" else "Não"}", fontSize = 14.sp)
            if (candidatura.bolseiro && candidatura.valorBolsa != null) {
                Text("Valor Bolsa: ${candidatura.valorBolsa} €", fontSize = 14.sp)
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
        CandidaturaCard(
            candidatura = Candidatura(
                    nome = "Fabio Sandro",
                    numAluno = 1340,
                    dataSubmissao = dataSubmissao,
                    dataNascimento = dataNascimento,
                    anoLetivo = "2025/2026",
                    grau = "Licenciatura",
                    curso = "Design Gráfico",
                    estadoCandidatura = "FILA_ESPERA",
                    cartaoCidadao = "12345678",
                    telemovel = "912345678",
                    email = "fabio@example.com",
                    tipologiaPedido = listOf("Roupa", "Alimentação"),
                    faes = true,
                    bolseiro = false,
                    valorBolsa = null
            )
        )
    }
}