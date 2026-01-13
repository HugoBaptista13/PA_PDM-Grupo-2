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
import ipca.example.lojasocialipca.ui.AvaliarCandidaturaScreen
import ipca.example.lojasocialipca.ui.ConsultarCandidaturaFuncionarioScreen
import ipca.example.lojasocialipca.ui.ConsultarEntregasScreen
import kotlin.Unit


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
                        currentScreen = "mainBenficiario"
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

                "produtosFuncionario" -> ProdutosFuncionarioScreen(
                    produtos = emptyList(),
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

                "consultarCampanha" -> ConsultarCampanhaScreen(
                    campanhas = emptyList(),
                    onCriarCampanha = {
                        currentScreen = "inserirCampanha"
                    }
                )

                "inserirCampanha" -> InserirCampanhaScreen(
                    onBack = {
                        currentScreen = "consultarCampanha"
                    },
                    onCriarCampanha = {
                        currentScreen = "consultarCampanha"
                    }
                )

                "funcionarioCandidatura" -> ConsultarCandidaturaFuncionarioScreen(
                    candidaturas = emptyList(),
                    onBack = {
                        currentScreen = "mainFuncionario"
                    },
                    onAvaliar = {
                        currentScreen = "avaliarCandidatura"
                    }
                )

                "avaliarCandidatura" -> AvaliarCandidaturaScreen(
                    candidatura = Candidatura(),
                    onBack = {
                        currentScreen = "funcionarioCandidatura"
                    },
                    onAprovar = {
                        currentScreen = "funcionarioCandidatura"
                    },
                    onReprovar = {
                        currentScreen = "funcionarioCandidatura"
                    }
                )

                "consultarEntrega" -> ConsultarEntregasScreen(
                    entregas = emptyList(),
                    onBack = {
                        currentScreen = "mainFuncionario"
                    },
                    onMarcar = {
                        currentScreen = "marcarEntrega"
                    }
                )

                "marcarEntrega" -> MarcarEntregaScreen(
                    produtos = emptyList(),
                    onBack = {
                        currentScreen = "consultarEntrega"
                    },
                    onConfirmar = {
                        currentScreen = "consultarEntrega"
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LojaSocialPreview() {
    LojaSocialApp()
}
