package ipca.example.lojasocialipca.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.models.Beneficiario
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onBackToLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = AppModule.auth
    val firestore = AppModule.firestore

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) } // indica se o processo está em andamento

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF006837)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "IPCA",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 24.dp, horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (loading) return@Button // impede múltiplos cliques

                        // Validações básicas
                        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (password.length < 6) {
                            Toast.makeText(context, "A password deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (password != confirmPassword) {
                            Toast.makeText(context, "As passwords não coincidem", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        loading = true

                        // Verificar se já existe beneficiário com este email
                        firestore.collection("beneficiarios")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                if (!snapshot.isEmpty) {
                                    Toast.makeText(context, "Já existe um beneficiário com este email", Toast.LENGTH_SHORT).show()
                                    loading = false
                                    return@addOnSuccessListener
                                }

                                // Criar utilizador no Firebase Auth
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnSuccessListener { authResult ->
                                        val uid = authResult.user?.uid
                                        if (uid != null) {
                                            val beneficiario = Beneficiario(
                                                idBeneficiario = uid,
                                                email = email
                                            )

                                            firestore.collection("beneficiarios")
                                                .document(uid)
                                                .set(beneficiario)
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                                                    loading = false
                                                    onRegisterSuccess()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(context, "Erro ao criar beneficiário: ${e.message}", Toast.LENGTH_LONG).show()
                                                    loading = false
                                                }
                                        } else {
                                            Toast.makeText(context, "Erro: UID não encontrado", Toast.LENGTH_SHORT).show()
                                            loading = false
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Erro ao registar: ${e.message}", Toast.LENGTH_LONG).show()
                                        loading = false
                                    }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Erro ao verificar beneficiário: ${e.message}", Toast.LENGTH_LONG).show()
                                loading = false
                            }

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                    enabled = !loading
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Registar")
                    }
                }

                TextButton(
                    onClick = { if (!loading) onBackToLogin() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Já tenho conta (Login)")
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    LojaSocialIpcaTheme {
        RegisterScreen()
    }
}
