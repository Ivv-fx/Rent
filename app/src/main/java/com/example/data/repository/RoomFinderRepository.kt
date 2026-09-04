package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.models.CommunityEventEntity
import com.example.data.models.ForumPostEntity
import com.example.data.models.ListingEntity
import com.example.data.models.MaintenanceTicketEntity
import com.example.data.models.MessageEntity
import com.example.data.models.MoveOutAlertEntity
import com.example.data.models.PaymentRecordEntity
import com.example.data.models.UserProfile
import com.example.data.sample.SampleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoomFinderRepository(private val db: AppDatabase)  {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile = _userProfile.asStateFlow()

    init {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            com.clerk.api.Clerk.userFlow.collect { user ->
                if (user != null) {
                    val current = _userProfile.value
                    val emailAddress = user.primaryEmailAddress?.emailAddress ?: ""
                    val fullName = "${user.firstName ?: ""} ${user.lastName ?: ""}".trim().ifBlank { 
                        emailAddress.substringBefore("@").replaceFirstChar { it.uppercase() } 
                    }
                    _userProfile.value = current.copy(
                        id = user.id,
                        email = emailAddress,
                        name = fullName.ifBlank { "User" }
                    )
                }
            }
        }
    }

    val listings: Flow<List<ListingEntity>> = db.listingDao().getAllListings()
    val savedListings: Flow<List<ListingEntity>> = db.listingDao().getSavedListings()
    val moveOutAlerts: Flow<List<MoveOutAlertEntity>> = db.moveOutAlertDao().getAllAlerts()
    val messages: Flow<List<MessageEntity>> = db.messageDao().getAllMessages()
    val maintenanceTickets: Flow<List<MaintenanceTicketEntity>> = db.maintenanceDao().getAllTickets()
    val payments: Flow<List<PaymentRecordEntity>> = db.paymentDao().getAllPayments()
    val forumPosts: Flow<List<ForumPostEntity>> = db.forumDao().getAllPosts()
    val communityEvents: Flow<List<CommunityEventEntity>> = db.forumDao().getAllEvents()

    fun getListingById(id: Long): Flow<ListingEntity?> = db.listingDao().getListingById(id)

    fun getMessagesForConversation(convId: String): Flow<List<MessageEntity>> =
        db.messageDao().getMessagesForConversation(convId)

    suspend fun seedDatabaseIfEmpty()  {
        val existingListings = db.listingDao().getAllListings().first()
        if (existingListings.isEmpty())  {
            db.listingDao().insertAllListings(SampleData.sampleListings)
            db.moveOutAlertDao().insertAllAlerts(SampleData.sampleMoveOutAlerts)
            db.messageDao().insertAllMessages(SampleData.sampleMessages)
            db.maintenanceDao().insertAllTickets(SampleData.sampleMaintenanceTickets)
            db.paymentDao().insertAllPayments(SampleData.samplePayments)
            db.forumDao().insertAllPosts(SampleData.sampleForumPosts)
            db.forumDao().insertAllEvents(SampleData.sampleCommunityEvents)
        }
    }

    suspend fun toggleSaveListing(id: Long, isSaved: Boolean)  {
        db.listingDao().updateSavedStatus(id, !isSaved)
    }

    suspend fun sendMessage(
        conversationId: String,
        listingId: Long,
        listingTitle: String,
        text: String,
        isLandlord: Boolean = false,
        tourBookingDate: String? = null
    )  {
        val msg = MessageEntity(
            conversationId = conversationId,
            listingId = listingId,
            listingTitle = listingTitle,
            senderId = if (isLandlord) "landlord_01" else _userProfile.value.id,
            senderName = if (isLandlord) "Rajesh Negi (Landlord)" else _userProfile.value.name,
            isLandlord = isLandlord,
            text = text,
            tourBookingDate = tourBookingDate
        )
        db.messageDao().insertMessage(msg)
    }

    suspend fun submitMoveOutNotice(
        listingId: Long,
        listingTitle: String,
        address: String,
        neighborhood: String,
        universityNearby: String,
        moveOutDate: String,
        monthlyPrice: Int,
        leavingReason: String
    )  {
        // 1. Update listing availability status to Move-out alert
        db.listingDao().updateAvailability(listingId, "Upcoming Move-out Alert")

        // 2. Broadcast move-out alert to community
        val alert = MoveOutAlertEntity(
            listingId = listingId,
            listingTitle = listingTitle,
            address = address,
            neighborhood = neighborhood,
            universityNearby = universityNearby,
            moveOutDate = moveOutDate,
            monthlyPrice = monthlyPrice,
            leavingReason = leavingReason,
            departingTenantName = _userProfile.value.name,
            subscribersCount = 1,
            isSubscribed = true
        )
        db.moveOutAlertDao().insertAlert(alert)

        // 3. Notify Landlord in direct messages
        sendMessage(
            conversationId = "conv_marcus_1",
            listingId = listingId,
            listingTitle = listingTitle,
            text = "Official Move-Out Notice: I will be leaving on $moveOutDate. Reason: $leavingReason. Automated vacancy listing has been published on UrbanRoom.",
            isLandlord = false
        )
    }

    suspend fun toggleMoveOutSubscription(id: Long, currentSubscribed: Boolean)  {
        db.moveOutAlertDao().toggleSubscription(id, !currentSubscribed)
    }

    suspend fun createMaintenanceTicket(
        listingId: Long,
        propertyTitle: String,
        unitNumber: String,
        issueTitle: String,
        description: String,
        category: String,
        urgency: String
    )  {
        val ticket = MaintenanceTicketEntity(
            listingId = listingId,
            propertyTitle = propertyTitle,
            unitNumber = unitNumber,
            tenantName = _userProfile.value.name,
            issueTitle = issueTitle,
            description = description,
            category = category,
            urgency = urgency,
            status = "Submitted",
            createdAt = "Just now"
        )
        db.maintenanceDao().insertTicket(ticket)
    }

    suspend fun updateMaintenanceStatus(ticketId: Long, newStatus: String)  {
        db.maintenanceDao().updateStatus(ticketId, newStatus)
    }

    suspend fun processPayment(
        listingId: Long,
        propertyTitle: String,
        amount: Double,
        paymentType: String,
        paymentMethod: String
    )  {
        val payment = PaymentRecordEntity(
            listingId = listingId,
            propertyTitle = propertyTitle,
            tenantName = _userProfile.value.name,
            amount = amount,
            paymentType = paymentType,
            status = "Completed",
            date = "Today",
            transactionRef = "UR-TX-${(100000..999999).random()}",
            paymentMethod = paymentMethod
        )
        db.paymentDao().insertPayment(payment)
    }

    suspend fun createForumPost(
        title: String,
        category: String,
        content: String
    )  {
        val post = ForumPostEntity(
            authorName = _userProfile.value.name,
            authorRole = if (_userProfile.value.isLandlordMode) "Verified Landlord Pro" else "Student @ HNBGU",
            university = _userProfile.value.university,
            category = category,
            title = title,
            content = content,
            likesCount = 0,
            commentsCount = 0,
            timestamp = "Just now",
            isLiked = false
        )
        db.forumDao().insertPost(post)
    }

    suspend fun toggleLikePost(id: Long, isLiked: Boolean, currentLikes: Int)  {
        val newLikes = if (isLiked) currentLikes - 1 else currentLikes + 1
        db.forumDao().updateLike(id, !isLiked, newLikes)
    }

    suspend fun toggleEventRsvp(id: Long, isRsvpd: Boolean, currentCount: Int)  {
        val newCount = if (isRsvpd) currentCount - 1 else currentCount + 1
        db.forumDao().updateRsvp(id, !isRsvpd, newCount)
    }

    suspend fun addLandlordListing(
        title: String,
        description: String,
        roomType: String,
        address: String,
        neighborhood: String,
        universityNearby: String,
        walkMinutesToCampus: Int,
        basePrice: Int,
        utilitiesPrice: Int,
        depositPrice: Int,
        studentDiscountPercent: Int,
        squareFeet: Int,
        furnished: Boolean,
        hasPrivateBath: Boolean,
        amenities: String
    )  {
        val listing = ListingEntity(
            title = title,
            description = description,
            roomType = roomType,
            address = address,
            neighborhood = neighborhood,
            universityNearby = universityNearby,
            walkMinutesToCampus = walkMinutesToCampus,
            metroDistance = "Nearby bus stand",
            basePrice = basePrice,
            utilitiesPrice = utilitiesPrice,
            depositPrice = depositPrice,
            studentDiscountPercent = studentDiscountPercent,
            availabilityStatus = "Available Now",
            availableDate = "Immediate",
            landlordId = _userProfile.value.id,
            landlordName = _userProfile.value.name + " (Verified Landlord)",
            landlordVerified = true,
            landlordRating = 5.0f,
            landlordReviewCount = 1,
            backgroundChecked = true,
            squareFeet = squareFeet,
            furnished = furnished,
            hasPrivateBath = hasPrivateBath,
            amenitiesJson = amenities,
            latitude = 30.2306,
            longitude = 78.7352,
            walkScore = 95,
            isFeatured = false
        )
        db.listingDao().insertListing(listing)
    }

    fun updateUserProfile(name: String, email: String, university: String, creditTier: String)  {
        val current = _userProfile.value
        _userProfile.value = current.copy(
            name = name,
            email = email,
            university = university,
            creditTier = creditTier
        )
    }

    fun verifyEmail() {
        _userProfile.value = _userProfile.value.copy(isEmailVerified = true)
    }
    fun verifyPhone() {
        _userProfile.value = _userProfile.value.copy(isPhoneVerified = true)
    }
    fun linkSocial() {
        _userProfile.value = _userProfile.value.copy(isSocialLinked = true)
    }
    fun verifyId() {
        _userProfile.value = _userProfile.value.copy(isIdVerified = true)
    }
    fun verifyStudent() {
        _userProfile.value = _userProfile.value.copy(isStudentVerified = true)
    }
    fun verifyBackground() {
        _userProfile.value = _userProfile.value.copy(isBackgroundChecked = true)
    }
    fun verifyBusiness() {
        _userProfile.value = _userProfile.value.copy(isBusinessVerified = true)
    }
    fun verifyOwnership() {
        _userProfile.value = _userProfile.value.copy(isOwnershipVerified = true)
    }

    fun toggleLandlordMode()  {
        val current = _userProfile.value
        _userProfile.value = current.copy(isLandlordMode = !current.isLandlordMode)
    }

    fun updateSubscriptionTier(tier: String)  {
        val current = _userProfile.value
        _userProfile.value = current.copy(landlordSubscription = tier)
    }
}
