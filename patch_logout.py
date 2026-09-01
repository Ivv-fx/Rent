import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace(
    'ProfileVerificationScreen(viewModel = viewModel)',
    'ProfileVerificationScreen(viewModel = viewModel, onLogout = { authViewModel.logout() })'
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/ui/screens/ProfileVerificationScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    'fun ProfileVerificationScreen(viewModel: RoomFinderViewModel)',
    'fun ProfileVerificationScreen(viewModel: RoomFinderViewModel, onLogout: () -> Unit = {})'
)
content = content.replace(
    'onClick =  { viewModel.logout() }',
    'onClick =  { onLogout() }'
)

with open("app/src/main/java/com/example/ui/screens/ProfileVerificationScreen.kt", "w") as f:
    f.write(content)
