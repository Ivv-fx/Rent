package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.models.CommunityEventEntity
import com.example.data.models.ForumPostEntity
import com.example.data.models.ListingEntity
import com.example.data.models.MaintenanceTicketEntity
import com.example.data.models.MessageEntity
import com.example.data.models.MoveOutAlertEntity
import com.example.data.models.PaymentRecordEntity
import com.example.data.models.PriceCategory
import com.example.data.models.UserProfile
import com.example.data.repository.RoomFinderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoomFinderViewModel(
    private val repository: RoomFinderRepository
) : ViewModel()  {

    init  {
        viewModelScope.launch  {
            repository.seedDatabaseIfEmpty()
        }
    }

    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    fun login(email: String, password: String)  {
        viewModelScope.launch  {
            _authError.value = null
            try  {
                val publishableKey = com.example.BuildConfig.NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY
                val clerkApi = com.example.data.api.ClerkAuthUtils.createClerkApi(publishableKey)
                val request = com.example.data.api.ClerkSignInRequest(
                    identifier = email,
                    password = password
                )
                
                val response = clerkApi.createSignIn(request = request)
                if (response.isSuccessful) {
                    println("Clerk API Sign-In success: ${response.code()}")
                    repository.login(email)
                } else {
                    println("Clerk API Error: ${response.errorBody()?.string()}")
                    _authError.value = "Invalid credentials. Please try again."
                }
            } catch (e: Exception)  {
                println("Clerk API Exception: ${e.message}")
                _authError.value = "Network error connecting to authentication service."
            }
        }
    }

    fun logout()  {
        repository.logout()
    }

    val userProfile: StateFlow<UserProfile> = repository.userProfile

    val rawListings: StateFlow<List<ListingEntity>> = repository.listings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedListings: StateFlow<List<ListingEntity>> = repository.savedListings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val moveOutAlerts: StateFlow<List<MoveOutAlertEntity>> = repository.moveOutAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val messages: StateFlow<List<MessageEntity>> = repository.messages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val maintenanceTickets: StateFlow<List<MaintenanceTicketEntity>> = repository.maintenanceTickets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val payments: StateFlow<List<PaymentRecordEntity>> = repository.payments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val forumPosts: StateFlow<List<ForumPostEntity>> = repository.forumPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val communityEvents: StateFlow<List<CommunityEventEntity>> = repository.communityEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and filter state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedPriceCategory = MutableStateFlow(PriceCategory.ALL)
    val selectedPriceCategory: StateFlow<PriceCategory> = _selectedPriceCategory.asStateFlow()

    private val _selectedRoomType = MutableStateFlow<String?>("All")
    val selectedRoomType: StateFlow<String?> = _selectedRoomType.asStateFlow()

    private val _selectedUniversityFilter = MutableStateFlow("All Campuses")
    val selectedUniversityFilter: StateFlow<String> = _selectedUniversityFilter.asStateFlow()

    private val _onlyVerifiedLandlords = MutableStateFlow(false)
    val onlyVerifiedLandlords: StateFlow<Boolean> = _onlyVerifiedLandlords.asStateFlow()

    private data class FilterParams(
        val query: String,
        val priceCat: PriceCategory,
        val roomType: String?,
        val uniFilter: String,
        val onlyVerified: Boolean
    )

    private val filterParamsFlow = combine(
        _searchQuery,
        _selectedPriceCategory,
        _selectedRoomType,
        _selectedUniversityFilter,
        _onlyVerifiedLandlords
    )  { query, priceCat, roomType, uniFilter, onlyVerified ->
        FilterParams(query, priceCat, roomType, uniFilter, onlyVerified)
    }

    val filteredListings: StateFlow<List<ListingEntity>> = combine(
        rawListings,
        filterParamsFlow
    )  { listings, params ->
        listings.filter  { listing ->
            val matchesQuery = params.query.isBlank() ||
                    listing.title.contains(params.query, ignoreCase = true) ||
                    listing.address.contains(params.query, ignoreCase = true) ||
                    listing.neighborhood.contains(params.query, ignoreCase = true) ||
                    listing.universityNearby.contains(params.query, ignoreCase = true)

            val matchesPrice = listing.basePrice >= params.priceCat.minPrice && listing.basePrice <= params.priceCat.maxPrice

            val matchesRoomType = params.roomType == null || params.roomType == "All" || listing.roomType.equals(params.roomType, ignoreCase = true)

            val matchesUni = params.uniFilter == "All Campuses" || listing.universityNearby.contains(params.uniFilter, ignoreCase = true)

            val matchesVerified = !params.onlyVerified || (listing.landlordVerified && listing.backgroundChecked)

            matchesQuery && matchesPrice && matchesRoomType && matchesUni && matchesVerified
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filter controls
    fun setSearchQuery(query: String)  {
        _searchQuery.value = query
    }

    fun setPriceCategory(cat: PriceCategory)  {
        _selectedPriceCategory.value = cat
    }

    fun setRoomType(type: String?)  {
        _selectedRoomType.value = type
    }

    fun setUniversityFilter(uni: String)  {
        _selectedUniversityFilter.value = uni
    }

    fun toggleVerifiedLandlordsOnly()  {
        _onlyVerifiedLandlords.value = !_onlyVerifiedLandlords.value
    }

    fun toggleSaveListing(id: Long, isSaved: Boolean)  {
        viewModelScope.launch  {
            repository.toggleSaveListing(id, isSaved)
        }
    }

    fun sendMessage(
        listingId: Long,
        listingTitle: String,
        text: String,
        tourDate: String? = null
    )  {
        viewModelScope.launch  {
            repository.sendMessage(
                conversationId = "conv_marcus_1",
                listingId = listingId,
                listingTitle = listingTitle,
                text = text,
                isLandlord = false,
                tourBookingDate = tourDate
            )
        }
    }

    fun submitMoveOutNotice(
        listingId: Long,
        listingTitle: String,
        address: String,
        neighborhood: String,
        universityNearby: String,
        moveOutDate: String,
        monthlyPrice: Int,
        leavingReason: String
    )  {
        viewModelScope.launch  {
            repository.submitMoveOutNotice(
                listingId = listingId,
                listingTitle = listingTitle,
                address = address,
                neighborhood = neighborhood,
                universityNearby = universityNearby,
                moveOutDate = moveOutDate,
                monthlyPrice = monthlyPrice,
                leavingReason = leavingReason
            )
        }
    }

    fun toggleMoveOutSubscription(id: Long, currentSubscribed: Boolean)  {
        viewModelScope.launch  {
            repository.toggleMoveOutSubscription(id, currentSubscribed)
        }
    }

    fun createMaintenanceTicket(
        listingId: Long,
        propertyTitle: String,
        unitNumber: String,
        issueTitle: String,
        description: String,
        category: String,
        urgency: String
    )  {
        viewModelScope.launch  {
            repository.createMaintenanceTicket(
                listingId = listingId,
                propertyTitle = propertyTitle,
                unitNumber = unitNumber,
                issueTitle = issueTitle,
                description = description,
                category = category,
                urgency = urgency
            )
        }
    }

    fun updateMaintenanceStatus(ticketId: Long, newStatus: String)  {
        viewModelScope.launch  {
            repository.updateMaintenanceStatus(ticketId, newStatus)
        }
    }

    fun processPayment(
        listingId: Long,
        propertyTitle: String,
        amount: Double,
        paymentType: String,
        paymentMethod: String
    )  {
        viewModelScope.launch  {
            repository.processPayment(
                listingId = listingId,
                propertyTitle = propertyTitle,
                amount = amount,
                paymentType = paymentType,
                paymentMethod = paymentMethod
            )
        }
    }

    fun createForumPost(title: String, category: String, content: String)  {
        viewModelScope.launch  {
            repository.createForumPost(title, category, content)
        }
    }

    fun toggleLikePost(id: Long, isLiked: Boolean, currentLikes: Int)  {
        viewModelScope.launch  {
            repository.toggleLikePost(id, isLiked, currentLikes)
        }
    }

    fun toggleEventRsvp(id: Long, isRsvpd: Boolean, currentCount: Int)  {
        viewModelScope.launch  {
            repository.toggleEventRsvp(id, isRsvpd, currentCount)
        }
    }

    fun addLandlordListing(
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
        viewModelScope.launch  {
            repository.addLandlordListing(
                title = title,
                description = description,
                roomType = roomType,
                address = address,
                neighborhood = neighborhood,
                universityNearby = universityNearby,
                walkMinutesToCampus = walkMinutesToCampus,
                basePrice = basePrice,
                utilitiesPrice = utilitiesPrice,
                depositPrice = depositPrice,
                studentDiscountPercent = studentDiscountPercent,
                squareFeet = squareFeet,
                furnished = furnished,
                hasPrivateBath = hasPrivateBath,
                amenities = amenities
            )
        }
    }

    fun updateUserProfile(name: String, email: String, university: String, creditTier: String)  {
        repository.updateUserProfile(name, email, university, creditTier)
    }

    fun verifyEmail() = repository.verifyEmail()
    fun verifyPhone() = repository.verifyPhone()
    fun linkSocial() = repository.linkSocial()
    fun verifyId() = repository.verifyId()
    fun verifyStudent() = repository.verifyStudent()
    fun verifyBackground() = repository.verifyBackground()
    fun verifyBusiness() = repository.verifyBusiness()
    fun verifyOwnership() = repository.verifyOwnership()

    fun toggleLandlordMode()  {
        repository.toggleLandlordMode()
    }

    fun updateSubscriptionTier(tier: String)  {
        repository.updateSubscriptionTier(tier)
    }
}

class RoomFinderViewModelFactory(private val repository: RoomFinderRepository) :
    ViewModelProvider.Factory  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T  {
        if (modelClass.isAssignableFrom(RoomFinderViewModel::class.java))  {
            return RoomFinderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
