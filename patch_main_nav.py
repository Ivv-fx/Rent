import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Pass authViewModel to MainAppNavHost
content = content.replace(
    "MainAppNavHost(viewModel = viewModel)",
    "MainAppNavHost(viewModel = viewModel, authViewModel = authViewModel)"
)

# Update MainAppNavHost signature
content = content.replace(
    "fun MainAppNavHost(viewModel: RoomFinderViewModel)  {",
    "fun MainAppNavHost(viewModel: RoomFinderViewModel, authViewModel: AuthViewModel)  {"
)

# Replace isLoggedIn check
content = content.replace(
    "val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()",
    "val loggedInUserId by authViewModel.isLoggedIn.collectAsState(initial = null)\n    val isLoggedIn = loggedInUserId != null"
)

# AuthScreen call update
content = content.replace(
    "AuthScreen(viewModel = viewModel)",
    "AuthScreen(viewModel = authViewModel, onAuthSuccess = { /* No-op, state triggers re-compose */ })"
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
