import re

with open("app/src/main/java/com/example/data/models/RoomModels.kt", "r") as f:
    content = f.read()

old_listing = """    val landlordName: String,
    val landlordVerified: Boolean,
    val landlordRating: Float,
    val landlordReviewCount: Int,
    val backgroundChecked: Boolean,"""

new_listing = """    val landlordName: String,
    val landlordVerified: Boolean,
    val landlordRating: Float,
    val landlordReviewCount: Int,
    val backgroundChecked: Boolean,
    val businessVerified: Boolean = false,
    val ownershipVerified: Boolean = false,"""

content = content.replace(old_listing, new_listing)

with open("app/src/main/java/com/example/data/models/RoomModels.kt", "w") as f:
    f.write(content)
