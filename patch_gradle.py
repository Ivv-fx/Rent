with open("app/build.gradle.kts", "r") as f:
    content = f.read()

if "maps.compose" not in content:
    content = content.replace('dependencies {', 'dependencies {\n    implementation(libs.maps.compose)\n    implementation(libs.play.services.maps)')

if "MAPS_API_KEY" not in content:
    target = 'defaultConfig {\n        applicationId = "com.example"'
    replacement = 'defaultConfig {\n        applicationId = "com.example"\n        manifestPlaceholders["MAPS_API_KEY"] = com.example.BuildConfig.MAPS_API_KEY ?: ""'
    content = content.replace(target, replacement)

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
