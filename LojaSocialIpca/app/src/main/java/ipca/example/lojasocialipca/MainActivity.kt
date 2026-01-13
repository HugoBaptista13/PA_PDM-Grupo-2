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
import ipca.example.lojasocialipca.ui.screens.LoginScreen
import ipca.example.lojasocialipca.ui.screens.RegisterScreen
import ipca.example.lojasocialipca.ui.screens.candidato.CandidaturaScreen
import ipca.example.lojasocialipca.ui.screens.candidato.MainCandidatoScreen

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
    // candidatura -> página da candidatura
    var currentScreen by remember { mutableStateOf("login") }

    MaterialTheme {
        Surface {
            when (currentScreen) {
                "login" -> LoginScreen(
                    onLoginSuccess = {
                        currentScreen = "mainCandidato"
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
                        currentScreen = "candidatura1"
                    },
                    onAcompanharCandidatura = {
                        // TODO: ligar ao ecrã de acompanhamento
                    },
                    onLogout = {
                        currentScreen = "login"
                    },
                    onHome = {
                        // por agora fica no próprio mainCandidato
                        currentScreen = "mainCandidato"
                    },
                    onPerfil = {
                        // TODO: quando existir ecrã de perfil
                    }
                )

                "candidatura" -> CandidaturaScreen(
                    onBack = {
                        currentScreen = "mainCandidato"
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
