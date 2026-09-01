with open("app/build.gradle.kts", "r") as f:
    content = f.read()

if "firebase" not in content:
    content = content.replace('dependencies {\n    implementation(libs.maps.compose)', 'dependencies {\n    implementation(platform(libs.firebase.bom))\n    implementation(libs.firebase.firestore)\n    implementation(libs.firebase.auth)\n    implementation(libs.maps.compose)')

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
