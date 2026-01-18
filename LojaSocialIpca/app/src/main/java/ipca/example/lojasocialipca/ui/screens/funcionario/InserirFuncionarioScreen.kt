package ipca.example.lojasocialipca.ui.screens.funcionario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.ui.components.TopBar

@Composable
fun InserirFuncionarioScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // Loading state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(true, onBack)

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = "Inserir Funcionário",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome do funcionário") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email do funcionário") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password do funcionário") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Validação
                    if (nome.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(
                            context,
                            "Preencha todos os campos obrigatórios",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    // Validar email
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "Insira um email válido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Validar password (mínimo 6 caracteres)
                    if (password.length < 6) {
                        Toast.makeText(
                            context,
                            "A password deve ter pelo menos 6 caracteres",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    isLoading = true // Mostra loading

                    // Criar utilizador no Firebase Auth
                    AppModule.auth.createUserWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid ?: run {
                                isLoading = false
                                return@addOnSuccessListener
                            }

                            // Adicionar funcionário no Firestore
                            val funcionario = hashMapOf(
                                "idFuncionario" to uid,
                                "nome" to nome.trim(),
                                "email" to email.trim()
                            )

                            AppModule.firestore.collection("funcionarios")
                                .document(uid)
                                .set(funcionario)
                                .addOnSuccessListener {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Funcionário criado com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onBack()
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Erro ao adicionar funcionário: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            val msg = when {
                                e.message?.contains("email address is already in use") == true ->
                                    "Este email já está registado"

                                else -> "Erro ao criar utilizador: ${e.message}"
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Criar Funcionário",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        // Overlay de loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun InserirFuncionarioScreenPreview() {
    InserirFuncionarioScreen(
        onBack = {},
    )
}