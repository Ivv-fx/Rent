import re

with open("app/src/main/java/com/example/ui/screens/ListingDetailScreen.kt", "r") as f:
    content = f.read()

old_badges_call = """                        VerificationBadgesRow(
                            isLandlordVerified = listing.landlordVerified,
                            isBackgroundChecked = listing.backgroundChecked
                        )"""

new_badges_call = """                        VerificationBadgesRow(
                            isLandlordVerified = listing.landlordVerified,
                            isBackgroundChecked = listing.backgroundChecked,
                            isBusinessVerified = listing.businessVerified,
                            isOwnershipVerified = listing.ownershipVerified
                        )"""

content = content.replace(old_badges_call, new_badges_call)

with open("app/src/main/java/com/example/ui/screens/ListingDetailScreen.kt", "w") as f:
    f.write(content)
