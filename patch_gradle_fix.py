with open("app/build.gradle.kts", "r") as f:
    content = f.read()

content = content.replace('        manifestPlaceholders["MAPS_API_KEY"] = com.example.BuildConfig.MAPS_API_KEY ?: ""', '')

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
