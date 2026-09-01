import re

with open("app/src/main/java/com/example/ui/screens/AuthScreen.kt", "r") as f:
    content = f.read()

if "import androidx.compose.runtime.collectAsState" not in content:
    content = content.replace("import androidx.compose.runtime.*", "import androidx.compose.runtime.*\nimport androidx.compose.runtime.collectAsState")

with open("app/src/main/java/com/example/ui/screens/AuthScreen.kt", "w") as f:
    f.write(content)
