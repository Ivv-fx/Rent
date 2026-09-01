with open("gradle/libs.versions.toml", "r") as f:
    content = f.read()

if "mapsCompose" not in content:
    content = content.replace('googleid = "1.1.1"', 'googleid = "1.1.1"\nmapsCompose = "4.3.0"\nplayServicesMaps = "18.2.0"')

if "maps-compose" not in content:
    content = content.replace('googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }', 'googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }\nmaps-compose = { group = "com.google.maps.android", name = "maps-compose", version.ref = "mapsCompose" }\nplay-services-maps = { group = "com.google.android.gms", name = "play-services-maps", version.ref = "playServicesMaps" }')

with open("gradle/libs.versions.toml", "w") as f:
    f.write(content)
