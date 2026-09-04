import re

with open("app/src/main/java/com/example/data/models/RoomModels.kt", "r") as f:
    content = f.read()

old_str = """data class UserProfile(
    val id: String = "user_101",
    val name: String = "Alex Chen",
    val email: String = "alex.chen@nyu.edu",
    val university: String = "Srinagar University (HNBGU)",
    val isStudent: Boolean = true,
    val isIdVerified: Boolean = true,
    val isBackgroundChecked: Boolean = true,
    val isStudentVerified: Boolean = true,
    val creditTier: String = "Excellent (740+)",
    val isLandlordMode: Boolean = false,
    val landlordSubscription: String = "Pro Landlord (₹19.99/mo)"
)"""

new_str = """data class UserProfile(
    val id: String = "user_101",
    val name: String = "Alex Chen",
    val email: String = "alex.chen@nyu.edu",
    val university: String = "Srinagar University (HNBGU)",
    val isStudent: Boolean = true,
    val isEmailVerified: Boolean = true,
    val isPhoneVerified: Boolean = false,
    val isSocialLinked: Boolean = false,
    val isIdVerified: Boolean = true,
    val isBackgroundChecked: Boolean = true,
    val isStudentVerified: Boolean = true,
    val creditTier: String = "Excellent (740+)",
    val isLandlordMode: Boolean = false,
    val isBusinessVerified: Boolean = false,
    val isOwnershipVerified: Boolean = false,
    val landlordSubscription: String = "Pro Landlord (₹19.99/mo)"
)"""

content = content.replace(old_str, new_str)
with open("app/src/main/java/com/example/data/models/RoomModels.kt", "w") as f:
    f.write(content)
