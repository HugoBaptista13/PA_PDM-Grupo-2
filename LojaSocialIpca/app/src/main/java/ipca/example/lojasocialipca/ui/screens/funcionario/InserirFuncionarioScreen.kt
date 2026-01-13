package ipca.example.lojasocialipca.ui.screens.funcionario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserirFuncionarioScreen(
    onBack: () -> Unit = {},
    onCriarFuncionario: () -> Unit = {}
) {
    var nome by remember {mutableStateOf("")}
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
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
            text = "Inserir Funcionário",
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
                Text("Nome", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 8.dp))
                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                        erro = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erro && nome.isBlank()) "Preenche o nome do funcionário"
                            else "Nome do funcionário"
                        )
                    },
                    isError = erro && nome.isBlank()
                )

                Text("Email", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        erro = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erro && email.isBlank()) "Preenche o email do funcionário"
                            else "Email do funcionário"
                        )
                    },
                    isError = erro && email.isBlank()
                )

                Text("Password", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        erro = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erro && password.isBlank()) "Preenche o password do funcionário"
                            else "Password do funcionário"
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = erro && password.isBlank()
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

        // BOTÃO CRIAR FUNCIONARIO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val valido =
                        nome.isNotBlank() && email.isNotBlank() && password.isNotBlank()
                    if (valido) {
                        onCriarFuncionario()
                    } else {
                        erro = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Criar Funcionário")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InserirFuncionarioScreenPreview() {
    InserirFuncionarioScreen(
        onBack = {},
        onCriarFuncionario = {}
    )
}