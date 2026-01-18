package ipca.example.lojasocialipca.ui.screens.candidato

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.helpers.criarData
import ipca.example.lojasocialipca.models.Candidatura
import ipca.example.lojasocialipca.ui.components.ComboBox
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

@Composable
fun CandidaturaScreen(
    onCandidaturaSuccess: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val firestore = AppModule.firestore
    val auth = AppModule.auth
    val context = LocalContext.current

    var step by remember { mutableStateOf(1) }
    var aSubmeter by remember { mutableStateOf(false) }

    val candidatura = remember { Candidatura() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
        TopBar(true, onBack)

        // CONTEÚDO
        when (step) {
            1 -> CandidaturaDadosPessoais(candidatura)
            2 -> CandidaturaDadosAcademicos(candidatura)
            3 -> CandidaturaApoiosExtras(candidatura)
        }

        Spacer(Modifier.weight(1f))

        // BARRA DE PROGRESSO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .background(
                            color = if (index < step) Color(0xFF006837) else Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(3.dp)
                        )
                )
            }
        }

        // BOTÕES
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (step > 1) {
                OutlinedButton(
                    onClick = { step-- },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Anterior")
                }
            }

            Button(
                onClick = {
                    val toastInvalido: () -> Unit = {
                        Toast.makeText(context, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                    }

                    // Validações de cada etapa
                    when (step) {
                        1 -> {
                            if (candidatura.nome.isBlank() || candidatura.email.isBlank() || candidatura.cartaoCidadao.isBlank() ||
                                candidatura.telemovel.isBlank() || candidatura.anoLetivo.isBlank()
                            ) {
                                toastInvalido()
                                return@Button
                            }
                        }
                        2 -> {
                            if (candidatura.grau.isBlank() || candidatura.curso.isBlank() ||
                                candidatura.numAluno == 0 || candidatura.tipologiaPedido.isEmpty()
                            ) {
                                toastInvalido()
                                return@Button
                            }
                        }
                        3 -> {
                            if (candidatura.bolseiro && candidatura.valorBolsa == null) {
                                Toast.makeText(context, "Indique o valor da bolsa", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                        }
                    }

                    // Passa para a próxima etapa ou submete
                    if (step < 3) {
                        step++
                    } else {
                        // Submeter candidatura
                        aSubmeter = true
                        candidatura.dataSubmissao = Date()
                        candidatura.estadoCandidatura = "FILA_ESPERA"

                        val uid = auth.currentUser?.uid

                        if (uid != null) {
                            firestore.collection("candidaturas")
                                .add(candidatura)
                                .addOnSuccessListener { candidaturaRef ->
                                    firestore.collection("beneficiarios")
                                        .whereEqualTo("idBeneficiario", uid)
                                        .get()
                                        .addOnSuccessListener { snapshot ->
                                            if (!snapshot.isEmpty) {
                                                val doc = snapshot.documents.first()
                                                val beneficiarioRef = doc.reference
                                                val candidaturasAtuais = doc.get("candidaturas") as? List<String> ?: emptyList()
                                                val novaLista = candidaturasAtuais + candidaturaRef.id

                                                beneficiarioRef.update("candidaturas", novaLista)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(context, "Candidatura submetida com sucesso!", Toast.LENGTH_SHORT).show()
                                                        aSubmeter = false
                                                        onCandidaturaSuccess()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(context, "Erro ao atualizar beneficiário", Toast.LENGTH_SHORT).show()
                                                        aSubmeter = false
                                                    }
                                            } else {
                                                Toast.makeText(context, "Beneficiário não encontrado", Toast.LENGTH_SHORT).show()
                                                aSubmeter = false
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Erro ao buscar beneficiário", Toast.LENGTH_SHORT).show()
                                            aSubmeter = false
                                        }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Erro ao submeter candidatura", Toast.LENGTH_SHORT).show()
                                    aSubmeter = false
                                }
                        } else {
                            Toast.makeText(context, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
                            aSubmeter = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                shape = RoundedCornerShape(12.dp),
                enabled = !aSubmeter
            ) {
                if (aSubmeter) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (step < 3) "Seguinte" else "Submeter")
                }
            }
        }
    }
}


@Composable
fun CandidaturaDadosPessoais(c: Candidatura) {
    val anosLetivos = listOf("2025/2026","2026/2027","2027/2028","2028/2029","2029/2030","2030/2031","2031/2032")

    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }

    var anoletivo by rememberSaveable { mutableStateOf(c.anoLetivo) }
    var nome by rememberSaveable { mutableStateOf(c.nome) }
    var cc by rememberSaveable { mutableStateOf(c.cartaoCidadao) }
    var telemovel by rememberSaveable { mutableStateOf(c.telemovel) }
    var email by rememberSaveable { mutableStateOf(c.email) }

    Text(
        "Identificação do candidato",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        ComboBox(
            label = "Ano Letivo",
            selected = anoletivo,
            options = anosLetivos,
            onSelect = {
                anoletivo = it
                c.anoLetivo = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
                c.nome = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nome") }
        )

        OutlinedTextField(
            value = cc,
            onValueChange = {
                cc = it
                c.cartaoCidadao = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Cartão de Cidadão") }
        )

        Text("Data de nascimento", fontWeight = FontWeight.SemiBold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            // DIA
            OutlinedTextField(
                value = dia,
                onValueChange = { input ->
                    dia = input.filter { it.isDigit() }
                        .take(2)
                        .let { if (it.toIntOrNull() in 1..31 || it.isEmpty()) it else dia }
                },
                modifier = Modifier.weight(1f),
                label = { Text("Dia") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // MÊS
            OutlinedTextField(
                value = mes,
                onValueChange = { input ->
                    mes = input.filter { it.isDigit() }
                        .take(2)
                        .let { if (it.toIntOrNull() in 1..12 || it.isEmpty()) it else mes }
                },
                modifier = Modifier.weight(1f),
                label = { Text("Mês") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // ANO
            OutlinedTextField(
                value = ano,
                onValueChange = { input ->
                    ano = input.filter { it.isDigit() }.take(4)
                },
                modifier = Modifier.weight(1f),
                label = { Text("Ano") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        OutlinedTextField(
            value = telemovel,
            onValueChange = {
                telemovel = it
                c.telemovel = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Telemóvel") }
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                c.email = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") }
        )

        if (dia.isNotEmpty() && mes.isNotEmpty() && ano.isNotEmpty()) {
            try {
                val dataNascimento = criarData(
                    ano = ano.toInt(),
                    mes = mes.toInt() - 1,
                    dia = dia.toInt()
                )
                c.dataNascimento = dataNascimento
            } catch (e: Exception) {
                // Data inválida: pode mostrar erro se quiser
            }
        }
    }
}

@Composable
fun CandidaturaDadosAcademicos(c: Candidatura) {
    val graus = listOf("Licenciatura", "Mestrado", "CTeSP")

    var alimentar by remember { mutableStateOf(false) }
    var higiene by remember { mutableStateOf(false) }
    var limpeza by remember { mutableStateOf(false) }

    var grau by rememberSaveable { mutableStateOf(c.grau) }
    var curso by rememberSaveable { mutableStateOf(c.curso) }
    var numeroAluno by rememberSaveable { mutableStateOf(c.numAluno.toString()) }

    Text(
        "Dados académicos",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        ComboBox(
            label = "Grau",
            selected = grau,
            options = graus,
            onSelect = {
                grau = it
                c.grau = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = curso,
            onValueChange = {
                curso = it
                c.curso = it
            },
            label = { Text("Curso") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = numeroAluno,
            onValueChange = {
                numeroAluno = it
                c.numAluno = it.toIntOrNull() ?: 0
            },
            label = { Text("Nº Aluno") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Tipologia do pedido", fontWeight = FontWeight.Bold)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = alimentar,
                    onCheckedChange = { alimentar = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF006837))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Produtos alimentares")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = higiene,
                    onCheckedChange = { higiene = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF006837))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Produtos de higiene pessoal")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = limpeza,
                    onCheckedChange = { limpeza = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF006837))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Produtos de limpeza")
            }
        }

        c.tipologiaPedido = buildList {
            if (alimentar) add("Produtos alimentares")
            if (higiene) add("Produtos de higiene pessoal")
            if (limpeza) add("Produtos de limpeza")
        }
    }
}

@Composable
fun CandidaturaApoiosExtras(c: Candidatura) {

    var faes by rememberSaveable { mutableStateOf(if (c.faes) "Sim" else "Não") }
    var bolseiro by rememberSaveable { mutableStateOf(if (c.bolseiro) "Sim" else "Não") }
    var valorBolsa by rememberSaveable { mutableStateOf(c.valorBolsa?.toString() ?: "") }

    Text(
        "Apoios extra",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // --- FAES ---
        Text(
            "É apoiado(a) pelo Fundo de Apoio de Emergência Social (FAES)?",
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("Sim", "Não").forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = faes == option,
                        onClick = {
                            faes = option
                            c.faes = option == "Sim"
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF006837))
                    )
                    Text(option)
                }
            }
        }

        // --- BOLSA ---
        Text(
            "É beneficiário de alguma bolsa de estudo ou apoio no presente ano letivo?",
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("Sim", "Não").forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = bolseiro == option,
                        onClick = {
                            bolseiro = option
                            c.bolseiro = option == "Sim"
                            if (option == "Não") {
                                valorBolsa = ""
                                c.valorBolsa = null
                            }
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF006837))
                    )
                    Text(option)
                }
            }
        }

        // --- VALOR BOLSA ---
        if (bolseiro == "Sim") {
            OutlinedTextField(
                value = valorBolsa,
                onValueChange = {
                    valorBolsa = it
                    c.valorBolsa = it.toDoubleOrNull()
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Indique o valor") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CandidaturaScreenPreview() {
    CandidaturaScreen()
}

@Preview(showBackground = true)
@Composable
fun CandidaturaDadosPessoaisPreview() {
    CandidaturaDadosPessoais(
        c = Candidatura()
    )
}

@Preview(showBackground = true)
@Composable
fun CandidaturaDadosAcademicosPreview() {
    CandidaturaDadosAcademicos(
        c = Candidatura()
    )
}

@Preview(showBackground = true)
@Composable
fun CandidaturaApoiosExtrasPreview() {
    CandidaturaApoiosExtras(
        c = Candidatura()
    )
}