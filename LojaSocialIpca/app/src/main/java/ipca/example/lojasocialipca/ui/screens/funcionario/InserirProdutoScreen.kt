package ipca.example.lojasocialipca.ui.screens.funcionario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Campanha
import ipca.example.lojasocialipca.models.TipoProduto
import ipca.example.lojasocialipca.ui.components.ComboBox
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

@Composable
fun InserirProdutoScreen(
    onCancelar: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var nomeProduto by remember { mutableStateOf("") }
    var tipos by remember { mutableStateOf<List<TipoProduto>>(emptyList()) }
    var tipo by remember { mutableStateOf<TipoProduto?>(null) }
    var categoria by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var campanhas by remember { mutableStateOf<List<Campanha>>(emptyList()) }
    var campanha by remember { mutableStateOf<Campanha?>(null) }
    var quantidade by remember { mutableStateOf(1) }
    var mostrarDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // --- Queries ao Firestore ---
    LaunchedEffect(Unit) {
        AppModule.firestore.collection("tipos_produto")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    tipos = snapshot.documents.map { doc ->
                        TipoProduto(
                            tipo = doc.getString("tipo") ?: "",
                            categorias = (doc.get("categorias") as? List<String>)?.toMutableList()
                                ?: mutableListOf()
                        )
                    }
                }
            }

        AppModule.firestore.collection("campanhas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    campanhas = snapshot.documents.map { doc ->
                        Campanha(
                            nome = doc.getString("nome") ?: "",
                            dataInicio = doc.getTimestamp("dataInicio")?.toDate() ?: Date(),
                            dataFim = doc.getTimestamp("dataFim")?.toDate(),
                            descricao = doc.getString("descricao") ?: "",
                            tipo = doc.getString("tipo") ?: "",
                            concluida = doc.getBoolean("concluida") ?: false,
                            responsavel = doc.getString("responsavel") ?: ""
                        )
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(mostrarBack = true, onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Inserir Produto", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = nomeProduto,
                onValueChange = { nomeProduto = it },
                label = { Text("Nome:") },
                modifier = Modifier.fillMaxWidth()
            )

            // TIPO
            ComboBox(
                label = "Tipo:",
                selected = tipo?.tipo ?: "",
                options = tipos.map { it.tipo },
                onSelect = { tipoNome ->
                    tipo = tipos.first { it.tipo == tipoNome }
                    categoria = ""
                },
                modifier = Modifier.fillMaxWidth()
            )

            // CATEGORIA
            if (tipo != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ComboBox(
                        label = "Categoria:",
                        selected = categoria,
                        options = tipo!!.categorias,
                        onSelect = { categoria = it },
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF006837), CircleShape)
                            .clickable { mostrarDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar categoria", tint = Color.White)
                    }
                }
            }

            // DATA DE VALIDADE
            Text("Data de Validade:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(dia, onValueChange = { dia = it }, label = { Text("Dia") }, modifier = Modifier.weight(1f))
                OutlinedTextField(mes, onValueChange = { mes = it }, label = { Text("Mês") }, modifier = Modifier.weight(1f))
                OutlinedTextField(ano, onValueChange = { ano = it }, label = { Text("Ano") }, modifier = Modifier.weight(1f))
            }

            // CAMPANHA
            ComboBox(
                label = "Campanha:",
                selected = campanha?.nome ?: "",
                options = campanhas.map { it.nome },
                onSelect = { nomeCampanha ->
                    campanha = campanhas.first { it.nome == nomeCampanha }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // QUANTIDADE
            Text("Quantidade:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(quantidade.toString(), onValueChange = {}, readOnly = true, modifier = Modifier.width(100.dp))
                Spacer(Modifier.width(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .clickable { quantidade++ },
                        contentAlignment = Alignment.Center
                    ) { Text("+", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .clickable { if (quantidade > 1) quantidade-- },
                        contentAlignment = Alignment.Center
                    ) { Text("–", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        // Validação
                        if (nomeProduto.isBlank() || tipo == null || categoria.isBlank() ||
                            dia.isBlank() || mes.isBlank() || ano.isBlank()
                        ) {
                            Toast.makeText(context, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val validade = try {
                            criarData(ano.toInt(), mes.toInt() - 1, dia.toInt())
                        } catch (e: Exception) {
                            Toast.makeText(context, "Data inválida", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val dataEntrada = Date()
                        val baseId = dataEntrada.format().replace("/", "") // ex: "13012026"

                        val tasks = mutableListOf<com.google.android.gms.tasks.Task<Void>>() // Lista de tasks de Firestore

                        for (i in 1..quantidade) {
                            val idProduto = "$baseId$i"
                            val produto = hashMapOf(
                                "idProduto" to idProduto,
                                "nome" to nomeProduto.trim(),
                                "tipo" to tipo!!.tipo,
                                "categoria" to categoria,
                                "validade" to validade,
                                "dataEntrada" to dataEntrada,
                                "estadoProduto" to ESTADO_ATIVO,
                                "responsavel" to AppModule.auth.currentUser?.uid.orEmpty(),
                                "campanha" to campanha?.nome.orEmpty()
                            )

                            val task = AppModule.firestore.collection("produtos")
                                .document(idProduto)
                                .set(produto)

                            tasks.add(task)
                        }

                        // Esperar todos os inserts
                        com.google.android.gms.tasks.Tasks.whenAllComplete(tasks)
                            .addOnSuccessListener { results ->
                                val failed = results.filter { !it.isSuccessful }
                                if (failed.isEmpty()) {
                                    Toast.makeText(context, "$quantidade produtos inseridos com sucesso!", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    val failedCount = failed.size
                                    Toast.makeText(
                                        context,
                                        "Erro ao inserir $failedCount produtos. Tente novamente.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF037C49)),
                    modifier = Modifier.weight(1f).height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Inserir", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White) }

                Button(
                    onClick = onCancelar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9F0D00)),
                    modifier = Modifier.weight(1f).height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Cancelar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White) }
            }

            Spacer(Modifier.height(12.dp))
        }

        // Dialog de nova categoria
        if (mostrarDialog && tipo != null) {
            AdicionarCategoriaDialog(
                onDismiss = { mostrarDialog = false },
                onCategoriaAdicionar = { novaCategoria ->
                    if (!tipo!!.categorias.contains(novaCategoria)) {
                        tipo!!.categorias.add(novaCategoria)
                        categoria = novaCategoria

                        // Atualizar no Firestore
                        AppModule.firestore.collection("tipos_produto")
                            .document(tipo!!.tipo)
                            .set(
                                mapOf("tipo" to tipo!!.tipo, "categorias" to tipo!!.categorias)
                            )
                    }
                }
            )
        }
    }
}

@Composable
fun AdicionarCategoriaDialog(
    onDismiss: () -> Unit,
    onCategoriaAdicionar: (String) -> Unit
) {
    var novaCategoria by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // FECHAR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color.Red
                        )
                    }
                }

                Text(
                    text = "Adicionar nova categoria",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // INPUT
                OutlinedTextField(
                    value = novaCategoria,
                    onValueChange = { novaCategoria = it },
                    label = { Text("Nome da categoria") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ADICIONAR
                Button(
                    onClick = {
                        onCategoriaAdicionar(novaCategoria.trim())
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = novaCategoria.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Adicionar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // CANCELAR
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Cancelar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AdicionarCategoriaDialogPreview() {
    AdicionarCategoriaDialog(
        onDismiss = {},
        onCategoriaAdicionar = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewInserirProdutoSimples() {
    MaterialTheme {
        InserirProdutoScreen()
    }
}





