package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

@Composable
fun ComboBox(
    label: String,
    selected: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onExpandedChange(!expanded) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$label: $selected", fontSize = 16.sp)
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }

    if (expanded) {
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))) {
            Column {
                options.forEach { option ->
                    Text(
                        text = option,
                        modifier = Modifier
                            .clickable {
                                onSelect(option)
                                onExpandedChange(false)
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InserirProdutoScreen() {
    var nomeProduto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf(1) }

    // Estados para ComboBox TIPO (agora começa vazio)
    var tipoSelecionado by remember { mutableStateOf("") }
    var tipoExpanded by remember { mutableStateOf(false) }
    val tipos = listOf("Produto Alimentar", "Produto Higiene", "Produto Limpeza")

    // Estados para ComboBox CAMPANHA
    var campanhaSelecionada by remember { mutableStateOf("") }
    var campanhaExpanded by remember { mutableStateOf(false) }
    val campanhas = listOf("Campanha1", "Campanha2")

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

            // TIPO - ComboBox (agora com "Selecione...")
            ComboBox(
                label = "Tipo",
                selected = if (tipoSelecionado.isEmpty()) "Selecione..." else tipoSelecionado,
                options = tipos,
                expanded = tipoExpanded,
                onExpandedChange = { tipoExpanded = it },
                onSelect = { tipoSelecionado = it }
            )

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

            // CATEGORIA
            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria:") },
                modifier = Modifier.fillMaxWidth()
            )

            // CAMPANHA - ComboBox
            ComboBox(
                label = "Campanha",
                selected = if (campanhaSelecionada.isEmpty()) "Selecione..." else campanhaSelecionada,
                options = campanhas,
                expanded = campanhaExpanded,
                onExpandedChange = { campanhaExpanded = it },
                onSelect = { campanhaSelecionada = it }
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

        // BOTTOM NAV
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFF006837)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Person, contentDescription = "Perfil", tint = Color.White, modifier = Modifier.size(28.dp))
            Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White, modifier = Modifier.size(28.dp))
            Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.White, modifier = Modifier.size(28.dp))
        }
    }
}

@Preview(showBackground = true,)
@Composable
fun PreviewInserirProdutoSimples() {
    MaterialTheme {
        InserirProdutoScreen()
    }
}





