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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Entrega
import ipca.example.lojasocialipca.models.Produto
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

@Composable
fun ConsultarEntregasScreen(
    onMarcar: (Entrega) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var entregas by remember { mutableStateOf<List<Entrega>>(emptyList()) }
    var produtosMap by remember { mutableStateOf<Map<String, Produto>>(emptyMap()) }

    var mostrarDialog by remember { mutableStateOf(false) }
    var mostrarDialogCancelar by remember { mutableStateOf(false) }
    var mostrarDialogEntregar by remember { mutableStateOf(false) }
    var entregaSelecionada by remember { mutableStateOf<Entrega?>(null) }

    // Buscar produtos do Firestore
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

    // Buscar entregas do Firestore
    LaunchedEffect(Unit) {
        val entregasRef = AppModule.firestore.collection("entregas")
        entregasRef.addSnapshotListener { snapshot, error ->
            if (snapshot != null) {
                entregas = snapshot.documents.mapNotNull { doc ->
                    try {
                        Entrega(
                            idEntrega = doc.getString("idEntrega") ?: "",
                            numEntrega = doc.getLong("numEntrega")?.toInt() ?: 0,
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
                    } catch (e: Exception) {
                        null
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
        TopBar(true, onBack)

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(entregas) { entrega ->
                EntregaCard(
                    entrega = entrega,
                    produtosMap = produtosMap,
                    onAcaoPrincipal = {
                        when (entrega.estadoEntrega) {
                            "POR_REMARCAR" -> {
                                entregaSelecionada = entrega
                                mostrarDialog = true
                            }
                            "POR_ENTREGAR" -> {
                                entregaSelecionada = entrega
                                mostrarDialogEntregar = true
                            }
                            else -> {
                                onMarcar(entrega)
                            }
                        }
                    },
                    onCancelar = {
                        entregaSelecionada = entrega
                        mostrarDialogCancelar = true
                    }
                )
            }
        }
    }

    // Dialog de remarcar / rejeitar
    if (mostrarDialog && entregaSelecionada != null) {
        Dialog(onDismissRequest = { mostrarDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { mostrarDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.Red)
                        }
                    }

                    Text("Data Original: ${entregaSelecionada!!.dataEntrega?.format() ?: "--"}", fontSize = 18.sp)
                    Text("Nova Data: ${entregaSelecionada!!.dataRemarcacao?.format() ?: "--"}", fontSize = 18.sp)

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            mostrarDialog = false

                            val docRef = AppModule.firestore
                                .collection("entregas")
                                .document(entregaSelecionada!!.idEntrega)

                            docRef.update(
                                mapOf(
                                    "dataEntrega" to entregaSelecionada!!.dataRemarcacao,
                                    "estadoEntrega" to "POR_ENTREGAR"
                                )
                            )
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Entrega remarcada!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Erro ao remarcar entrega", Toast.LENGTH_SHORT).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Remarcar", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

                    Button(
                        onClick = {
                            mostrarDialog = false

                            val docRef = AppModule.firestore
                                .collection("entregas")
                                .document(entregaSelecionada!!.idEntrega)

                            docRef.update(
                                mapOf(
                                    "dataRemarcacao" to null,
                                    "estadoEntrega" to "POR_ENTREGAR"
                                )
                            )
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Remarcação rejeitada!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Erro ao rejeitar remarcação", Toast.LENGTH_SHORT).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Rejeitar", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }

    if (mostrarDialogEntregar && entregaSelecionada != null) {
        Dialog(onDismissRequest = { mostrarDialogEntregar = false }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Botão fechar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { mostrarDialogEntregar = false }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Fechar",
                                tint = Color.Red
                            )
                        }
                    }

                    Text(
                        text = "Confirmar entrega Nº${entregaSelecionada!!.numEntrega}?",
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            mostrarDialogEntregar = false

                            val entrega = entregaSelecionada!!

                            if (entrega.idEntrega.isBlank()) {
                                Toast.makeText(context, "Erro: entrega inválida", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val firestore = AppModule.firestore
                            val batch = firestore.batch()

                            val entregaRef = firestore
                                .collection("entregas")
                                .document(entrega.idEntrega)

                            batch.update(
                                entregaRef,
                                mapOf(
                                    "estadoEntrega" to "ENTREGUE",
                                    "dataEntrega" to Date()
                                )
                            )

                            entrega.produtos.forEach { idProduto ->
                                val produtoRef = firestore
                                    .collection("produtos")
                                    .document(idProduto)

                                batch.update(produtoRef, "estadoProduto", "Inativo")
                            }

                            batch.commit()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Entrega concluída e produtos atualizados!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        context,
                                        "Erro ao concluir entrega: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF006837)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Sim",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Botão NÃO – fecha diálogo
                    Button(
                        onClick = { mostrarDialogEntregar = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB00000)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Não",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (mostrarDialogCancelar && entregaSelecionada != null) {
        Dialog(onDismissRequest = { mostrarDialogCancelar = false }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Botão fechar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { mostrarDialogCancelar = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.Red)
                        }
                    }

                    Text(
                        "Deseja cancelar a entrega Nº${entregaSelecionada!!.numEntrega}?",
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    // Botão SIM - cancela a entrega
                    Button(
                        onClick = {
                            mostrarDialogCancelar = false

                            val docRef = AppModule.firestore
                                .collection("entregas")
                                .document(entregaSelecionada!!.idEntrega)

                            docRef.update("estadoEntrega", "CANCELADA")
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Entrega cancelada!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Erro ao cancelar: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Sim", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    // Botão NÃO - fecha diálogo
                    Button(
                        onClick = { mostrarDialogCancelar = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Não", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EntregaCard(
    entrega: Entrega,
    produtosMap: Map<String, Produto>,
    onAcaoPrincipal: () -> Unit,
    onCancelar: () -> Unit
) {
    val corFundo = if (entrega.estadoEntrega == "ENTREGUE") Color(0xFF00B050)
    else if (entrega.estadoEntrega == "CANCELADA") Color(0xFFFF2F2F)
    else Color(0xFFE0E0E0)

    val estadoTexto = when (entrega.estadoEntrega) {
        "PENDENTE" -> "Pendente"
        "POR_ENTREGAR" -> "Por Entregar"
        "POR_REMARCAR" -> "Por Remarcar"
        "ENTREGUE" -> "Entregue"
        "CANCELADA" -> "Cancelada"
        else -> entrega.estadoEntrega
    }

    val botaoTexto = when (entrega.estadoEntrega) {
        "PENDENTE" -> "Marcar"
        "POR_ENTREGAR" -> "Entregar"
        "POR_REMARCAR" -> "Remarcar"
        else -> null
    }

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
                text = entrega.dataEntrega?.format() ?: "--",
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

            val botaoModifier = Modifier.width(108.dp).height(44.dp)

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

                    Button(
                        onClick = onCancelar,
                        modifier = botaoModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Cancelar") }
                }
            }
        }
    }
}

/* ---------- PREVIEW ---------- */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarEntregasScreenPreview() {
    MaterialTheme {
        ConsultarEntregasScreen()
    }
}
