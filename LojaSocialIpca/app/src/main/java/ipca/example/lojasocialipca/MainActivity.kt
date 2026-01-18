package ipca.example.lojasocialipca

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ipca.example.lojasocialipca.models.Candidatura
import ipca.example.lojasocialipca.models.Entrega
import ipca.example.lojasocialipca.ui.screens.LoginScreen
import ipca.example.lojasocialipca.ui.screens.RegisterScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.ConsultarPedidoScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.InserirPedidoScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.MainBeneficiarioScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.PerfilBeneficiarioScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.RemarcarPedidoScreen
import ipca.example.lojasocialipca.ui.screens.candidato.CandidaturaScreen
import ipca.example.lojasocialipca.ui.screens.candidato.ConsultarCandidaturaScreen
import ipca.example.lojasocialipca.ui.screens.candidato.MainCandidatoScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.AvaliarCandidaturaScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarBeneficiarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarCampanhaView
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarCandidaturaFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarEntregasScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.InserirCampanhaScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.InserirFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.InserirProdutoScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.MainFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.MarcarEntregaScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.PerfilFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ProdutosFuncionarioView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Esconde status bar e navigation bar
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(
                WindowInsetsCompat.Type.statusBars() or
                        WindowInsetsCompat.Type.navigationBars()
            )

            // Faz reaparecer apenas com swipe
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        setContent {
            LojaSocialApp(
                onLogout = { logout() }
            )
        }
    }
    fun logout() {
        AppModule.auth.signOut()
    }
}

@Composable
fun LojaSocialApp(
    onLogout: () -> Unit
) {
    var currentScreen by remember { mutableStateOf("login") }
    var selectedCandidatura by remember { mutableStateOf<Candidatura?>(null) }
    var selectedEntrega by remember { mutableStateOf<Entrega?>(null) }
    val context = LocalContext.current

    val firestore = AppModule.firestore

    MaterialTheme {
        Surface {
            when (currentScreen) {

                // ------------------- LOGIN -------------------
                "login" -> LoginScreen(
                    onLoginSuccess = { uid ->
                        // Verifica se é funcionário ou beneficiário
                        firestore.collection("funcionarios")
                            .document(uid)
                            .get()
                            .addOnSuccessListener { funcSnap ->
                                if (funcSnap.exists()) {
                                    currentScreen = "mainFuncionario"
                                } else {
                                    // Verifica beneficiário
                                    firestore.collection("beneficiarios")
                                        .document(uid)
                                        .get()
                                        .addOnSuccessListener { benSnap ->
                                            if (benSnap.exists()) {
                                                val aceite = benSnap.getBoolean("aceite") ?: false
                                                currentScreen = if (aceite) "mainBeneficiario" else "mainCandidato"
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Utilizador não encontrado",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                currentScreen = "login"
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                "Erro ao buscar beneficiário: ${it.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Erro ao buscar funcionário: ${it.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    },
                    onGoToRegister = {
                        currentScreen = "register"
                    }
                )

                // ------------------- REGISTRO -------------------
                "register" -> RegisterScreen(
                    onRegisterSuccess = {
                        currentScreen = "login"
                    },
                    onBackToLogin = {
                        currentScreen = "login"
                    }
                )

                // ------------------- CANDIDATO -------------------
                "mainCandidato" -> MainCandidatoScreen(
                    onFazerCandidatura = {
                        currentScreen = "candidatura"
                    },
                    onAcompanharCandidatura = {
                        currentScreen = "consultarCandidatura"
                    },
                    onLogout = {
                        onLogout()
                        currentScreen = "login"
                    },
                    onPerfil = {
                        currentScreen = "perfilBeneficiario"
                    }
                )

                "candidatura" -> CandidaturaScreen(
                    onCandidaturaSuccess = {
                        currentScreen = "consultarCandidatura"
                    },
                    onBack = {
                        currentScreen = "mainCandidato"
                    }
                )

                "consultarCandidatura" -> ConsultarCandidaturaScreen(
                    onBack = {
                        currentScreen = "mainCandidato"
                    },
                )


                // ------------------- BENEFICIARIO -------------------
                "mainBeneficiario" -> MainBeneficiarioScreen(
                    onFazerPedido = {
                        currentScreen = "inserirPedido"
                    },
                    onAcompanharPedido = {
                        currentScreen = "consultarPedido"
                    },
                    onLogout = {
                        onLogout()
                        currentScreen = "login"
                    },
                    onPerfil = {
                        currentScreen = "perfilBeneficiario"
                    }
                )

                "perfilBeneficiario" -> {
                    PerfilBeneficiarioScreen(
                        onHome = {
                            val uid = AppModule.auth.currentUser?.uid
                            if (uid != null) {
                                AppModule.firestore.collection("beneficiarios")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener { snapshot ->
                                        val aceite = snapshot.getBoolean("aceite") ?: false
                                        currentScreen = if (aceite) "mainBeneficiario" else "mainCandidato"
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Erro ao obter dados do beneficiário",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Utilizador não autenticado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        onLogout = {
                            onLogout()
                            currentScreen = "login"
                        }
                    )
                }

                "inserirPedido" -> InserirPedidoScreen (
                    onBack = {
                        currentScreen = "mainBeneficiario"
                    },
                    onInserirPedido = {
                        currentScreen = "consultarPedido"
                    }
                )

                "consultarPedido" -> ConsultarPedidoScreen(
                    onBack = {
                        currentScreen = "mainBeneficiario"
                    },
                    onRealizaRemarcar = { entrega ->
                        selectedEntrega = entrega
                        currentScreen = "remarcarEntrega"
                    }
                )

                "remarcarEntrega" -> {
                    selectedEntrega?.let { entrega ->
                        RemarcarPedidoScreen(
                            entrega = entrega,
                            onBack = {
                                currentScreen = "consultarPedido"
                                selectedEntrega = null
                            }
                        )
                    }
                }
                // ------------------- FUNCIONARIO -------------------
                "mainFuncionario" -> MainFuncionarioScreen(
                    onProdutos = {
                        currentScreen = "produtosFuncionario"
                    },
                    onCampanhas = {
                        currentScreen = "consultarCampanha"
                    },
                    onCandidaturas = {
                        currentScreen = "funcionarioCandidatura"
                    },
                    onEntregas = {
                        currentScreen = "consultarEntrega"
                    },
                    onFuncionarios = {
                        currentScreen = "consultarFuncionarios"
                    },
                    onBeneficiarios = {
                        currentScreen = "consultarBeneficiarios"
                    },
                    onLogout = {
                        onLogout()
                        currentScreen = "login"
                    },
                    onPerfil = {
                        currentScreen = "perfilFuncionario"
                    }
                )

                "perfilFuncionario" -> PerfilFuncionarioScreen(
                    onHome = {
                        currentScreen = "mainFuncionario"
                    },
                    onLogout = {
                        onLogout()
                        currentScreen = "login"
                    },
                )

                "produtosFuncionario" -> ProdutosFuncionarioView(
                    onBack = {
                        currentScreen = "mainFuncionario"
                    },
                    onInserirProduto = {
                        currentScreen = "inserirProduto"
                    }
                )

                "inserirProduto" -> InserirProdutoScreen(
                    onBack = {
                        currentScreen = "produtosFuncionario"
                    },
                    onCancelar = {
                        currentScreen = "produtosFuncionario"
                    }
                )

                "consultarCampanha" -> ConsultarCampanhaView(
                    onBack = {
                        currentScreen = "mainFuncionario"
                    },
                    onCriarCampanha = {
                        currentScreen = "inserirCampanha"
                    }
                )

                "inserirCampanha" -> InserirCampanhaScreen(
                    onBack = {
                        currentScreen = "consultarCampanha"
                    }
                )

                "funcionarioCandidatura" -> ConsultarCandidaturaFuncionarioScreen(
                    onBack = {
                        currentScreen = "mainFuncionario"
                    },
                    onAvaliar = { candidatura ->
                        selectedCandidatura = candidatura
                        currentScreen = "avaliarCandidatura"
                    }
                )

                "avaliarCandidatura" -> {
                    // Só mostra a tela se selectedCandidatura não for nula
                    selectedCandidatura?.let { candidatura ->
                        AvaliarCandidaturaScreen(
                            candidatura = candidatura,
                            onBack = {
                                currentScreen = "funcionarioCandidatura"
                                selectedCandidatura = null // limpa após voltar
                            }
                        )
                    }
                }

                "consultarEntrega" -> ConsultarEntregasScreen(
                    onBack = {
                        currentScreen = "mainFuncionario"
                    },
                    onMarcar = { entrega ->
                        selectedEntrega = entrega
                        currentScreen = "marcarEntrega"
                    }
                )

                "marcarEntrega" -> {
                    selectedEntrega?.let { entrega ->
                        MarcarEntregaScreen(
                            entrega = entrega,
                            onBack = {
                                currentScreen = "consultarEntrega"
                                selectedEntrega = null
                            }
                        )
                    }
                }

                "consultarFuncionarios" -> ConsultarFuncionarioScreen(
                    onBack = {
                        currentScreen = "mainFuncionario"
                    },
                    onInserir = {
                        currentScreen = "inserirFuncionario"
                    }
                )

                "inserirFuncionario" -> InserirFuncionarioScreen(
                    onBack = {
                        currentScreen = "consultarFuncionarios"
                    }
                )

                "consultarBeneficiarios" -> ConsultarBeneficiarioScreen(
                    onBack = {
                        currentScreen = "mainFuncionario"
                    }
                )
            }
        }
    }
}
