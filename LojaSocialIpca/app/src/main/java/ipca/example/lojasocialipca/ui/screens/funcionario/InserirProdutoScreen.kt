package ipca.example.lojasocialipca.ui.screens.funcionario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ipca.example.lojasocialipca.models.Campanha
import ipca.example.lojasocialipca.models.TipoProduto
import ipca.example.lojasocialipca.ui.components.ComboBox
import java.util.Date

@Composable
fun InserirProdutoScreen(
    tipos: List<TipoProduto>,
    campanhas: List<Campanha>
) {
    var nomeProduto by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf<TipoProduto?>(null) }
    var categoria by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var campanha by remember { mutableStateOf<Campanha?>(null) }
    var quantidade by remember { mutableStateOf(1) }
    var mostrarDialog by remember { mutableStateOf(false) }



    //val tipos = listOf("Produto Alimentar", "Produto Higiene", "Produto Limpeza")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Loja Social", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notificações", tint = Color.White)
                Text("IPCA", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Inserir Produto", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            // NOME
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

                    // BOTÃO +
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF006837), CircleShape)
                            .clickable { mostrarDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar categoria",
                            tint = Color.White
                        )
                    }
                }
            }

            // DATA DE VALIDADE
            Text("Data de Validade:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = dia,
                    onValueChange = { dia = it },
                    label = { Text("Dia") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = mes,
                    onValueChange = { mes = it },
                    label = { Text("Mês") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = ano,
                    onValueChange = { ano = it },
                    label = { Text("Ano") },
                    modifier = Modifier.weight(1f)
                )
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
                OutlinedTextField(
                    value = quantidade.toString(),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.width(100.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .clickable { quantidade++ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .clickable { if (quantidade > 1) quantidade-- },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("–", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // BOTÕES
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF037C49)),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Inserir", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9F0D00)),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        if (mostrarDialog && tipo != null) {
            AdicionarCategoriaDialog(
                onDismiss = { mostrarDialog = false },
                onCategoriaAdicionar = { novaCategoria ->
                    if (!tipo!!.categorias.contains(novaCategoria)) {
                        tipo!!.categorias.add(novaCategoria)
                        categoria = novaCategoria
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
        InserirProdutoScreen(
            tipos = listOf(
                TipoProduto(
                    tipo = "Produto Alimentar",
                    categorias = listOf("Arroz", "Massa", "Enlatados", "Azeite") as MutableList<String>
                ),
                TipoProduto(
                    tipo = "Produto de Higiene Pessoal",
                    categorias = listOf("Sabonete", "Champô", "Pasta de dentes", "Escovas de dentes") as MutableList<String>
                ),
                TipoProduto(
                    tipo = "Produto de Limpeza",
                    categorias = listOf("Líxivia", "Tira-gorduras", "Panos") as MutableList<String>
                )
            ),
            campanhas = listOf(
                Campanha(
                    nome = "Campanha 1",
                    dataInicio = Date(),
                    dataFim = null,
                    descricao = "",
                    tipo = "Externa",
                    concluida = false,
                    responsavel = ""
                ),
                Campanha(
                    nome = "Campanha 2",
                    dataInicio = Date(),
                    dataFim = null,
                    descricao = "",
                    tipo = "Externa",
                    concluida = false,
                    responsavel = ""
                )
            )
        )
    }
}





