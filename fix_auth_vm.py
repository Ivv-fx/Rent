import re

with open("app/src/main/java/com/example/ui/viewmodel/AuthViewModel.kt", "r") as f:
    content = f.read()

# Remove the bad appended code
if "class AuthViewModelFactory" in content:
    content = content.split("import androidx.lifecycle.ViewModelProvider")[0].strip()

# Now add AuthViewModelFactory correctly
factory_code = """
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModelFactory(
    private val repository: com.example.domain.repository.AuthRepository,
    private val sessionManager: com.example.data.local.SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
"""

if "class AuthViewModelFactory" not in content:
    content = content + "\n" + factory_code

with open("app/src/main/java/com/example/ui/viewmodel/AuthViewModel.kt", "w") as f:
    f.write(content)
