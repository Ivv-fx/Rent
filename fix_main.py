with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                ProfileVerificationScreen(
                    viewModel = viewModel,
                    onNavigateToSavedRooms =  {
                        navController.navigate("saved_rooms")
                    }
                )""",
"""                ProfileVerificationScreen(
                    viewModel = viewModel,
                    onNavigateToSavedRooms =  {
                        navController.navigate("saved_rooms")
                    },
                    onLogout = {
                        authViewModel.logout()
                    }
                )"""
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
