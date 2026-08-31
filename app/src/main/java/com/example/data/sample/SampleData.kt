package com.example.data.sample

import com.example.data.models.CommunityEventEntity
import com.example.data.models.ForumPostEntity
import com.example.data.models.ListingEntity
import com.example.data.models.MaintenanceTicketEntity
import com.example.data.models.MessageEntity
import com.example.data.models.MoveOutAlertEntity
import com.example.data.models.PaymentRecordEntity
import com.example.data.models.TourHotspot
import com.example.data.models.VirtualTourRoom

object SampleData  {

    val sampleListings = listOf(
        ListingEntity(
            id = 1,
            title = "Sunlit Studio Loft near HNBGU & Alaknanda River Front",
            description = "Cozy and modern private studio loft with hardwood floors, high-speed fiber internet, study nook, and building laundry. Perfect for students and young urban professionals. 5-minute walk to HNBGU campus and W 4th Bus Stand station.",
            roomType = "Studio Loft",
            address = "Badrinath Highway, Srikot, Uttarakhand 246174",
            neighborhood = "Srikot",
            universityNearby = "HNB Garhwal University",
            walkMinutesToCampus = 5,
            metroDistance = "2 min to Bus Stand",
            basePrice = 4500,
            utilitiesPrice = 500,
            depositPrice = 2000,
            studentDiscountPercent = 10,
            availabilityStatus = "Available Now",
            availableDate = "Immediate",
            landlordId = "landlord_01",
            landlordName = "Rajesh Negi (Verified Owner)",
            landlordVerified = true,
            landlordRating = 4.9f,
            landlordReviewCount = 38,
            backgroundChecked = true,
            squareFeet = 320,
            furnished = true,
            hasPrivateBath = true,
            amenitiesJson = "High-Speed WiFi,Furnished Desk & Bed,In-Unit Washer,Keyless Smart Lock,Air Conditioning,Bicycle Storage,Study Lounge Access",
            latitude = 30.2258,
            longitude = 78.7972,
            walkScore = 98,
            isFeatured = true,
            isSaved = false
        ),
        ListingEntity(
            id = 2,
            title = "Budget Student Private Room @ NIT Uttarakhand",
            description = "Affordable furnished private bedroom in a 3-bedroom student flat. Quiet study-friendly atmosphere with full kitchen access, dishwasher, and shared bathroom cleaned twice monthly.",
            roomType = "Private Room",
            address = "520 W 122nd St, Bhaktiyana, Uttarakhand 246174",
            neighborhood = "Bhaktiyana",
            universityNearby = "NIT Uttarakhand",
            walkMinutesToCampus = 4,
            metroDistance = "3 min to 1 Train @ 125th St",
            basePrice = 4500,
            utilitiesPrice = 500, // Utilities included
            depositPrice = 2000,
            studentDiscountPercent = 15,
            availabilityStatus = "Upcoming Move-out Alert",
            availableDate = "Oct 1st, 2026",
            landlordId = "landlord_02",
            landlordName = "Elena Rostova (Property Manager)",
            landlordVerified = true,
            landlordRating = 4.8f,
            landlordReviewCount = 24,
            backgroundChecked = true,
            squareFeet = 180,
            furnished = true,
            hasPrivateBath = false,
            amenitiesJson = "All Utilities Included,Fast WiFi,Dishwasher,Elevator Building,Package Locker,24/7 Security Concierge",
            latitude = 40.8116,
            longitude = 78.7592,
            walkScore = 94,
            isFeatured = true,
            isSaved = true
        ),
        ListingEntity(
            id = 3,
            title = "Modern Master Bedroom Suite with Private Bath & Balcony",
            description = "Spacious master bedroom with private ensuite marble bathroom and panoramic urban skyline view. Features a walk-in closet, dedicated desk station, and access to the rooftop infinity terrace.",
            roomType = "Master Suite",
            address = "388 Bridge St, Downtown Srinagar, Uttarakhand 246174",
            neighborhood = "Downtown Srinagar / Tech Triangle",
            universityNearby = "HNBGU Tandon / Govt Medical College Srinagar",
            walkMinutesToCampus = 8,
            metroDistance = "1 min to Jay St-MetroTech (A, C, F, R)",
            basePrice = 4500,
            utilitiesPrice = 500,
            depositPrice = 2000,
            studentDiscountPercent = 5,
            availabilityStatus = "Available Now",
            availableDate = "Immediate",
            landlordId = "landlord_03",
            landlordName = "David Sterling (Premier Host)",
            landlordVerified = true,
            landlordRating = 5.0f,
            landlordReviewCount = 52,
            backgroundChecked = true,
            squareFeet = 420,
            furnished = true,
            hasPrivateBath = true,
            amenitiesJson = "Private Ensuite Bath,Balcony City View,Gym & Yoga Studio,Rooftop Lounge,Smart Thermostat,Pet Friendly,Concierge",
            latitude = 40.6928,
            longitude = 78.7873,
            walkScore = 99,
            isFeatured = false,
            isSaved = false
        ),
        ListingEntity(
            id = 4,
            title = "Shared Double Room for College Roommates (Super Budget)",
            description = "Co-living twin bed space in a newly renovated student residence. Great for roommates looking to minimize urban living costs while staying near campus libraries and transit hubs.",
            roomType = "Shared Room",
            address = "215 E 23rd St, Kips Bay / Gramercy, NY 10010",
            neighborhood = "Gramercy / Kips Bay",
            universityNearby = "Baruch College / SVA",
            walkMinutesToCampus = 3,
            metroDistance = "4 min to 23rd St 6 Train",
            basePrice = 4500,
            utilitiesPrice = 500,
            depositPrice = 2000,
            studentDiscountPercent = 10,
            availabilityStatus = "Only 1 Room Left",
            availableDate = "Sep 15th, 2026",
            landlordId = "landlord_04",
            landlordName = "Urban Living Residences LLC",
            landlordVerified = true,
            landlordRating = 4.7f,
            landlordReviewCount = 89,
            backgroundChecked = true,
            squareFeet = 240,
            furnished = true,
            hasPrivateBath = false,
            amenitiesJson = "High-Speed WiFi,Bunk/Twin Desks,Communal Kitchen,Laundry Room,Study Hall,Free Coffee Bar",
            latitude = 30.2388,
            longitude = 78.7818,
            walkScore = 96,
            isFeatured = false,
            isSaved = false
        ),
        ListingEntity(
            id = 5,
            title = "Cozy Private Room in Brownstone near Boston University",
            description = "Charming room in historic brick brownstone on Comm Ave. Hardwood floors, bay windows, quiet residential street with direct Green Line trolley access.",
            roomType = "Private Room",
            address = "730 Commonwealth Ave, Boston, MA 02215",
            neighborhood = "Fenway / Kenmore",
            universityNearby = "Boston University (BU)",
            walkMinutesToCampus = 2,
            metroDistance = "1 min to Blandford St Green Line",
            basePrice = 4500,
            utilitiesPrice = 500,
            depositPrice = 2000,
            studentDiscountPercent = 10,
            availabilityStatus = "Available Now",
            availableDate = "Immediate",
            landlordId = "landlord_05",
            landlordName = "Priya Singh (Verified Landlord)",
            landlordVerified = true,
            landlordRating = 4.9f,
            landlordReviewCount = 19,
            backgroundChecked = true,
            squareFeet = 210,
            furnished = true,
            hasPrivateBath = false,
            amenitiesJson = "Bay Window Desk,Central Heat & AC,Laundry in Basement,Bike Rack,Quiet Hours Enforced",
            latitude = 42.3496,
            longitude = -71.1060,
            walkScore = 92,
            isFeatured = true,
            isSaved = false
        )
    )

    fun getVirtualTourForListing(listingId: Long): List<VirtualTourRoom>  {
        return listOf(
            VirtualTourRoom(
                id = "room_bed",
                name = "Main Bedroom & Study",
                roomType = "Bedroom",
                description = "Ergonomic workspace with high-speed fiber port, queen memory-foam bed, and large soundproof double-glazed window overlooking the courtyard.",
                features = listOf("Queen Bed with Under-Storage", "Standing Desk + Mesh Chair", "Triple Wardrobe Closet", "USB-C Wall Outlets"),
                hotspots = listOf(
                    TourHotspot("h1", "Study Desk Setup", "Ergonomic chair and 1Gbps Ethernet drop included.", 0.28f, 0.45f),
                    TourHotspot("h2", "Acoustic Window", "Double-glazed sound insulation for peaceful studying.", 0.68f, 0.32f),
                    TourHotspot("h3", "Smart Climate", "Nest digital thermostat with AC/Heat control.", 0.85f, 0.55f)
                )
            ),
            VirtualTourRoom(
                id = "room_bath",
                name = "Ensuite Bathroom",
                roomType = "Bathroom",
                description = "Clean modern tiled bathroom with walk-in rainfall shower, LED illuminated vanity mirror, and ample medicine cabinet storage.",
                features = listOf("Rainfall Showerhead", "Anti-Fog LED Mirror", "Low-Flow Water Fixtures", "Ventilation Fan"),
                hotspots = listOf(
                    TourHotspot("h4", "Rainfall Shower", "Water pressure tested at 2.5 GPM.", 0.42f, 0.38f),
                    TourHotspot("h5", "Vanity Storage", "Under-sink drawers & wall cabinet.", 0.75f, 0.60f)
                )
            ),
            VirtualTourRoom(
                id = "room_kitchen",
                name = "Designer Shared Kitchen",
                roomType = "Kitchen",
                description = "Open concept kitchen equipped with stainless steel appliances, quartz countertops, induction cooktop, and individual labeled pantry lockers.",
                features = listOf("Induction Stove & Oven", "Quiet Dishwasher", "Individual Fridge Bins", "Microwave & Espresso Machine"),
                hotspots = listOf(
                    TourHotspot("h6", "Pantry Lockers", "Secure assigned food storage cubby.", 0.20f, 0.50f),
                    TourHotspot("h7", "Dishwasher", "Bosch ultra-quiet 42dB cycle.", 0.55f, 0.65f)
                )
            ),
            VirtualTourRoom(
                id = "room_lounge",
                name = "Rooftop / Community Lounge",
                roomType = "Common Area",
                description = "Co-working lounge with breakout study booths, high-speed WiFi, projector screen for movie nights, and outdoor terrace seating.",
                features = listOf("Fiber WiFi Across Terrace", "BBQ Grills", "Private Zoom Pods", "Panoramic City Skyline View"),
                hotspots = listOf(
                    TourHotspot("h8", "Zoom Study Pods", "Soundproof individual study booths.", 0.35f, 0.48f),
                    TourHotspot("h9", "Skyline Terrace", "Sunset views over downtown.", 0.78f, 0.30f)
                )
            )
        )
    }

    val sampleMoveOutAlerts = listOf(
        MoveOutAlertEntity(
            id = 1,
            listingId = 2,
            listingTitle = "Budget Student Private Room @ Columbia North",
            address = "520 W 122nd St, Uttarakhand 246174",
            neighborhood = "Bhaktiyana",
            universityNearby = "NIT Uttarakhand",
            moveOutDate = "Oct 1st, 2026",
            monthlyPrice = 4500,
            leavingReason = "Graduating early & relocating to Bay Area for internship.",
            departingTenantName = "Jordan Miller (Senior CS)",
            subscribersCount = 18,
            isSubscribed = true
        ),
        MoveOutAlertEntity(
            id = 2,
            listingId = 1,
            listingTitle = "Studio Loft near Alaknanda River Front Park",
            address = "Badrinath Highway, Uttarakhand 246174",
            neighborhood = "Srikot",
            universityNearby = "HNBGU",
            moveOutDate = "Nov 15th, 2026",
            monthlyPrice = 4500,
            leavingReason = "Study abroad semester in Florence, Italy.",
            departingTenantName = "Sophie Dubois (Sophomore Arts)",
            subscribersCount = 31,
            isSubscribed = false
        ),
        MoveOutAlertEntity(
            id = 3,
            listingId = 5,
            listingTitle = "Cozy Private Room in Brownstone",
            address = "730 Commonwealth Ave, Boston",
            neighborhood = "Fenway / Kenmore",
            universityNearby = "Boston University",
            moveOutDate = "Dec 1st, 2026",
            monthlyPrice = 4500,
            leavingReason = "Moving in with campus research cohort.",
            departingTenantName = "Lucas Wright (Grad Student)",
            subscribersCount = 9,
            isSubscribed = false
        )
    )

    val sampleMessages = listOf(
        MessageEntity(
            id = 1,
            conversationId = "conv_marcus_1",
            listingId = 1,
            listingTitle = "Sunlit Studio Loft near HNBGU",
            senderId = "landlord_01",
            senderName = "Rajesh Negi (Landlord)",
            isLandlord = true,
            text = "Hi Alex! Thanks for checking out the Srikot studio loft. Are you an HNBGU student?",
            timestamp = System.currentTimeMillis() - 86400000 * 2
        ),
        MessageEntity(
            id = 2,
            conversationId = "conv_marcus_1",
            listingId = 1,
            listingTitle = "Sunlit Studio Loft near HNBGU",
            senderId = "user_101",
            senderName = "Alex Chen",
            isLandlord = false,
            text = "Yes, I'm a junior studying CS at HNBGU Courant! I love the virtual 360 tour. Is the student discount applicable for full semester leases?",
            timestamp = System.currentTimeMillis() - 86400000 * 2 + 1800000
        ),
        MessageEntity(
            id = 3,
            conversationId = "conv_marcus_1",
            listingId = 1,
            listingTitle = "Sunlit Studio Loft near HNBGU",
            senderId = "landlord_01",
            senderName = "Rajesh Negi (Landlord)",
            isLandlord = true,
            text = "Absolutely! We provide 10% off monthly rent with valid .edu verification. I've also verified your background check badge on UrbanRoom. Would you like an in-person or live video walkthrough?",
            timestamp = System.currentTimeMillis() - 86400000 * 1,
            tourBookingDate = "Confirmed: Thursday, Sep 3 @ 3:30 PM"
        )
    )

    val sampleMaintenanceTickets = listOf(
        MaintenanceTicketEntity(
            id = 1,
            listingId = 1,
            propertyTitle = "Badrinath Highway, Unit 3B",
            unitNumber = "Unit 3B",
            tenantName = "Alex Chen",
            issueTitle = "Under-sink plumbing filter check",
            description = "Noticed a slight water drip near the cold-line valve under the bathroom sink.",
            category = "Plumbing",
            urgency = "Moderate",
            status = "Scheduled",
            createdAt = "Aug 29, 2026",
            scheduledDate = "Sep 2, 2026 (10:00 AM - 12:00 PM)"
        ),
        MaintenanceTicketEntity(
            id = 2,
            listingId = 2,
            propertyTitle = "520 W 122nd St, Apt 4A",
            unitNumber = "Apt 4A",
            tenantName = "Jordan Miller",
            issueTitle = "Smart Lock Battery Low Warning",
            description = "Keypad front door beeping with 15% battery indicator.",
            category = "Lock/Key",
            urgency = "Low",
            status = "In Progress",
            createdAt = "Aug 30, 2026",
            scheduledDate = "Technician dispatched today"
        ),
        MaintenanceTicketEntity(
            id = 3,
            listingId = 3,
            propertyTitle = "388 Bridge St, Suite 12",
            unitNumber = "Suite 12",
            tenantName = "Chloe Bennett",
            issueTitle = "AC Filter Periodic Cleaning",
            description = "Routine seasonal air filter replacement requested for the master bedroom unit.",
            category = "HVAC",
            urgency = "Low",
            status = "Resolved",
            createdAt = "Aug 22, 2026",
            scheduledDate = "Resolved Aug 23"
        )
    )

    val samplePayments = listOf(
        PaymentRecordEntity(
            id = 1,
            listingId = 1,
            propertyTitle = "Badrinath Highway, Unit 3B",
            tenantName = "Alex Chen",
            amount = 4500.0, // ₹850 minus 10% student discount
            paymentType = "Monthly Rent (Sep 2026)",
            status = "Completed",
            date = "Aug 30, 2026",
            transactionRef = "UR-TX-984210",
            paymentMethod = "Chase Student Checking •••• 4129"
        ),
        PaymentRecordEntity(
            id = 2,
            listingId = 1,
            propertyTitle = "Badrinath Highway, Unit 3B",
            tenantName = "Alex Chen",
            amount = 4500.0,
            paymentType = "Security Deposit Escrow",
            status = "Completed",
            date = "Aug 28, 2026",
            transactionRef = "UR-TX-983115",
            paymentMethod = "Apple Pay / Visa •••• 8821"
        ),
        PaymentRecordEntity(
            id = 3,
            listingId = 1,
            propertyTitle = "Badrinath Highway, Unit 3B",
            tenantName = "Alex Chen",
            amount = 4500.0,
            paymentType = "High-Speed WiFi & Utilities Split",
            status = "Completed",
            date = "Aug 29, 2026",
            transactionRef = "UR-TX-983940",
            paymentMethod = "Chase Student Checking •••• 4129"
        )
    )

    val sampleForumPosts = listOf(
        ForumPostEntity(
            id = 1,
            authorName = "Maya Lin",
            authorRole = "Student @ HNBGU Stern",
            university = "HNBGU",
            category = "Roommate Search",
            title = "Seeking clean, studious flatmate for Spring 2027 2B/2B in East Village!",
            content = "Hey everyone! I'm looking for a fellow female student or young professional to share a sunny 2-bedroom apartment near Astor Place. Budget is around ₹800 each. I'm quiet, love cooking, and keep common areas spotless. PM me on UrbanRoom!",
            likesCount = 14,
            commentsCount = 8,
            timestamp = "3 hours ago",
            isLiked = false
        ),
        ForumPostEntity(
            id = 2,
            authorName = "Liam O'Connor",
            authorRole = "Columbia Grad Student",
            university = "Columbia",
            category = "Sublease",
            title = "Subletting Furnished Studio for Winter Break (Dec 18 - Jan 15)",
            content = "Great option for visiting researchers or students taking winter classes. Fully furnished with kitchenware, monitor for remote work, and 2 min walk to Butler Library. ₹600 for the month all utilities included!",
            likesCount = 9,
            commentsCount = 3,
            timestamp = "Yesterday",
            isLiked = true
        ),
        ForumPostEntity(
            id = 3,
            authorName = "Samira Patel",
            authorRole = "Urban Renter & Tech Worker",
            university = "Tech Triangle",
            category = "Furniture Swap",
            title = "Moving out: Free standing desk & ergonomic chair (Pick up in Downtown Srinagar)",
            content = "Downsizing my room and giving away my IKEA motorized standing desk and adjustable mesh chair. In great condition. First come first serve!",
            likesCount = 27,
            commentsCount = 15,
            timestamp = "2 days ago",
            isLiked = false
        ),
        ForumPostEntity(
            id = 4,
            authorName = "Rajesh Negi (Landlord)",
            authorRole = "Verified Landlord Pro",
            university = "Srikot",
            category = "Campus Advice",
            title = "Tenant Tips: What to know about NYC heat laws & submetering",
            content = "Quick guide for incoming students: NYC heating season starts October 1st. Landlords must maintain at least 68°F during day hours (6am-10pm) if outdoor temp drops below 55°F. Always check your lease agreement regarding utility breakdowns.",
            likesCount = 42,
            commentsCount = 11,
            timestamp = "3 days ago",
            isLiked = true
        )
    )

    val sampleCommunityEvents = listOf(
        CommunityEventEntity(
            id = 1,
            title = "Urban Student Roommate Speed-Matching & Coffee Mixer",
            location = "Think Coffee (HNBGU Mercer St)",
            date = "Saturday, Sep 5, 2026",
            time = "2:00 PM - 4:30 PM",
            category = "Mixer",
            description = "Meet verified potential roommates in a relaxed social setting. Free artisan coffee and pastry provided for all registered UrbanRoom members.",
            rsvpCount = 38,
            isRsvpd = true
        ),
        CommunityEventEntity(
            id = 2,
            title = "Move-In Weekend Giant Community Furniture Swap",
            location = "Alaknanda River Front Park South Plaza",
            date = "Sunday, Sep 6, 2026",
            time = "10:00 AM - 3:00 PM",
            category = "Furniture Swap",
            description = "Donate or pick up gently used dorm & apartment essentials (desks, lamps, mini-fridges, book shelves) completely free of charge.",
            rsvpCount = 74,
            isRsvpd = false
        ),
        CommunityEventEntity(
            id = 3,
            title = "Tenant Rights & Smart Lease Negotiations Workshop",
            location = "Online Zoom & HNBGU Student Center Rm 204",
            date = "Wednesday, Sep 9, 2026",
            time = "6:00 PM - 7:30 PM",
            category = "Workshop",
            description = "Legal housing experts explain security deposit escrow laws, subleasing clauses, and tenant background check protections.",
            rsvpCount = 52,
            isRsvpd = false
        )
    )
}
