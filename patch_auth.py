import re

with open('app/src/main/java/com/example/ui/screens/AuthScreen.kt', 'r') as f:
    content = f.read()

# Add collectAsState
if 'import androidx.compose.runtime.collectAsState' not in content:
    content = content.replace('import androidx.compose.runtime.*', 'import androidx.compose.runtime.*\nimport androidx.compose.runtime.collectAsState')

# Find the Spacer before Button
target = '''                Spacer(modifier = Modifier.height(24.dp))

                Button('''

replacement = '''                Spacer(modifier = Modifier.height(24.dp))
                
                val authError by viewModel.authError.collectAsState()
                LaunchedEffect(authError) {
                    if (authError != null) {
                        isLoading = false
                    }
                }
                if (authError != null) {
                    Text(
                        text = authError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button('''

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/ui/screens/AuthScreen.kt', 'w') as f:
    f.write(content)
