package ipca.example.lojasocialipca.ui.screens.funcionario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.EmailAuthProvider
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.models.Funcionario
import ipca.example.lojasocialipca.models.Notificacao
import ipca.example.lojasocialipca.ui.components.BottomBar
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

@Composable
fun PerfilFuncionarioScreen(
    onLogout: () -> Unit = {},
    onHome: () -> Unit = {}
) {
    val firestore = AppModule.firestore
    val auth = AppModule.auth

    var funcionario by remember { mutableStateOf<Funcionario?>(null) }
    var alertas by remember { mutableStateOf<List<Notificacao>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Buscar dados do funcionário logado
    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            // Listener para o funcionário
            firestore.collection("funcionarios")
                .document(uid)
                .addSnapshotListener { snapshot, error ->
                    if (snapshot != null && snapshot.exists()) {
                        funcionario = Funcionario(
                            idFuncionario = snapshot.id,
                            nome = snapshot.getString("nome") ?: "",
                            email = snapshot.getString("email") ?: ""
                        )
                    }
                }

            // Listener para alertas do funcionário
            firestore.collection("notificacoes")
                .whereEqualTo("destinatario", uid)
                .addSnapshotListener { snapshot, error ->
                    if (snapshot != null) {
                        alertas = snapshot.documents.mapNotNull { doc ->
                            Notificacao(
                                id = doc.id,
                                mensagem = doc.getString("mensagem") ?: "",
                                dataEnvio = doc.getTimestamp("data")?.toDate() ?: Date()
                            )
                        }.sortedByDescending { it.dataEnvio }
                    }
                    loading = false
                }
        } else {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(false)

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF006837))
            }
        } else {
            funcionario?.let { func ->
                PerfilFuncionarioCard(
                    funcionario = func,
                    alertas = alertas
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomBar(onLogout = onLogout, onHome = onHome)
    }
}

@Composable
fun PerfilFuncionarioCard(
    funcionario: Funcionario,
    alertas: List<Notificacao>,
) {
    var mostrarDialogMudarPassword by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val letraInicial = funcionario.nome.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // CABEÇALHO AVATAR + INFO
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF006837)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letraInicial,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(funcionario.nome, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Email: ${funcionario.email}", fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            // BOTÃO MUDAR PALAVRA-PASSE
            Button(
                onClick = { mostrarDialogMudarPassword = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837))
            ) {
                Text(
                    "Mudar Palavra-Passe",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            // LISTA DE ALERTAS COM ALTURA FIXA
            Text(
                text = "Alertas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF006837),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (alertas.isEmpty()) {
                Text("Nenhum alerta.", fontSize = 14.sp, color = Color.Gray)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // altura fixa para scroll
                        .background(Color(0xFFF2F2F2))
                        .clip(RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(alertas) { alerta ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF006837))
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = alerta.mensagem,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIÁLOGO MUDAR SENHA
    if (mostrarDialogMudarPassword) {
        Dialog(onDismissRequest = { mostrarDialogMudarPassword = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Mudar Palavra-Passe", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006837))

                    var oldPassword by remember { mutableStateOf("") }
                    var newPassword by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Palavra-Passe Antiga") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Palavra-Passe Nova") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Button(
                        onClick = {
                            if (newPassword.length < 6) {
                                Toast.makeText(
                                    context,
                                    "A palavra-passe deve ter pelo menos 6 caracteres",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            val user = AppModule.auth.currentUser
                            if (user != null && oldPassword.isNotBlank()) {
                                val credential = EmailAuthProvider.getCredential(user.email ?: "", oldPassword)

                                // Reautenticar antes de alterar a password
                                user.reauthenticate(credential).addOnCompleteListener { authTask ->
                                    if (authTask.isSuccessful) {
                                        user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                Toast.makeText(context, "Palavra-Passe alterada com sucesso!", Toast.LENGTH_SHORT).show()
                                                mostrarDialogMudarPassword = false
                                            } else {
                                                Toast.makeText(context, "Erro ao alterar palavra-passe: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "Senha antiga incorreta", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Usuário não autenticado ou senha antiga vazia", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Confirmar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun PerfilFuncionarioScreenPreview() {
    PerfilFuncionarioCard(
        Funcionario(idFuncionario = "asdsada", nome = "Jorge Alves", email = "jalves@ipca.pt"),
        alertas = listOf(
            Notificacao(
                mensagem = "15 dias para produto expirar",
                resolvida = false
            ),
            Notificacao(
                mensagem = "15 dias para produto expirar",
                resolvida = false
            ),
            Notificacao(
                mensagem = "1 dia para entrega",
                resolvida = true
            ),Notificacao(
                mensagem = "2 dias para produto expirar",
                resolvida = true
            ),
            Notificacao(
                mensagem = "15 dias para produto expirar",
                resolvida = false
            )
        )
    )
}

