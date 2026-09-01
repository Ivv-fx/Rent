with open("gradle/libs.versions.toml", "r") as f:
    content = f.read()

# Make sure datastore and firebase-auth are there.
# They should already be, but let's check.
