package ipca.example.lojasocialipca.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme


@Composable
fun LoginScreen(
    onLoginSuccess: (uid: String) -> Unit = {},
    onGoToRegister: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = AppModule.auth

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (loading) return@Button

                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        loading = true

                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener { authResult ->
                                val uid = authResult.user?.uid
                                if (uid != null) {
                                    Toast.makeText(context, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()
                                    loading = false
                                    onLoginSuccess(uid) // passa o UID do utilizador logado
                                } else {
                                    Toast.makeText(context, "Erro: UID não encontrado", Toast.LENGTH_SHORT).show()
                                    loading = false
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Erro no login: ${e.message}", Toast.LENGTH_LONG).show()
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
                        Text("Login")
                    }
                }

                TextButton(
                    onClick = { if (!loading) onGoToRegister() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Não tem conta? Registe-se")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LojaSocialIpcaTheme {
        LoginScreen()
    }
}


