package ipca.example.lojasocialipca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import ipca.example.lojasocialipca.models.Candidatura
import ipca.example.lojasocialipca.models.Entrega
import ipca.example.lojasocialipca.ui.screens.funcionario.AvaliarCandidaturaScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarCandidaturaFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.LoginScreen
import ipca.example.lojasocialipca.ui.screens.RegisterScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.ConsultarPedidoScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.InserirPedidoScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.MainBeneficiarioScreen
import ipca.example.lojasocialipca.ui.screens.beneficiario.PerfilBeneficiarioScreen
import ipca.example.lojasocialipca.ui.screens.candidato.CandidaturaScreen
import ipca.example.lojasocialipca.ui.screens.candidato.ConsultarCandidaturaScreen
import ipca.example.lojasocialipca.ui.screens.candidato.MainCandidatoScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarCampanhaScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarCampanhaView
import ipca.example.lojasocialipca.ui.screens.funcionario.ConsultarEntregasScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.InserirCampanhaScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.InserirProdutoScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.MainFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.MarcarEntregaScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.PerfilFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ProdutosFuncionarioScreen
import ipca.example.lojasocialipca.ui.screens.funcionario.ProdutosFuncionarioView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LojaSocialApp()
        }
    }
}

@Composable
fun LojaSocialApp() {
    // login        -> ecrã Login
    // register     -> ecrã Registo
    // mainCandidato-> ecrã principal do candidato
    // candidatura1 -> primeira página da candidatura
    // candidatura2 -> segunda página da candidatura
    var currentScreen by remember { mutableStateOf("login") }
    var selectedCandidatura by remember { mutableStateOf<Candidatura?>(null) }
    var selectedEntrega by remember { mutableStateOf<Entrega?>(null) }

    MaterialTheme {
        Surface {
            when (currentScreen) {
                "login" -> LoginScreen(
                    onLoginSuccess = {
                        currentScreen = "mainFuncionario"
                    },
                    onGoToRegister = {
                        currentScreen = "register"
                    }
                )

                "register" -> RegisterScreen(
                    onRegisterSuccess = {
                        currentScreen = "login"
                    },
                    onBackToLogin = {
                        currentScreen = "login"
                    }
                )

                "mainCandidato" -> MainCandidatoScreen(
                    onFazerCandidatura = {
                        currentScreen = "candidatura"
                    },
                    onAcompanharCandidatura = {
                        currentScreen = "consultarCandidatura"
                    },
                    onLogout = {
                        currentScreen = "login"
                    },
                    onHome = {
                        currentScreen = "mainCandidato"
                    },
                    onPerfil = {
                        currentScreen = ""
                    }
                )

                "candidatura" -> CandidaturaScreen(
                    onCandidaturaSuccess = {
                        currentScreen = "consultarCandidatura"
                    },
                )

                "consultarCandidatura" -> ConsultarCandidaturaScreen(
                    candidaturas = emptyList(),
                    onBack = {
                        currentScreen = "mainCandidato"
                    },
                )

                "mainBeneficiario" -> MainBeneficiarioScreen (
                    onFazerPedido = {
                        currentScreen = "inserirPedido"
                    },
                    onAcompanharPedido = {
                        currentScreen = "consultarPedido"
                    },
                    onLogout = {
                        currentScreen = "login"
                    },
                    onPerfil = {
                        currentScreen = "perfilBeneficiario"
                    }
                )

                "perfilBeneficiario" -> PerfilBeneficiarioScreen(
                    nome = "",
                    numAluno = 12,
                    curso = "",
                    grau = "",
                    alertas = emptyList(),
                    onMudarPassword = {}
                )

                "inserirPedido" -> InserirPedidoScreen (
                    onBack = {
                        currentScreen = "mainBeneficiario"
                    },
                    onInserirPedido = {
                        currentScreen = "consultarPedido"
                    }
                )

                "consultarPedido" -> ConsultarPedidoScreen(
                    pedidos = emptyList(),
                    onBack = {
                        currentScreen = "mainBeneficiario"
                    }
                )

                "mainFuncionario" -> MainFuncionarioScreen (
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
                    onLogout = {
                        currentScreen = "login"
                    },
                    onPerfil = {
                        currentScreen = "perfilFuncionario"
                    }
                )

                "perfilFuncionario" -> PerfilFuncionarioScreen(
                    nome = "",
                    email = "",
                    alertas = emptyList(),
                    onMudarPassword = {}
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LojaSocialPreview() {
    LojaSocialApp()
}
