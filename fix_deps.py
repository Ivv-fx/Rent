with open("app/build.gradle.kts", "r") as f:
    content = f.read()

content = content.replace('// implementation(platform(libs.firebase.bom))', 'implementation(platform(libs.firebase.bom))')
content = content.replace('// implementation(libs.androidx.credentials)', 'implementation(libs.androidx.credentials)')
content = content.replace('// implementation(libs.androidx.credentials.play.services)', 'implementation(libs.androidx.credentials.play.services)')
content = content.replace('// implementation(libs.googleid)', 'implementation(libs.googleid)')

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
