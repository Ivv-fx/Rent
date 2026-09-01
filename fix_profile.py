import re

with open("app/src/main/java/com/example/ui/screens/ProfileVerificationScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    "fun ProfileVerificationScreen(\n    viewModel: RoomFinderViewModel,\n    onNavigateToSavedRooms: () -> Unit,\n    modifier: Modifier = Modifier\n)  {",
    "fun ProfileVerificationScreen(\n    viewModel: RoomFinderViewModel,\n    onNavigateToSavedRooms: () -> Unit,\n    onLogout: () -> Unit = {},\n    modifier: Modifier = Modifier\n)  {"
)

with open("app/src/main/java/com/example/ui/screens/ProfileVerificationScreen.kt", "w") as f:
    f.write(content)
