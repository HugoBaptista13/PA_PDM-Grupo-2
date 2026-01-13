package ipca.example.lojasocialipca.ui.screens.funcionario

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.R
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Produto
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme
import java.util.Calendar

const val ESTADO_ATIVO = "Ativo"

fun List<Produto>.groupByTipoECategoria():
        Map<String, Map<String, List<Produto>>> =
    this.groupBy { it.tipo }
        .mapValues { (_, produtosDoTipo) ->
            produtosDoTipo.groupBy { it.categoria }
        }

@Composable
fun ProdutosFuncionarioScreen(
    produtos: List<Produto>,
    onBack: () -> Unit = {}
) {
    val produtosAtivos = remember(produtos) {
        produtos.filter { it.estadoProduto == ESTADO_ATIVO }
    }

    val produtosPorTipo = remember(produtosAtivos) {
        produtosAtivos.groupByTipoECategoria()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        item { TopBar(onBack) }

        item {
            Text(
                text = "Produtos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        produtosPorTipo.forEach { (tipo, categorias) ->
            item {
                TipoExpandable(
                    tipo = tipo,
                    categorias = categorias
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            ButtonInserirProduto()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProdutoItemPreview() {
    val dataExemplo = criarData(2025, Calendar.MAY, 25)
    val dataValidadeExemplo = criarData(2025, Calendar.DECEMBER, 18)
    ProdutoItem(
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
        )
    )
}

@Composable
fun TipoExpandable(
    tipo: String,
    categorias: Map<String, List<Produto>>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        TipoLinha(
            titulo = tipo,
            expanded = expanded,
            onClick = { expanded = !expanded }
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (expanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                categorias.forEach { (categoria, produtos) ->
                    CategoriaExpandable(categoria, produtos)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CategoriaExpandable(
    categoria: String,
    produtos: List<Produto>
) {
    var expanded by remember { mutableStateOf(false) }
    val stockTotal = produtos.size

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = categoria,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Stock: $stockTotal",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }

                Icon(
                    imageVector = if (expanded)
                        Icons.Filled.KeyboardArrowDown
                    else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    produtos.forEach { produto ->
                        ProdutoItem(produto)
                    }
                }
            }
        }
    }
}

@Composable
fun ProdutoItem(
    produto: Produto,
    onRemover: (Produto) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color(0xFF00361F),
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "ID-${produto.idProduto}-${produto.nome}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Text(
                    "Data de validade: ${produto.validade.format()}",
                    fontSize = 12.sp
                )
                Text(
                    "Data de entrada: ${produto.dataEntrada.format()}",
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = { onRemover(produto) },
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
                    .background(
                        color = Color.Red,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFF000000),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remover produto",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun TipoLinha(
    titulo: String,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(titulo, fontWeight = FontWeight.SemiBold)
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF006837))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
            }
            Text(
                text = "Loja Social",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text("IPCA", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ButtonInserirProduto() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /* TODO: navega para ecrã Inserir Produto */ },
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_mais),
                contentDescription = "mais",
                modifier = Modifier
                    .height(64.dp)   // ajusta se quiseres maior
            )
            Text("Inserir Produto", color = Color.White, fontWeight = FontWeight.Bold)

        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProdutosFuncionarioScreen() {
    val dataExemplo = criarData(2025, Calendar.MAY, 25)
    val dataValidadeExemplo = criarData(2025, Calendar.DECEMBER, 18)

    LojaSocialIpcaTheme {
        ProdutosFuncionarioScreen(
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
}



