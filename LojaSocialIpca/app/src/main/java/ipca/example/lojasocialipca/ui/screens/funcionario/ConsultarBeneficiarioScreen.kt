package ipca.example.lojasocialipca.ui.screens.funcionario

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.firestore.FieldPath
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Candidatura
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

data class BeneficiarioUI(
    val idBeneficiario: String,
    val nome: String,
    val dataA: Date,
    val numAluno: String,
    val email: String,
    val grau: String,
    val curso: String,
    val candidaturas: List<Candidatura>
)

@Composable
fun ConsultarBeneficiarioScreen(
    onBack: () -> Unit = {}
) {
    val firestore = AppModule.firestore

    var beneficiariosUI by remember { mutableStateOf<List<BeneficiarioUI>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val listener = firestore.collection("beneficiarios")
            .addSnapshotListener { snapshot, error ->

                if (error != null || snapshot == null) {
                    loading = false
                    return@addSnapshotListener
                }

                loading = true

                val beneficiariosDocs = snapshot.documents

                val candidaturasIds = beneficiariosDocs
                    .flatMap { it.get("candidaturas") as? List<String> ?: emptyList() }
                    .distinct()

                if (candidaturasIds.isEmpty()) {
                    beneficiariosUI = emptyList()
                    loading = false
                    return@addSnapshotListener
                }

                firestore.collection("candidaturas")
                    .whereIn(FieldPath.documentId(), candidaturasIds.take(10))
                    .get()
                    .addOnSuccessListener { candidaturasSnapshot ->

                        val candidaturasMap = candidaturasSnapshot.documents.mapNotNull { doc ->
                            Candidatura(
                                idCandidatura = doc.id,
                                anoLetivo = doc.getString("anoLetivo") ?: "",
                                nome = doc.getString("nome") ?: "",
                                email = doc.getString("email") ?: "",
                                grau = doc.getString("grau") ?: "",
                                curso = doc.getString("curso") ?: "",
                                numAluno = doc.getLong("numAluno")?.toInt() ?: 0,
                                tipologiaPedido = doc.get("tipologiaPedido") as? List<String> ?: emptyList(),
                                estadoCandidatura = doc.getString("estadoCandidatura") ?: ""
                            )
                        }.associateBy { it.idCandidatura }

                        beneficiariosUI = beneficiariosDocs.mapNotNull { beneficiarioDoc ->

                            val aceite = beneficiarioDoc.getBoolean("aceite") ?: false
                            if (!aceite) return@mapNotNull null   // 👈 SÓ ACEITES

                            val dataAprovacao = beneficiarioDoc
                                .getTimestamp("dataAprovacao")
                                ?.toDate() ?: return@mapNotNull null

                            val ids = beneficiarioDoc.get("candidaturas") as? List<String> ?: emptyList()
                            val candidaturas = ids.mapNotNull { candidaturasMap[it] }

                            if (candidaturas.isEmpty()) return@mapNotNull null

                            val principal = candidaturas
                                .firstOrNull { it.estadoCandidatura == "APROVADA" }
                                ?: candidaturas.first()

                            BeneficiarioUI(
                                idBeneficiario = beneficiarioDoc.id,
                                nome = principal.nome,
                                dataA = dataAprovacao,
                                numAluno = principal.numAluno.toString(),
                                email = principal.email,
                                grau = principal.grau,
                                curso = principal.curso,
                                candidaturas = candidaturas
                            )
                        }

                        loading = false
                    }
            }

        onDispose { listener.remove() }
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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(beneficiariosUI) { beneficiario ->
                    BeneficiarioCard(
                        idBeneficiario = beneficiario.idBeneficiario,
                        nome = beneficiario.nome,
                        dataA = beneficiario.dataA,
                        numAluno = beneficiario.numAluno,
                        email = beneficiario.email,
                        grau = beneficiario.grau,
                        curso = beneficiario.curso,
                        candidaturas = beneficiario.candidaturas
                    )
                }
            }
        }
    }
}



@Composable
fun BeneficiarioCard(
    idBeneficiario: String,
    nome: String,
    dataA: Date,
    numAluno: String,
    email: String,
    grau: String,
    curso: String,
    candidaturas: List<Candidatura>
) {
    var expandido by remember { mutableStateOf(false) }
    var mostrarDialog by remember { mutableStateOf(false) }
    var aAtualizar by remember { mutableStateOf(false) }

    val firestore = AppModule.firestore
    val context = LocalContext.current
    val letraInicial = nome.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            /* ---------- CABEÇALHO ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF006837)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letraInicial,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(nome, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Data de aprovação: ${dataA.format()}", fontSize = 14.sp)
                    Text("Email: $email", fontSize = 14.sp)
                    Text("Nº Aluno: $numAluno", fontSize = 14.sp)
                    Text("Curso: $curso", fontSize = 14.sp)
                    Text("Grau: $grau", fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider()

            /* ---------- MOSTRAR CANDIDATURAS ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandido = !expandido }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Mostrar candidaturas",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(if (expandido) 180f else 0f)
                )
            }

            AnimatedVisibility(visible = expandido) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    candidaturas.forEach {
                        CandidaturaResumoCard(it)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            /* ---------- BOTÃO REVOGAR ---------- */
            Button(
                onClick = { mostrarDialog = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = !aAtualizar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Revogar acesso", color = Color.White)
            }
        }
    }

    /* ---------- DIÁLOGO ---------- */
    if (mostrarDialog) {
        Dialog(onDismissRequest = { if (!aAtualizar) mostrarDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        "Revogar acesso?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "O beneficiário \"$nome\" deixará de ter acesso.",
                        fontSize = 14.sp
                    )

                    Button(
                        enabled = !aAtualizar,
                        onClick = {
                            aAtualizar = true

                            firestore.collection("beneficiarios")
                                .document(idBeneficiario)
                                .update("aceite", false)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Acesso revogado com sucesso",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    mostrarDialog = false
                                    aAtualizar = false
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Erro ao revogar acesso",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    aAtualizar = false
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (aAtualizar) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Confirmar revogação", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun CandidaturaResumoCard(candidatura: Candidatura) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0xFFF5F5F5))
        .border(
            width = 2.dp,
            color = Color(0xFF00361F),
            shape = RoundedCornerShape(12.dp)
        )
        .padding(12.dp)
    ) {
        Text(
            text = "Ano Letivo: ${candidatura.anoLetivo}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Text("Data de submissão: ${candidatura.dataSubmissao.format()}", fontSize = 13.sp)
        Text("Grau: ${candidatura.grau}", fontSize = 13.sp)
        Text("Curso: ${candidatura.curso}", fontSize = 13.sp)

        Text(
            text = "Tipologia: ${candidatura.tipologiaPedido.joinToString(", ")}",
            fontSize = 13.sp
        )
    }
}



@Preview(showBackground = true)
@Composable
fun BeneficiarioCardPreview() {
    BeneficiarioCard(
        idBeneficiario = "",
        nome = "Galerinha",
        dataA = Date(),
        numAluno = "12356",
        email = "a12356@alunos.ipca.pt",
        grau = "Licenciatura",
        curso = "RSI",
        candidaturas = listOf(
            Candidatura(tipologiaPedido = listOf("Produto Alimentar", "Produto de Higiene Pessoal", "Produto de Limpeza")),
            Candidatura(tipologiaPedido = listOf("Produto Alimentar"))
        )
    )
}