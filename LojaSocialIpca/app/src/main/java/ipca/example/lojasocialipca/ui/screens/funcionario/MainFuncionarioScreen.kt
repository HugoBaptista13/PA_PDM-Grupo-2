package ipca.example.lojasocialipca.ui.screens.funcionario

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.R
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Notificacao
import ipca.example.lojasocialipca.ui.components.BottomBar
import ipca.example.lojasocialipca.ui.components.TopBar
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme
import java.util.Date

@Composable
fun MainFuncionarioScreen(
    onProdutos: () -> Unit = {},
    onCampanhas: () -> Unit = {},
    onCandidaturas: () -> Unit = {},
    onFuncionarios: () -> Unit = {},
    onBeneficiarios: () -> Unit = {},
    onEntregas: () -> Unit = {},
    onLogout: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    val uid = AppModule.auth.currentUser?.uid

    val notificacoes = remember { mutableStateListOf<Notificacao>() }

    LaunchedEffect(uid) {
        if (uid!!.isNotEmpty()) {
            AppModule.firestore.collection("notificacoes")
                .whereEqualTo("destinatario", uid)
                .whereEqualTo("resolvida", false)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener
                    if (snapshot != null) {
                        notificacoes.clear()
                        snapshot.documents.forEach { doc ->
                            val mensagem = doc.getString("mensagem") ?: ""
                            val timestamp = doc.getTimestamp("dataEnvio")?.toDate() ?: Date()
                            val destinatario = doc.getString("destinatario") ?: ""
                            val resolvida = doc.getBoolean("resolvida") ?: false
                            notificacoes.add(
                                Notificacao(
                                    id = doc.id,  // ⚡️ ID do documento
                                    mensagem = mensagem,
                                    dataEnvio = timestamp,
                                    destinatario = destinatario,
                                    resolvida = resolvida
                                )
                            )
                        }
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR
        TopBar(false)

        // TÍTULO ALERTAS
        Text(
            text = "Alertas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        // LISTA DE ALERTAS
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notificacoes) { notificacao ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            AppModule.firestore.collection("notificacoes")
                                .document(notificacao.id)
                                .update("resolvida", true)
                                .addOnSuccessListener {
                                    notificacoes.remove(notificacao)
                                }
                        },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "• ${notificacao.mensagem}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = notificacao.dataEnvio.format(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // QUATRO BOTÕES NO FORMATO DO MOCKUP
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FuncionarioTile(texto = "Produtos", iconRes = R.drawable.ic_produtos, onClick = onProdutos)
                    FuncionarioTile(texto = "Campanhas", iconRes = R.drawable.ic_campanhas, onClick = onCampanhas)

                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FuncionarioTile(texto = "Candidaturas", iconRes = R.drawable.ic_candidaturas, onClick = onCandidaturas)
                    FuncionarioTile(texto = "Entregas", iconRes = R.drawable.ic_entregas, onClick = onEntregas)

                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FuncionarioTile(texto = "Funcionários", iconRes = R.drawable.ic_staff, onClick = onFuncionarios)
                    FuncionarioTile(texto = "Beneficiários", iconRes = R.drawable.ic_beneficiario, onClick = onBeneficiarios)

                }
            }

            }
        BottomBar (
            onLogout = onLogout,
            onPerfil = onPerfil
        )
    }
}

@Composable
fun FuncionarioTile(
    texto: String,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width=120.dp,height=120.dp)          // retângulo baixo e largo, como no mockup
            .background(Color(0xFF006837), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ÍCONE 512x512 escalado (zona de cima)
            Image(
                painter = painterResource(iconRes),
                contentDescription = texto,
                modifier = Modifier
                    .height(64.dp)   // ajusta se quiseres maior
            )

            // Texto em baixo
            Text(
                text = texto,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
@Preview(showBackground = true)
fun MainFuncionariopreview() {
    LojaSocialIpcaTheme {
        MainFuncionarioScreen()
    }
}


