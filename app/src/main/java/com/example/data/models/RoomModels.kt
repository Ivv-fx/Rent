package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RoomType(val displayName: String)  {
    PRIVATE_ROOM("Private Room"),
    SHARED_ROOM("Shared Room"),
    STUDIO("Studio Loft"),
    MASTER_SUITE("Master Suite")
}

enum class PriceCategory(val label: String, val minPrice: Int, val maxPrice: Int)  {
    ALL("All Prices", 0, 10000),
    BUDGET_STUDENT("Student Budget (<₹650)", 0, 650),
    MID_RANGE("Mid-Range (₹650 - ₹1,100)", 650, 1100),
    PREMIUM("Premium (₹1,100+)", 1100, 10000)
}

enum class AvailabilityStatus(val label: String)  {
    AVAILABLE_NOW("Available Now"),
    MOVE_OUT_ALERT("Upcoming Move-out Alert"),
    ONLY_ONE_LEFT("Only 1 Room Left"),
    RESERVED("Reserved")
}

data class TourHotspot(
    val id: String,
    val title: String,
    val description: String,
    val xPercent: Float, // 0.0 to 1.0 on 360 canvas
    val yPercent: Float,
    val iconType: String = "info"
)

data class VirtualTourRoom(
    val id: String,
    val name: String,
    val roomType: String,
    val description: String,
    val features: List<String>,
    val hotspots: List<TourHotspot>
)

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val roomType: String,
    val address: String,
    val neighborhood: String,
    val universityNearby: String,
    val walkMinutesToCampus: Int,
    val metroDistance: String,
    val basePrice: Int,
    val utilitiesPrice: Int, // e.g. ₹60 or 0 if included
    val depositPrice: Int,
    val studentDiscountPercent: Int, // e.g. 10%
    val availabilityStatus: String,
    val availableDate: String,
    val landlordId: String,
    val landlordName: String,
    val landlordVerified: Boolean,
    val landlordRating: Float,
    val landlordReviewCount: Int,
    val backgroundChecked: Boolean,
    val businessVerified: Boolean = false,
    val ownershipVerified: Boolean = false,
    val squareFeet: Int,
    val furnished: Boolean,
    val hasPrivateBath: Boolean,
    val amenitiesJson: String, // Comma separated
    val latitude: Double,
    val longitude: Double,
    val walkScore: Int,
    val isFeatured: Boolean = false,
    val isSaved: Boolean = false
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: String,
    val listingId: Long,
    val listingTitle: String,
    val senderId: String,
    val senderName: String,
    val isLandlord: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = true,
    val tourBookingDate: String? = null
)

@Entity(tableName = "move_out_alerts")
data class MoveOutAlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listingId: Long,
    val listingTitle: String,
    val address: String,
    val neighborhood: String,
    val universityNearby: String,
    val moveOutDate: String,
    val monthlyPrice: Int,
    val leavingReason: String,
    val departingTenantName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val subscribersCount: Int = 12,
    val isSubscribed: Boolean = false
)

@Entity(tableName = "maintenance_tickets")
data class MaintenanceTicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listingId: Long,
    val propertyTitle: String,
    val unitNumber: String,
    val tenantName: String,
    val issueTitle: String,
    val description: String,
    val category: String, // Plumbing, Electrical, HVAC, Appliance, Lock/Key
    val urgency: String, // Urgent, Moderate, Low
    val status: String, // Submitted, In Progress, Scheduled, Resolved
    val createdAt: String,
    val scheduledDate: String? = null
)

@Entity(tableName = "payments")
data class PaymentRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listingId: Long,
    val propertyTitle: String,
    val tenantName: String,
    val amount: Double,
    val paymentType: String, // Monthly Rent, Security Deposit, Utilities Split, Landlord Subscription
    val status: String, // Completed, Pending, Processing
    val date: String,
    val transactionRef: String,
    val paymentMethod: String
)

@Entity(tableName = "forum_posts")
data class ForumPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorRole: String, // Student @ HNBGU, Urban Renter, Verified Landlord
    val university: String,
    val category: String, // Roommate Search, Sublease, Campus Advice, Furniture Swap, Safety
    val title: String,
    val content: String,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val timestamp: String,
    val isLiked: Boolean = false
)

@Entity(tableName = "community_events")
data class CommunityEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val location: String,
    val date: String,
    val time: String,
    val category: String, // Mixer, Workshop, Furniture Swap, Safety
    val description: String,
    val rsvpCount: Int = 24,
    val isRsvpd: Boolean = false
)

data class UserProfile(
    val id: String = "user_101",
    val name: String = "Alex Chen",
    val email: String = "alex.chen@nyu.edu",
    val university: String = "Srinagar University (HNBGU)",
    val isStudent: Boolean = true,
    val isEmailVerified: Boolean = true,
    val isPhoneVerified: Boolean = false,
    val isSocialLinked: Boolean = false,
    val isIdVerified: Boolean = true,
    val isBackgroundChecked: Boolean = true,
    val isStudentVerified: Boolean = true,
    val creditTier: String = "Excellent (740+)",
    val isLandlordMode: Boolean = false,
    val isBusinessVerified: Boolean = false,
    val isOwnershipVerified: Boolean = false,
    val landlordSubscription: String = "Pro Landlord (₹19.99/mo)"
)
