package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.ui.components.ComboBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserirPedidoScreen(
    onBack: () -> Unit = {},
    onInserirPedido: () -> Unit = {}
) {
    var tipo by remember { mutableStateOf("Produto Alimentar") }
    var tipoExpanded by remember { mutableStateOf(false) }
    val tipos = listOf("Produto Alimentar", "Produto Higiene", "Produto Limpeza")
    var descricao by remember { mutableStateOf("") }

    // erro simples
    var erro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR (igual ao InserirCampanha)
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
                    "Loja Social",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Notifications, "Notificações", tint = Color.White)
                }
                Text("IPCA", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        // TÍTULO
        Text(
            text = "Inserir Pedido",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        // FORMULÁRIO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ComboBox(
                label = "Tipo",
                selected = tipo,
                options = tipos,
                onSelect = { tipo = it },
                modifier = Modifier
            )

            // Descrição do produto
            Column {
                Text("Descrição", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = descricao,
                    onValueChange = {
                        descricao = it
                        erro = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    label = {
                        Text(
                            if (erro && descricao.isBlank()) "Preenche a descrição"
                            else "Descrição do pedido"
                        )
                    },
                    isError = erro && descricao.isBlank(),
                    singleLine = false,
                    maxLines = 6
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ALERTA VERMELHO
        if (erro) {
            Text(
                text = "Campos obrigatórios em falta",
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // BOTÃO INSERIR PRODUTO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val valido = descricao.isNotBlank()
                    if (valido) {
                        onInserirPedido()
                    } else {
                        erro = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Inserir Pedido")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InserirPedidoScreenPreview() {
    InserirPedidoScreen()
}
