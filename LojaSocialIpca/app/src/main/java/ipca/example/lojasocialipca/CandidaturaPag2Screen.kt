package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CandidaturaPag2Screen(
    onBack: () -> Unit = {},
    onSeguinte: () -> Unit = {}
) {
    val grau = "Licenciatura"
    var curso by remember { mutableStateOf("") }
    var numeroAluno by remember { mutableStateOf("") }
    var alimentar by remember { mutableStateOf(false) }
    var higiene by remember { mutableStateOf(false) }
    var limpeza by remember { mutableStateOf(false) }
    var erroValidacao by remember { mutableStateOf(false) }

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
                Text("Loja Social", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Notifications, "Notificações", tint = Color.White)
                }
                Text("IPCA", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        // TÍTULO
        Text(
            text = "Dados académicos",
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
            // Grau
            Column {
                Text("Grau", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = grau,
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    label = { Text("Grau académico") }
                )
            }

            // Curso COM ERRO NA CAIXA
            Column {
                Text("Nome do curso", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = curso,
                    onValueChange = {
                        curso = it
                        erroValidacao = false  // limpa erro ao escrever
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erroValidacao && curso.isEmpty()) "Preenche o curso"
                            else "Indica o teu curso"
                        )
                    },
                    isError = erroValidacao && curso.isEmpty()
                )
            }

// Nº Aluno COM ERRO NA CAIXA
            Column {
                Text("Nº de aluno", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = numeroAluno,
                    onValueChange = {
                        numeroAluno = it
                        erroValidacao = false  // limpa erro ao escrever
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erroValidacao && numeroAluno.isEmpty()) "Preenche o nº aluno"
                            else "Número de aluno"
                        )
                    },
                    isError = erroValidacao && numeroAluno.isEmpty()
                )
            }


            // Tipologia (MAIOR como mockup)
            Text(
                "Tipologia do pedido",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Produtos alimentares", fontSize = 15.sp)
                    Checkbox(checked = alimentar, onCheckedChange = { alimentar = it })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Produtos de higiene pessoal", fontSize = 15.sp)
                    Checkbox(checked = higiene, onCheckedChange = { higiene = it })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Produtos de limpeza", fontSize = 15.sp)
                    Checkbox(checked = limpeza, onCheckedChange = { limpeza = it })
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ALERTA VERMELHO
        if (erroValidacao) {
            Text(
                text = "Campos não preenchidos",
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // BARRA DE PROGRESSO (2/5)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF006837))
            )
            Box(
                modifier = Modifier
                    .weight(3f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFE0E0E0))
            )
        }

        // BOTÃO SEGUINTE (com validação)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (curso.isNotEmpty() || numeroAluno.isNotEmpty()) {
                        onSeguinte()
                    } else {
                        erroValidacao = true  // ativa erro nas caixas
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Seguinte")
            }
        }
    }
}





