with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

new_lines = []
skip = False
for i, line in enumerate(lines):
    if "val sessionManager = SessionManager(applicationContext)" in line and i > 93:
        continue
    if "val authRepository = FirebaseAuthRepositoryImpl()" in line and i > 93:
        continue
    if "val authViewModelFactory = AuthViewModelFactory(authRepository, sessionManager)" in line and i > 93:
        continue
    if "val authViewModel: AuthViewModel by viewModels { authViewModelFactory }" in line and i > 93:
        continue
    new_lines.append(line)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.writelines(new_lines)
