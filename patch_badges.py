import re

with open("app/src/main/java/com/example/ui/components/CommonBadges.kt", "r") as f:
    content = f.read()

old_fun = """@Composable
fun VerificationBadgesRow(
    isLandlordVerified: Boolean,
    isBackgroundChecked: Boolean,
    modifier: Modifier = Modifier
)  {"""

new_fun = """import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home

@Composable
fun VerificationBadgesRow(
    isLandlordVerified: Boolean,
    isBackgroundChecked: Boolean,
    isBusinessVerified: Boolean = false,
    isOwnershipVerified: Boolean = false,
    modifier: Modifier = Modifier
)  {"""

content = content.replace(old_fun, new_fun)

# Add business and ownership blocks
old_blocks = """        if (isBackgroundChecked)  {
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ThemeSuccessLight)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            )  {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Background Checked",
                    tint = ThemeSuccess,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Background Checked",
                    color = ThemeSuccess,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}"""

new_blocks = """        if (isBackgroundChecked)  {
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ThemeSuccessLight)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            )  {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Background Checked",
                    tint = ThemeSuccess,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Background Checked",
                    color = ThemeSuccess,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        if (isBusinessVerified) {
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ThemeSecondaryLight)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            )  {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = "Business Verified",
                    tint = ThemeSecondary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Business Verified",
                    color = ThemeSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        if (isOwnershipVerified) {
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ThemeSecondaryLight)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            )  {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Ownership Verified",
                    tint = ThemeSecondary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Ownership Verified",
                    color = ThemeSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}"""

content = content.replace(old_blocks, new_blocks)

with open("app/src/main/java/com/example/ui/components/CommonBadges.kt", "w") as f:
    f.write(content)
