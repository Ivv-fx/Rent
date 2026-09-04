import re

with open("app/src/main/java/com/example/ui/viewmodel/RoomFinderViewModel.kt", "r") as f:
    content = f.read()

# Add the verification functions
verification_functions = """    fun updateUserProfile(name: String, email: String, university: String, creditTier: String) {
        val current = _userProfile.value
        _userProfile.value = current.copy(
            name = name,
            email = email,
            university = university,
            creditTier = creditTier
        )
    }

    fun verifyEmail() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isEmailVerified = true)
    }

    fun verifyPhone() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isPhoneVerified = true)
    }

    fun linkSocial() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isSocialLinked = true)
    }

    fun verifyId() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isIdVerified = true)
    }

    fun verifyStudent() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isStudentVerified = true)
    }

    fun verifyBackground() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isBackgroundChecked = true)
    }

    fun verifyBusiness() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isBusinessVerified = true)
    }

    fun verifyOwnership() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isOwnershipVerified = true)
    }"""

# Replace existing updateUserProfile with our bunch
old_update_user_profile = """    fun updateUserProfile(name: String, email: String, university: String, creditTier: String) {
        val current = _userProfile.value
        _userProfile.value = current.copy(
            name = name,
            email = email,
            university = university,
            creditTier = creditTier
        )
    }"""

content = content.replace(old_update_user_profile, verification_functions)

with open("app/src/main/java/com/example/ui/viewmodel/RoomFinderViewModel.kt", "w") as f:
    f.write(content)
