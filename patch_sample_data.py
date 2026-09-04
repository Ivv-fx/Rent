import re

with open("app/src/main/java/com/example/data/sample/SampleData.kt", "r") as f:
    content = f.read()

content = content.replace("backgroundChecked = true,", "backgroundChecked = true,\n            businessVerified = true,\n            ownershipVerified = true,")

with open("app/src/main/java/com/example/data/sample/SampleData.kt", "w") as f:
    f.write(content)
