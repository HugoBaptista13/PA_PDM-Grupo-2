package ipca.example.lojasocialipca.ui.screens.funcionario

import android.util.Log
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
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Campanha
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme
import java.util.Calendar
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

@Composable
fun ConsultarCampanhaView(
    onCriarCampanha: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var campanhas by remember { mutableStateOf<List<Campanha>>(emptyList()) }

    // Buscar campanhas do Firestore e ouvir alterações em tempo real
    LaunchedEffect(Unit) {
        val collectionRef = AppModule.firestore.collection("campanhas")

        collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("Firestore", "Erro ao buscar campanhas", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val lista = snapshot.documents.mapNotNull { doc ->
                    try {
                        Campanha(
                            nome = doc.getString("nome") ?: "",
                            dataInicio = doc.getTimestamp("dataInicio")?.toDate() ?: Date(),
                            dataFim = doc.getTimestamp("dataFim")?.toDate(),
                            descricao = doc.getString("descricao") ?: "",
                            tipo = doc.getString("tipo") ?: "",
                            concluida = doc.getBoolean("concluida") ?: false,
                            responsavel = doc.getString("responsavel") ?: ""
                        )
                    } catch (e: Exception) {
                        null
                    }
                }.sortedBy { it.dataInicio } // ordenar por data de início
                campanhas = lista
            }
        }
    }

    // Função para finalizar campanha
    fun finalizarCampanha(campanha: Campanha) {
        AppModule.firestore.collection("campanhas")
            .whereEqualTo("nome", campanha.nome)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val docId = querySnapshot.documents[0].id
                    AppModule.firestore.collection("campanhas")
                        .document(docId)
                        .update("concluida", true)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Campanha '${campanha.nome}' finalizada com sucesso!")
                            // Não precisa atualizar a lista manualmente, o snapshot listener fará isso
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Erro ao finalizar campanha", e)
                        }
                } else {
                    Log.e("Firestore", "Campanha '${campanha.nome}' não encontrada")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao buscar campanha para finalizar", e)
            }
    }

    // Chamamos o Composable existente passando a lista e a função de finalizar
    ConsultarCampanhaScreen(
        campanhas = campanhas,
        onFinalizarCampanha = { finalizarCampanha(it) },
        onCriarCampanha = onCriarCampanha,
        onBack = onBack
    )
}


@Composable
fun ConsultarCampanhaScreen(
    campanhas: List<Campanha>,
    onFinalizarCampanha: (Campanha) -> Unit = {},
    onCriarCampanha: ()-> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
        TopBar(true, onBack)

        // BOTÃO CRIAR CAMPANHA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onCriarCampanha,
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
                Text("Descrição: ${campanha.descricao}", fontSize = 12.sp)
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

