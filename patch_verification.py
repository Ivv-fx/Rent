import re

with open("app/src/main/java/com/example/ui/screens/ProfileVerificationScreen.kt", "r") as f:
    content = f.read()

# Make VerificationStatusItem clickable and add onClick
old_verification_item = """@Composable
private fun VerificationStatusItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isVerified: Boolean
)  {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder()
    )  {"""

new_verification_item = """@Composable
private fun VerificationStatusItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isVerified: Boolean,
    onClick: (() -> Unit)? = null
)  {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isVerified && onClick != null) { onClick?.invoke() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder()
    )  {"""

content = content.replace(old_verification_item, new_verification_item)

# Update the "Pending" text so it shows a clickable prompt if an action is available
old_text = """                    Text(
                        text = if (isVerified) "Verified" else "Pending",
                        color = if (isVerified) ThemeSuccess else ThemeTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )"""

new_text = """                    Text(
                        text = if (isVerified) "Verified" else if (onClick != null) "Verify Now" else "Pending",
                        color = if (isVerified) ThemeSuccess else ThemeTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )"""
content = content.replace(old_text, new_text)

# We need to inject additional icons to imports
old_imports = """import androidx.compose.material.icons.filled.SwapHoriz"""
new_imports = """import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
"""
content = content.replace(old_imports, new_imports)

with open("app/src/main/java/com/example/ui/screens/ProfileVerificationScreen.kt", "w") as f:
    f.write(content)
