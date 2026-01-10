package ipca.example.lojasocialipca

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.models.Produto
import ipca.example.lojasocialipca.ui.components.ComboBox
import ipca.example.lojasocialipca.ui.components.NumericUpDown
import java.util.Calendar



fun produtosDisponiveisParaIndice(
    produtos: List<Produto>,
    selecionados: List<String>,
    indiceAtual: Int
): List<String> {
    return produtos
        .map { "ID-${it.idProduto}-${it.nome}" }
        .filter { selecionado ->
            selecionados.getOrNull(indiceAtual) == selecionado ||
                    !selecionados.contains(selecionado)
        }
}


fun categoriasDisponiveis(produtos: List<Produto>): List<String> =
    produtos
        .filter { it.estadoProduto == "Ativo" }
        .map { it.categoria }
        .distinct()

fun produtosPorCategoria(
    produtos: List<Produto>,
    categoria: String
): List<Produto> =
    produtos.filter {
        it.estadoProduto == "Ativo" && it.categoria == categoria
    }

class LinhaEntregaProduto {

    var categoria by mutableStateOf("")
    var quantidade by mutableIntStateOf(1)

    val produtosSelecionados = mutableStateListOf<String>()
}

@Composable
fun TESTE() {
    val dataExemplo = criarData(2025, Calendar.MAY, 25)
    val dataValidadeExemplo = criarData(2025, Calendar.DECEMBER, 18)

    MarcarEntregaScreen(
        produtos = listOf(
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Arroz 1Kg",
                tipo = "Alimentar",
                categoria = "Arroz",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520252",
                campanha = "Teste",
                nome = "Arroz 1Kg",
                tipo = "Alimentar",
                categoria = "Arroz",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520253",
                campanha = "Teste",
                nome = "Arroz 1Kg",
                tipo = "Alimentar",
                categoria = "Arroz",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Lata de Atum natural",
                tipo = "Alimentar",
                categoria = "Enlatados",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520252",
                campanha = "Teste",
                nome = "Lata de Atum natural",
                tipo = "Alimentar",
                categoria = "Enlatados",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Arroz 1Kg",
                tipo = "Alimentar",
                categoria = "Arroz",
                validade = dataValidadeExemplo ,
                estadoProduto = "Desativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Lixivia 1L",
                tipo = "Limpeza",
                categoria = "Lixivia",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Sabão líquido 1L",
                tipo = "Higiene Pessoal",
                categoria = "Sabão",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            )
        ),
        onBack = {}
    )
}

@Composable
fun MarcarEntregaScreen(
    produtos: List<Produto>,
    onBack: () -> Unit = {}
) {
    val linhas = remember { mutableStateListOf<LinhaEntregaProduto>() }

    var ano by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }

    val categorias = categoriasDisponiveis(produtos)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fundo branco para toda a tela
    ) {

        // TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF006837))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Text(
                "Loja Social",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // HEADER PRODUTOS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Produtos", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                // Botão Adicionar Linha
                IconButton(
                    onClick = { linhas.add(LinhaEntregaProduto()) },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF006837), CircleShape)
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                }

                // Botão Remover Linha (apaga a última)
                IconButton(
                    onClick = {
                        if (linhas.isNotEmpty()) linhas.removeAt(linhas.size - 1)
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Red, CircleShape)
                ) {
                    Icon(Icons.Default.Delete, null, tint = Color.White)
                }
            }
        }

        // LISTA DE PRODUTOS COM SCROLL E FUNDO CINZA
        LazyColumn(
            modifier = Modifier
                .weight(1f) // Ocupa o espaço restante
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0)) // Apenas a lista com fundo cinza
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(linhas) { linha ->
                val produtosDaCategoria = produtosPorCategoria(produtos, linha.categoria)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFF006837), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    // Categoria + quantidade
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
                            modifier = Modifier.fillMaxWidth(0.5f),
                        )

                        Spacer(Modifier.width(8.dp))

                        NumericUpDown(
                            value = linha.quantidade,
                            onValueChange = { linha.quantidade = it }
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Produtos
                    repeat(linha.quantidade) { index ->
                        val opcoesDisponiveis = produtosDisponiveisParaIndice(
                            produtos = produtosDaCategoria,
                            selecionados = linha.produtosSelecionados,
                            indiceAtual = index
                        )

                        ComboBox(
                            label = "Produto ${index + 1}",
                            selected = linha.produtosSelecionados.getOrNull(index) ?: "",
                            options = opcoesDisponiveis,
                            onSelect = { selecionado ->
                                if (linha.produtosSelecionados.size > index) {
                                    linha.produtosSelecionados[index] = selecionado
                                } else {
                                    linha.produtosSelecionados.add(selecionado)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        }

        // DATA E BOTÃO CONFIRMAR (SEM FUNDO CINZA)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Data da entrega",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = dia,
                    onValueChange = { dia = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Dia") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = mes,
                    onValueChange = { mes = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Mês") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = ano,
                    onValueChange = { ano = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Ano") },
                    singleLine = true
                )
            }

            Button(
                onClick = { /* TODO: ação de confirmar */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837))
            ) {
                Text(
                    text = "Confirmar Marcação",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MarcarEntregaScreenPreview() {
    val dataExemplo = criarData(2025, Calendar.MAY, 25)
    val dataValidadeExemplo = criarData(2025, Calendar.DECEMBER, 18)

    MarcarEntregaScreen(
        produtos = listOf(
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Arroz 1Kg",
                tipo = "Alimentar",
                categoria = "Arroz",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520252",
                campanha = "Teste",
                nome = "Arroz 1Kg",
                tipo = "Alimentar",
                categoria = "Arroz",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520253",
                campanha = "Teste",
                nome = "Arroz 1Kg",
                tipo = "Alimentar",
                categoria = "Arroz",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Lata de Atum natural",
                tipo = "Alimentar",
                categoria = "Enlatados",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520252",
                campanha = "Teste",
                nome = "Lata de Atum natural",
                tipo = "Alimentar",
                categoria = "Enlatados",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Arroz 1Kg",
                tipo = "Alimentar",
                categoria = "Arroz",
                validade = dataValidadeExemplo ,
                estadoProduto = "Desativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Lixivia 1L",
                tipo = "Limpeza",
                categoria = "Lixivia",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            ),
            Produto(
                idProduto = "250520251",
                campanha = "Teste",
                nome = "Sabão líquido 1L",
                tipo = "Higiene Pessoal",
                categoria = "Sabão",
                validade = dataValidadeExemplo ,
                estadoProduto = "Ativo",
                dataEntrada = dataExemplo,
                responsavel = "Teste",
            )
        ),
        onBack = {}
    )
}

