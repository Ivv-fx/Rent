import re

with open("app/src/main/java/com/example/ui/viewmodel/RoomFinderViewModel.kt", "r") as f:
    content = f.read()

new_vm_funcs = """    fun verifyEmail() = repository.verifyEmail()
    fun verifyPhone() = repository.verifyPhone()
    fun linkSocial() = repository.linkSocial()
    fun verifyId() = repository.verifyId()
    fun verifyStudent() = repository.verifyStudent()
    fun verifyBackground() = repository.verifyBackground()
    fun verifyBusiness() = repository.verifyBusiness()
    fun verifyOwnership() = repository.verifyOwnership()"""

# Insert before toggleLandlordMode
content = content.replace("    fun toggleLandlordMode()  {", new_vm_funcs + "\n\n    fun toggleLandlordMode()  {")

with open("app/src/main/java/com/example/ui/viewmodel/RoomFinderViewModel.kt", "w") as f:
    f.write(content)
