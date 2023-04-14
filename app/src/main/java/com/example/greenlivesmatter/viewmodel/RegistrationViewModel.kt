import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    fun register(): Boolean {
        // Здесь должна быть реализация регистрации, например, через API-запрос или другой способ
        // В этом примере мы считаем, что регистрация успешна, если имя, email и пароль непустые
        return name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty()
    }

    fun onNameChanged(newName: String) {
        name.value = newName
    }

    fun onEmailChanged(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }
}
