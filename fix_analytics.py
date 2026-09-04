import re

with open("gradle/libs.versions.toml", "r") as f:
    toml = f.read()

if "firebase-analytics" not in toml:
    toml = toml.replace('firebase-auth = { group = "com.google.firebase", name = "firebase-auth" }',
                        'firebase-auth = { group = "com.google.firebase", name = "firebase-auth" }\nfirebase-analytics = { group = "com.google.firebase", name = "firebase-analytics" }')
    with open("gradle/libs.versions.toml", "w") as f:
        f.write(toml)

with open("app/build.gradle.kts", "r") as f:
    app_gradle = f.read()

if "libs.firebase.analytics" not in app_gradle:
    app_gradle = app_gradle.replace('implementation(platform(libs.firebase.bom))',
                                    'implementation(platform(libs.firebase.bom))\n  implementation(libs.firebase.analytics)')
    with open("app/build.gradle.kts", "w") as f:
        f.write(app_gradle)

