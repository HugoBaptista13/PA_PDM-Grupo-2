package ipca.example.lojasocialipca.ui.screens.funcionario

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
fun AvaliarCandidaturaScreen(
    candidatura: Candidatura,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var mostrarMotivo by remember { mutableStateOf(false) }
    var motivo by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // fundo da tela
    ) {
        TopBar(true, onBack)

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp) // padding lateral uniforme
        ) {
            // ===== DADOS PESSOAIS =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Dados Pessoais", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Text("Nome: ${candidatura.nome}")
                    Text("Cartão de Cidadão: ${candidatura.cartaoCidadao}")
                    Text("Data Nascimento: ${candidatura.dataNascimento.format()}")
                    Text("Telemóvel: ${candidatura.telemovel}")
                    Text("Email: ${candidatura.email}")
                    Text("Grau: ${candidatura.grau}")
                }
            }

            Spacer(Modifier.height(12.dp))

            // ===== INFORMACOES ACADÊMICAS =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Informações Acadêmicas", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Text("Nº Aluno: ${candidatura.numAluno}")
                    Text("Curso: ${candidatura.curso}")
                    Text("Ano Letivo: ${candidatura.anoLetivo}")
                }
            }

            Spacer(Modifier.height(12.dp))

            // ===== PEDIDO / TIPOS =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Pedido", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Text("Tipologia Pedido: ${candidatura.tipologiaPedido.joinToString()}")
                    Text("FAES: ${if (candidatura.faes) "Sim" else "Não"}")
                    Text("Bolseiro: ${if (candidatura.bolseiro) "Sim" else "Não"}")
                    Text("Valor Bolsa: ${candidatura.valorBolsa ?: "-"}")
                }
            }

            Spacer(Modifier.height(12.dp))

            // ===== ESTADO =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Estado da Candidatura", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Text(
                        "Estado atual: ${
                            when (candidatura.estadoCandidatura) {
                                "FILA_ESPERA" -> "Em fila de espera"
                                "APROVADA" -> "Aprovada"
                                "REPROVADA" -> "Reprovada"
                                else -> "Desconhecido"
                            }
                        }"
                    )
                    Text("Data submissão: ${candidatura.dataSubmissao.format()}")
                }
            }

            Spacer(Modifier.height(24.dp))

            // ===== BOTÕES APROVAR / REPROVAR =====
            if (candidatura.estadoCandidatura == "FILA_ESPERA") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            // Atualiza Firestore
                            val docRef = AppModule.firestore
                                .collection("candidaturas")
                                .document(candidatura.idCandidatura)

                            docRef.update("estadoCandidatura", "APROVADA")
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Candidatura aprovada!", Toast.LENGTH_SHORT).show()
                                    onBack()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Erro ao aprovar: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B050)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Aprovar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { mostrarMotivo = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4C4C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reprovar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // MOTIVO DE REPROVAÇÃO
            if (mostrarMotivo) {
                Spacer(Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Motivo da reprovação:", fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = motivo,
                        onValueChange = { motivo = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4,
                        label = { Text("Indique o motivo") }
                    )

                    Button(
                        onClick = {
                            if (motivo.isNotBlank()) {
                                val docRef = AppModule.firestore
                                    .collection("candidaturas")
                                    .document(candidatura.idCandidatura)

                                docRef.update(
                                    mapOf(
                                        "estadoCandidatura" to "REPROVADA",
                                        "motivoReprovacao" to motivo
                                    )
                                )
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Candidatura reprovada!", Toast.LENGTH_SHORT).show()
                                        onBack()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Erro ao reprovar: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                Toast.makeText(context, "Indique o motivo da reprovação", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Submeter Reprovação", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
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
                tipologiaPedido = listOf("TESTE", "TESTE"),
                faes = false,
                bolseiro = false,
                valorBolsa = null,
                dataSubmissao = Date(),
                estadoCandidatura = "FILA_ESPERA"
            )
        )
    }
}

