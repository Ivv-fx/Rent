import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

init_auth = """
        val sessionManager = SessionManager(applicationContext)
        val authRepository = FirebaseAuthRepositoryImpl()
        val authViewModelFactory = AuthViewModelFactory(authRepository, sessionManager)
        val authViewModel: AuthViewModel by viewModels { authViewModelFactory }
"""

# replace safely using regex
content = re.sub(r'val viewModel: RoomFinderViewModel by viewModels\s+\{\s*viewModelFactory\s*\}', 
                 r'val viewModel: RoomFinderViewModel by viewModels { viewModelFactory }\n' + init_auth, 
                 content)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
