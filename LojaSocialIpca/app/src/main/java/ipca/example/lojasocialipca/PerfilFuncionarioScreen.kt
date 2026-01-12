package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun PerfilFuncionarioScreen(
    nome: String,
    email: String,
    alertas: List<String>,
    onMudarPassword: () -> Unit = {}
) {
    var mostrarDialogMudarPassword by remember { mutableStateOf(false) }

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
            Text(
                text = "Loja Social",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "IPCA",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // HEADER: AVATAR + INFO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar quadrado com borda verde
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF006837)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    // Avatar simples (círculo + "pescoço")
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(36.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.LightGray)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Nome: $nome",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Email: $email",
                    fontSize = 22.sp
                )
            }
        }

        // ALERTAS
        Text(
            text = "Alertas",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 24.dp, top = 32.dp, bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF2F2F2))
                .padding(vertical = 8.dp)
        ) {
            alertas.forEach { alerta ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .border(
                            width = 2.dp,
                            color = Color(0xFF006837),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF008000))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = alerta,
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTÃO MUDAR PALAVRA-PASSE
        Button(
            onClick = {
                mostrarDialogMudarPassword = true
                onMudarPassword()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .height(72.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006837),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Mudar Palavra-passe",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // DIALOG (POP-UP) PARA MUDAR PALAVRA-PASSE
    if (mostrarDialogMudarPassword) {
        Dialog(
            onDismissRequest = { mostrarDialogMudarPassword = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                ) {
                    // Título
                    Text(
                        text = "Mudar Palavra-passe",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006837),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Campo: Palavra-Passe Antiga
                    Text(
                        text = "Palavra-Passe Antiga:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = { /* TODO: Implementar lógica */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    // Campo: Palavra-Passe Nova
                    Text(
                        text = "Palavra-Passe Nova:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = { /* TODO: Implementar lógica */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    // Botão Confirmar
                    Button(
                        onClick = {
                            // TODO: Implementar lógica de mudança de senha
                            mostrarDialogMudarPassword = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF006837),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Mudar Palavra-passe",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PerfilFuncionarioScreenPreview() {
    PerfilFuncionarioScreen(
        nome = "Jorge Alves",
        email = "ja@ipca.pt",
        alertas = listOf(
            "Produto disponível: Arroz",
            "Promoção especial esta semana",
            "Novo horário de funcionamento"
        )
    )
}