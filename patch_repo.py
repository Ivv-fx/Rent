import re

with open("app/src/main/java/com/example/data/repository/RoomFinderRepository.kt", "r") as f:
    content = f.read()

new_repo_funcs = """    fun verifyEmail() {
        _userProfile.value = _userProfile.value.copy(isEmailVerified = true)
    }
    fun verifyPhone() {
        _userProfile.value = _userProfile.value.copy(isPhoneVerified = true)
    }
    fun linkSocial() {
        _userProfile.value = _userProfile.value.copy(isSocialLinked = true)
    }
    fun verifyId() {
        _userProfile.value = _userProfile.value.copy(isIdVerified = true)
    }
    fun verifyStudent() {
        _userProfile.value = _userProfile.value.copy(isStudentVerified = true)
    }
    fun verifyBackground() {
        _userProfile.value = _userProfile.value.copy(isBackgroundChecked = true)
    }
    fun verifyBusiness() {
        _userProfile.value = _userProfile.value.copy(isBusinessVerified = true)
    }
    fun verifyOwnership() {
        _userProfile.value = _userProfile.value.copy(isOwnershipVerified = true)
    }"""

# Insert before toggleLandlordMode
content = content.replace("    fun toggleLandlordMode()  {", new_repo_funcs + "\n\n    fun toggleLandlordMode()  {")

with open("app/src/main/java/com/example/data/repository/RoomFinderRepository.kt", "w") as f:
    f.write(content)
