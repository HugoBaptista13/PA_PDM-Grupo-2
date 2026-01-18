package ipca.example.lojasocialipca.ui.screens.funcionario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.models.Entrega
import ipca.example.lojasocialipca.models.Produto
import ipca.example.lojasocialipca.ui.components.ComboBox
import ipca.example.lojasocialipca.ui.components.NumericUpDown
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

class LinhaEntregaProduto {

    var categoria by mutableStateOf("")
    var quantidade by mutableIntStateOf(1)

    val produtosSelecionados = mutableStateListOf<String>()
}

@Composable
fun MarcarEntregaScreen(
    entrega: Entrega,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val linhas = remember { mutableStateListOf<LinhaEntregaProduto>() }

    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }

    var produtos by remember { mutableStateOf<List<Produto>>(emptyList()) }
    var categorias by remember { mutableStateOf<List<String>>(emptyList()) }

    // Buscar produtos do Firestore
    LaunchedEffect(Unit) {
        val produtosRef = AppModule.firestore.collection("produtos")
        produtosRef.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                produtos = snapshot.documents.mapNotNull { doc ->
                    val id = doc.id
                    val nome = doc.getString("nome") ?: return@mapNotNull null
                    Produto(
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
                }
                categorias = produtos.filter { it.estadoProduto == "Ativo" }
                    .map { it.categoria }
                    .distinct()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(true, onBack)

        // HEADER + BOTÕES ADICIONAR/REMOVER LINHA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Produtos", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                IconButton(
                    onClick = { linhas.add(LinhaEntregaProduto()) },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF006837), CircleShape)
                ) { Icon(Icons.Default.Add, null, tint = Color.White) }

                IconButton(
                    onClick = { if (linhas.isNotEmpty()) linhas.removeAt(linhas.size - 1) },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Red, CircleShape)
                ) { Icon(Icons.Default.Delete, null, tint = Color.White) }
            }
        }

        // LISTA DE LINHAS
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(linhas) { linha ->
                val produtosDaCategoria = produtos.filter {
                    it.estadoProduto == "Ativo" && it.categoria == linha.categoria
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFF006837), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ComboBox(
                            label = "Categoria",
                            selected = linha.categoria,
                            options = categorias,
                            onSelect = {
                                linha.categoria = it
                                linha.produtosSelecionados.clear()
                            },
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )

                        Spacer(Modifier.width(8.dp))

                        NumericUpDown(
                            value = linha.quantidade,
                            onValueChange = { linha.quantidade = it }
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    repeat(linha.quantidade) { index ->
                        val opcoesDisponiveis = produtosDaCategoria
                            .map { "ID-${it.idProduto}-${it.nome}" }
                            .filter { selecionado ->
                                linha.produtosSelecionados.getOrNull(index) == selecionado ||
                                        !linha.produtosSelecionados.contains(selecionado)
                            }

                        ComboBox(
                            label = "Produto ${index + 1}",
                            selected = linha.produtosSelecionados.getOrNull(index) ?: "",
                            options = opcoesDisponiveis,
                            onSelect = { selecionado ->
                                if (linha.produtosSelecionados.size > index)
                                    linha.produtosSelecionados[index] = selecionado
                                else
                                    linha.produtosSelecionados.add(selecionado)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        }

        // DATA E BOTÃO CONFIRMAR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Data da entrega", fontSize = 14.sp, fontWeight = FontWeight.Normal)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = dia,
                    onValueChange = { input ->
                        dia = input.filter { it.isDigit() }
                            .take(2)
                            .let { if (it.toIntOrNull() in 1..31 || it.isEmpty()) it else dia }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Dia") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = mes,
                    onValueChange = { input ->
                        mes = input.filter { it.isDigit() }
                            .take(2)
                            .let { if (it.toIntOrNull() in 1..12 || it.isEmpty()) it else mes }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Mês") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = ano,
                    onValueChange = { input ->
                        ano = input.filter { it.isDigit() }.take(4)
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Ano") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Button(
                onClick = {

                    if (entrega.idEntrega.isBlank()) {
                        Toast.makeText(
                            context,
                            "Erro: entrega sem identificador",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val dataEntrega = try {
                        criarData(ano.toInt(), mes.toInt() - 1, dia.toInt())
                    } catch (e: Exception) {
                        Toast.makeText(context, "Data inválida", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Preparar lista de IDs de produtos selecionados
                    val produtosSelecionadosIds = linhas.flatMap { linha ->
                        linha.produtosSelecionados.map {
                            it.substringAfter("ID-").substringBefore("-")
                        }
                    }

                    // Atualizar entrega no Firestore (AGORA SEGURO)
                    val docRef = AppModule.firestore
                        .collection("entregas")
                        .document(entrega.idEntrega)

                    val updateData = mapOf(
                        "produtos" to produtosSelecionadosIds,
                        "dataEntrega" to dataEntrega,
                        "estadoEntrega" to "POR_ENTREGAR"
                    )

                    docRef.update(updateData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Entrega marcada com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            onBack()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Erro ao marcar entrega: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837))
            ) {
                Text("Confirmar Marcação", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MarcarEntregaScreenPreview() {
    MarcarEntregaScreen(
        onBack = {},
        entrega = Entrega()
    )
}

