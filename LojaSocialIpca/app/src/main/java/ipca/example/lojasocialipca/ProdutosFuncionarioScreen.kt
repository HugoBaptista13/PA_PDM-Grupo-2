package ipca.example.lojasocialipca

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme

data class Produto(
    val nome: String,
    val stock: Int,
    val detalhes: List<ProdutoDetalhe> = emptyList()
)

data class ProdutoDetalhe(
    val id: String,
    val validade: String,
    val entrada: String
)

@Composable
fun ProdutosFuncionarioScreen(
    onBack: () -> Unit = {}
) {
    // Estados de expansão separados para cada categoria
    var expandedAlimentar by remember { mutableStateOf(false) }
    var expandedHigiene by remember { mutableStateOf(false) }
    var expandedLimpeza by remember { mutableStateOf(false) }

    // Estados de produto selecionado para cada categoria
    var produtoAlimentarSelecionado by remember { mutableStateOf<Produto?>(null) }
    var produtoHigieneSelecionado by remember { mutableStateOf<Produto?>(null) }
    var produtoLimpezaSelecionado by remember { mutableStateOf<Produto?>(null) }

    val produtosAlimentares = listOf(
        Produto(
            nome = "Arroz",
            stock = 3,
            detalhes = listOf(
                ProdutoDetalhe("ID-25052026-ARROZ 1Kg", "25/05/2026", "18/12/2025"),
                ProdutoDetalhe("ID-25052027-ARROZ 1Kg", "25/05/2027", "18/12/2025"),
                ProdutoDetalhe("ID-25052028-ARROZ 1Kg", "25/05/2028", "18/12/2025")
            )
        ),
        Produto(
            nome = "Enlatados",
            stock = 3,
            detalhes = listOf(
                ProdutoDetalhe("ID-25052026-ENLATADOS 1Kg", "25/05/2026", "18/12/2025"),
                ProdutoDetalhe("ID-25052027-ENLATADOS 1Kg", "25/05/2027", "18/12/2025"),
                ProdutoDetalhe("ID-25052028-ENLATADOS 1Kg", "25/05/2028", "18/12/2025")
            )
        ),
        Produto(
            nome = "Massa",
            stock = 3,
            detalhes = listOf(
                ProdutoDetalhe("ID-25052026-MASSA 1Kg", "25/05/2026", "18/12/2025"),
                ProdutoDetalhe("ID-25052027-MASSA 1Kg", "25/05/2027", "18/12/2025"),
                ProdutoDetalhe("ID-25052028-MASSA 1Kg", "25/05/2028", "18/12/2025")
            )
        )
    )

    val produtosHigiene = listOf(
        Produto(
            nome = "Sabão",
            stock = 56,
            detalhes = listOf(
                ProdutoDetalhe("ID-05012026-SABAO 500g", "05/01/2026", "01/12/2025"),
                ProdutoDetalhe("ID-05012027-SABAO 500g", "05/01/2027", "01/12/2025"),
                ProdutoDetalhe("ID-05012028-SABAO 500g", "05/01/2028", "01/12/2025")
            )
        ),
        Produto(
            nome = "Champô",
            stock = 30,
            detalhes = listOf(
                ProdutoDetalhe("ID-15012026-CHAMPO 300ml", "15/01/2026", "10/12/2025"),
                ProdutoDetalhe("ID-15012027-CHAMPO 300ml", "15/01/2027", "10/12/2025")
            )
        ),
        Produto(
            nome = "Gilete",
            stock = 12,
            detalhes = listOf(
                ProdutoDetalhe("ID-20012026-GILETE 5unid", "20/01/2026", "15/12/2025"),
                ProdutoDetalhe("ID-20012027-GILETE 5unid", "20/01/2027", "15/12/2025")
            )
        ),
        Produto(
            nome = "Creme Dental",
            stock = 40,
            detalhes = listOf(
                ProdutoDetalhe("ID-01022026-CREME 75ml", "01/02/2026", "20/12/2025"),
                ProdutoDetalhe("ID-01022027-CREME 75ml", "01/02/2027", "20/12/2025")
            )
        )
    )

    val produtosLimpeza = listOf(
        Produto(
            nome = "Detergente Roupa",
            stock = 10,
            detalhes = listOf(
                ProdutoDetalhe("ID-10012026-DETROUPA 2L", "10/01/2026", "05/12/2025"),
                ProdutoDetalhe("ID-10012027-DETROUPA 2L", "10/01/2027", "05/12/2025")
            )
        ),
        Produto(
            nome = "Detergente Loiça",
            stock = 15,
            detalhes = listOf(
                ProdutoDetalhe("ID-12012026-DETLOICA 1L", "12/01/2026", "08/12/2025"),
                ProdutoDetalhe("ID-12012027-DETLOICA 1L", "12/01/2027", "08/12/2025"),
                ProdutoDetalhe("ID-12012028-DETLOICA 1L", "12/01/2028", "08/12/2025")
            )
        ),
        Produto(
            nome = "Detergente Lava Tudo",
            stock = 8,
            detalhes = listOf(
                ProdutoDetalhe("ID-18012026-LAVATUDO 750ml", "18/01/2026", "12/12/2025"),
                ProdutoDetalhe("ID-18012027-LAVATUDO 750ml", "18/01/2027", "12/12/2025")
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(onBack)

        Text(
            text = "Produtos",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // CATEGORIA ALIMENTARES
            CategoriaLinha(
                titulo = "Produtos Alimentares",
                expanded = expandedAlimentar,
                onClick = {
                    expandedAlimentar = !expandedAlimentar
                    if (!expandedAlimentar) produtoAlimentarSelecionado = null
                }
            )
            if (expandedAlimentar) {
                CategoriaProdutos(
                    produtos = produtosAlimentares,
                    produtoSelecionado = produtoAlimentarSelecionado,
                    onProdutoClick = { produto ->
                        produtoAlimentarSelecionado =
                            if (produtoAlimentarSelecionado?.nome == produto.nome) null else produto
                    }
                )
            }

            // CATEGORIA HIGIENE
            CategoriaLinha(
                titulo = "Produtos de Higiene Pessoal",
                expanded = expandedHigiene,
                onClick = {
                    expandedHigiene = !expandedHigiene
                    if (!expandedHigiene) produtoHigieneSelecionado = null
                }
            )
            if (expandedHigiene) {
                CategoriaProdutos(
                    produtos = produtosHigiene,
                    produtoSelecionado = produtoHigieneSelecionado,
                    onProdutoClick = { produto ->
                        produtoHigieneSelecionado =
                            if (produtoHigieneSelecionado?.nome == produto.nome) null else produto
                    }
                )
            }

            // CATEGORIA LIMPEZA
            CategoriaLinha(
                titulo = "Produtos de Limpeza",
                expanded = expandedLimpeza,
                onClick = {
                    expandedLimpeza = !expandedLimpeza
                    if (!expandedLimpeza) produtoLimpezaSelecionado = null
                }
            )
            if (expandedLimpeza) {
                CategoriaProdutos(
                    produtos = produtosLimpeza,
                    produtoSelecionado = produtoLimpezaSelecionado,
                    onProdutoClick = { produto ->
                        produtoLimpezaSelecionado =
                            if (produtoLimpezaSelecionado?.nome == produto.nome) null else produto
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        ButtonInserirProduto()
    }
}

@Composable
fun CategoriaLinha(
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
                imageVector = if (expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun CategoriaProdutos(
    produtos: List<Produto>,
    produtoSelecionado: Produto?,
    onProdutoClick: (Produto) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        produtos.forEach { produto ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProdutoClick(produto) },
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(produto.nome, fontWeight = FontWeight.Medium)
                        Text("Stock: ${produto.stock}", fontSize = 12.sp)
                    }
                    if (produto.detalhes.isNotEmpty()) {
                        Icon(
                            imageVector = if (produtoSelecionado?.nome == produto.nome)
                                Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }

            if (produtoSelecionado?.nome == produto.nome && produto.detalhes.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    produto.detalhes.forEach { det ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(det.id, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Validade: ${det.validade}", fontSize = 12.sp)
                                Text("Entrada: ${det.entrada}", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
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
                Icon(Icons.Filled.ArrowBack, "Voltar", tint = Color.White)
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
    LojaSocialIpcaTheme {
        ProdutosFuncionarioScreen()
    }
}



