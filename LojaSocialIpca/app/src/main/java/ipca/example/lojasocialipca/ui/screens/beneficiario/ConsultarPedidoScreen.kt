package ipca.example.lojasocialipca.ui.screens.beneficiario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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
import ipca.example.lojasocialipca.models.Entrega
import ipca.example.lojasocialipca.models.Produto
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date


@Composable
fun ConsultarPedidoScreen(
    onRealizaRemarcar: (Entrega) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val firestore = AppModule.firestore
    val auth = AppModule.auth
    val context = LocalContext.current

    var produtosMap by remember { mutableStateOf<Map<String, Produto>>(emptyMap()) }
    var entregas by remember { mutableStateOf<List<Entrega>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val produtosRef = AppModule.firestore.collection("produtos")
        produtosRef.addSnapshotListener { snapshot, error ->
            if (snapshot != null) {
                produtosMap = snapshot.documents.mapNotNull { doc ->
                    val id = doc.id
                    val nome = doc.getString("nome") ?: return@mapNotNull null
                    id to Produto(
                        idProduto = id,
                        nome = nome,
                        categoria = doc.getString("categoria") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        campanha = doc.getString("campanha"),
                        validade = doc.getTimestamp("validade")?.toDate() ?: Date(),
                        estadoProduto = doc.getString("estadoProduto") ?: "",
                        dataEntrada = doc.getTimestamp("dataEntrada")?.toDate() ?: Date(),
                        responsavel = doc.getString("responsavel") ?: ""
                    )
                }.toMap()
            }
        }
    }

    // Buscar entregas do beneficiário logado
    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("entregas")
                .whereEqualTo("destinatario", uid)
                .orderBy("numEntrega")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Toast.makeText(context, "Erro ao buscar entregas", Toast.LENGTH_SHORT).show()
                        loading = false
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        entregas = snapshot.documents.mapNotNull { doc ->
                            Entrega(
                                numEntrega = doc.getLong("numEntrega")?.toInt() ?: 1,
                                destinatario = doc.getString("destinatario") ?: "",
                                responsavel = doc.getString("responsavel"),
                                dataSubmissao = doc.getTimestamp("dataSubmissao")?.toDate() ?: Date(),
                                dataEntrega = doc.getTimestamp("dataEntrega")?.toDate(),
                                dataRemarcacao = doc.getTimestamp("dataRemarcacao")?.toDate(),
                                estadoEntrega = doc.getString("estadoEntrega") ?: "PENDENTE",
                                produtos = doc.get("produtos") as? List<String> ?: emptyList(),
                                tipo = doc.getString("tipo") ?: "",
                                descricao = doc.getString("descricao") ?: ""
                            )
                        }
                    }

                    loading = false
                }
        } else {
            Toast.makeText(context, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR
        TopBar(true, onBack)

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF006837))
            }
        } else {
            if (entregas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma entrega encontrada.", fontSize = 16.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(entregas) { entrega ->
                        PedidoCard(
                            entrega = entrega,
                            produtosMap = produtosMap,
                            onAcaoPrincipal = { onRealizaRemarcar(entrega) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PedidoCard(
    entrega: Entrega,
    produtosMap: Map<String, Produto>,
    onAcaoPrincipal: () -> Unit
) {
    val corFundo = when (entrega.estadoEntrega) {
        "PENDENTE" -> Color(0xFFE0E0E0)
        "POR_ENTREGAR" -> Color(0xFFE0E0E0)
        "POR_REMARCAR" -> Color(0xFFFFC000)
        "ENTREGUE" -> Color(0xFF00B050)
        "CANCELADA" -> Color(0xFFFF2F2F)
        else -> Color(0xFFE0E0E0)
    }

    val estadoTexto = when (entrega.estadoEntrega) {
        "PENDENTE" -> "Pendente"
        "POR_ENTREGAR" -> "Por Entregar"
        "POR_REMARCAR" -> "Por Remarcar"
        "ENTREGUE" -> "Entregue"
        "CANCELADA" -> "Cancelada"
        else -> entrega.estadoEntrega
    }

    val botaoTexto = if (entrega.estadoEntrega == "POR_ENTREGAR") "Remarcar" else null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(corFundo)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = entrega.dataEntrega?.format() ?: entrega.dataSubmissao.format(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(text = estadoTexto, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                entrega.dataRemarcacao?.let {
                    Text(text = it.format(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                if (entrega.estadoEntrega == "PENDENTE") {
                    Text("Tipo: ${entrega.tipo}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(entrega.descricao, fontSize = 12.sp)
                } else {
                    // Agrupar produtos pelo nome e contar ocorrências
                    val produtosDetalhados = entrega.produtos
                        .mapNotNull { produtosMap[it] }
                        .groupingBy { it.nome }
                        .eachCount()

                    produtosDetalhados.forEach { (nome, qtd) ->
                        Text("$nome x $qtd", fontSize = 14.sp)
                    }
                }
            }

            val botaoModifier = Modifier
                .width(108.dp)
                .height(44.dp)

            if (botaoTexto != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onAcaoPrincipal,
                        modifier = botaoModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text(botaoTexto) }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarPedidoScreenPreview() {
    PedidoCard(
        entrega = Entrega(),
        produtosMap = emptyMap(),
        onAcaoPrincipal = { }
    )
}