package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserirCampanhaScreen(
    onBack: () -> Unit = {},
    onCriarCampanha: () -> Unit = {}
) {
    var nome by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Campanha Interna") }
    var tipoExpanded by remember { mutableStateOf(false) }
    val tipos = listOf("Campanha Interna", "Campanha Externa")
    var diaInicio by remember { mutableStateOf("") }
    var mesInicio by remember { mutableStateOf("") }
    var anoInicio by remember { mutableStateOf("") }
    var diaFim by remember { mutableStateOf("") }
    var mesFim by remember { mutableStateOf("") }
    var anoFim by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    // erros simples
    var erro by remember { mutableStateOf(false) }

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
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, "Voltar", tint = Color.White)
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
            text = "Inserir Campanha",
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
            // Nome
            Column {
                Text("Nome", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                        erro = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erro && nome.isBlank()) "Preenche o nome da campanha"
                            else "Nome da campanha"
                        )
                    },
                    isError = erro && nome.isBlank()
                )
            }

            // Tipo (simples: duas opções fixas)

            val tipos = listOf("Interna", "Externa")

            ComboBox(
                label = "Tipo",
                selected = "Interna",
                options = tipos,
                onSelect = {},
                modifier = Modifier
            )

            // Data de início
            Text(
                "Data de início",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = diaInicio,
                    onValueChange = {
                        diaInicio = it
                        erro = false
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Dia") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = mesInicio,
                    onValueChange = {
                        mesInicio = it
                        erro = false
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Mês") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = anoInicio,
                    onValueChange = {
                        anoInicio = it
                        erro = false
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Ano") },
                    singleLine = true
                )
            }

            // Data de fim
            Text(
                "Data de fim",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = diaFim,
                    onValueChange = {
                        diaFim = it
                        erro = false
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Dia") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = mesFim,
                    onValueChange = {
                        mesFim = it
                        erro = false
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Mês") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = anoFim,
                    onValueChange = {
                        anoFim = it
                        erro = false
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Ano") },
                    singleLine = true
                )
            }

            // Descrição
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
                        .height(120.dp),
                    label = {
                        Text(
                            if (erro && descricao.isBlank()) "Preenche a descrição"
                            else "Descrição da campanha"
                        )
                    },
                    isError = erro && descricao.isBlank(),
                    singleLine = false,
                    maxLines = 5
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

        // BOTÃO CRIAR CAMPANHA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val valido =
                        nome.isNotBlank() && descricao.isNotBlank()
                    if (valido) {
                        onCriarCampanha()
                    } else {
                        erro = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Criar Campanha")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InserirCampanhaPreview() {
    InserirCampanhaScreen ()
}
