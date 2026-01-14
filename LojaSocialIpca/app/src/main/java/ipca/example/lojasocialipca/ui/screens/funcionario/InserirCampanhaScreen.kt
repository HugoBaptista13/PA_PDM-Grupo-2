package ipca.example.lojasocialipca.ui.screens.funcionario

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.ui.components.ComboBox
import ipca.example.lojasocialipca.ui.components.TopBar

@Composable
fun InserirCampanhaScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var nome by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    val tipos = listOf("Campanha Interna", "Campanha Externa")
    var diaInicio by remember { mutableStateOf("") }
    var mesInicio by remember { mutableStateOf("") }
    var anoInicio by remember { mutableStateOf("") }
    var diaFim by remember { mutableStateOf("") }
    var mesFim by remember { mutableStateOf("") }
    var anoFim by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

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
                text = "Inserir Campanha",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da campanha") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tipo
            ComboBox(
                label = "Tipo",
                selected = tipo,
                options = tipos,
                onSelect = { tipo = it },
                modifier = Modifier.fillMaxWidth()
            )

            // Data de início
            Text("Data de início")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = diaInicio, onValueChange = { diaInicio = it }, label = { Text("Dia") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = mesInicio, onValueChange = { mesInicio = it }, label = { Text("Mês") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = anoInicio, onValueChange = { anoInicio = it }, label = { Text("Ano") }, modifier = Modifier.weight(1f))
            }

            // Data de fim
            Text("Data de fim")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = diaFim, onValueChange = { diaFim = it }, label = { Text("Dia") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = mesFim, onValueChange = { mesFim = it }, label = { Text("Mês") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = anoFim, onValueChange = { anoFim = it }, label = { Text("Ano") }, modifier = Modifier.weight(1f))
            }

            // Descrição
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição da campanha") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Criar
            Button(
                onClick = {
                    // Validação de campos obrigatórios
                    if (nome.isBlank() || descricao.isBlank() || tipo.isBlank() ||
                        diaInicio.isBlank() || mesInicio.isBlank() || anoInicio.isBlank()
                    ) {
                        Toast.makeText(context, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Tentar criar datas
                    val dataInicio = try { criarData(anoInicio.toInt(), mesInicio.toInt() - 1, diaInicio.toInt()) } catch (e: Exception) {
                        Toast.makeText(context, "Data de início inválida", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val dataFimObj = if (diaFim.isNotBlank() && mesFim.isNotBlank() && anoFim.isNotBlank()) {
                        try { criarData(anoFim.toInt(), mesFim.toInt() - 1, diaFim.toInt()) } catch (e: Exception) {
                            Toast.makeText(context, "Data de fim inválida", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                    } else null

                    // Verificar se já existe campanha com este nome
                    AppModule.firestore.collection("campanhas")
                        .whereEqualTo("nome", nome.trim())
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (!snapshot.isEmpty) {
                                Toast.makeText(context, "Já existe uma campanha com este nome", Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }

                            // Inserir campanha no Firestore
                            val novaCampanha = hashMapOf(
                                "nome" to nome.trim(),
                                "tipo" to tipo,
                                "descricao" to descricao.trim(),
                                "dataInicio" to dataInicio,
                                "dataFim" to dataFimObj,
                                "concluida" to false,
                                "responsavel" to AppModule.auth.currentUser?.uid.orEmpty()
                            )

                            AppModule.firestore.collection("campanhas")
                                .add(novaCampanha)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Campanha criada com sucesso!", Toast.LENGTH_SHORT).show()
                                    onBack()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Erro ao criar campanha: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Erro ao verificar nome: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Criar Campanha", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InserirCampanhaPreview() {
    InserirCampanhaScreen ()
}
