import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

import_str = """import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.FirebaseApp"""

content = content.replace("import com.google.firebase.FirebaseApp", import_str)

init_str = """        try {
            FirebaseApp.initializeApp(this)
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            FirebaseFirestore.getInstance().firestoreSettings = settings
        } catch (e: Exception) {"""

content = content.replace("""        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {""", init_str)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
