import re

with open("app/src/main/java/com/example/ui/screens/ProfileVerificationScreen.kt", "r") as f:
    content = f.read()

# Replace the specific block
start_str = """            // Background Check & Verification Badges Center (CRITICAL FEATURE)"""
end_str = """            // Saved Rooms Quick Tile"""

# Extract everything between these and replace
match = re.search(re.escape(start_str) + r"(.*?)" + re.escape(end_str), content, re.DOTALL)
if match:
    new_content = """            // Background Check & Verification Badges Center (CRITICAL FEATURE)
            Text(
                text = "Integrated Background Checks & Credentials",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Verified credentials protect both renters and landlords in urban student housing.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )

            // Basic Info Verification
            VerificationStatusItem(
                icon = Icons.Default.Email,
                title = "Email Verification",
                subtitle = "Linked to ${profile.email}",
                isVerified = profile.isEmailVerified,
                onClick = { viewModel.verifyEmail() }
            )
            Spacer(modifier = Modifier.height(8.dp))
            VerificationStatusItem(
                icon = Icons.Default.Phone,
                title = "Phone Verification",
                subtitle = "SMS verification for secure communication",
                isVerified = profile.isPhoneVerified,
                onClick = { viewModel.verifyPhone() }
            )
            Spacer(modifier = Modifier.height(8.dp))
            VerificationStatusItem(
                icon = Icons.Default.Link,
                title = "Social Media Link",
                subtitle = "Connect LinkedIn or Instagram (Optional)",
                isVerified = profile.isSocialLinked,
                onClick = { viewModel.linkSocial() }
            )
            Spacer(modifier = Modifier.height(8.dp))
            VerificationStatusItem(
                icon = Icons.Default.Badge,
                title = "Government ID Verification",
                subtitle = "State Driver's License & Biometrics verified",
                isVerified = profile.isIdVerified,
                onClick = { viewModel.verifyId() }
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (!profile.isLandlordMode) {
                VerificationStatusItem(
                    icon = Icons.Default.School,
                    title = "Student Status Verification",
                    subtitle = "Enrolled at ${profile.university} (.edu active)",
                    isVerified = profile.isStudentVerified,
                    onClick = { viewModel.verifyStudent() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                VerificationStatusItem(
                    icon = Icons.Default.Security,
                    title = "Background Check Record",
                    subtitle = "Criminal record & national eviction search passed",
                    isVerified = profile.isBackgroundChecked,
                    onClick = { viewModel.verifyBackground() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                VerificationStatusItem(
                    icon = Icons.Default.CreditScore,
                    title = "Credit Score Tier (${profile.creditTier})",
                    subtitle = "Verified via TransUnion SmartMove API",
                    isVerified = true
                )
            } else {
                Text(
                    text = "Landlord Strict Verification",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
                VerificationStatusItem(
                    icon = Icons.Default.Business,
                    title = "Business Registration",
                    subtitle = "LLC or Sole Proprietorship verified",
                    isVerified = profile.isBusinessVerified,
                    onClick = { viewModel.verifyBusiness() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                VerificationStatusItem(
                    icon = Icons.Default.Home,
                    title = "Property Ownership",
                    subtitle = "Deed & Property Tax Records verified",
                    isVerified = profile.isOwnershipVerified,
                    onClick = { viewModel.verifyOwnership() }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Saved Rooms Quick Tile"""
    content = content.replace(match.group(0), new_content)

with open("app/src/main/java/com/example/ui/screens/ProfileVerificationScreen.kt", "w") as f:
    f.write(content)
