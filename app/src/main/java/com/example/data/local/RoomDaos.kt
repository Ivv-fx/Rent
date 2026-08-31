package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.CommunityEventEntity
import com.example.data.models.ForumPostEntity
import com.example.data.models.ListingEntity
import com.example.data.models.MaintenanceTicketEntity
import com.example.data.models.MessageEntity
import com.example.data.models.MoveOutAlertEntity
import com.example.data.models.PaymentRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao  {
    @Query("SELECT * FROM listings ORDER BY isFeatured DESC, id ASC")
    fun getAllListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE id = :id LIMIT 1")
    fun getListingById(id: Long): Flow<ListingEntity?>

    @Query("SELECT * FROM listings WHERE isSaved = 1")
    fun getSavedListings(): Flow<List<ListingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllListings(listings: List<ListingEntity>)

    @Query("UPDATE listings SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSavedStatus(id: Long, isSaved: Boolean)

    @Query("UPDATE listings SET availabilityStatus = :status WHERE id = :id")
    suspend fun updateAvailability(id: Long, status: String)

    @Query("DELETE FROM listings WHERE id = :id")
    suspend fun deleteListing(id: Long)
}

@Dao
interface MessageDao  {
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE conversationId = :convId ORDER BY timestamp ASC")
    fun getMessagesForConversation(convId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMessages(messages: List<MessageEntity>)
}

@Dao
interface MoveOutAlertDao  {
    @Query("SELECT * FROM move_out_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<MoveOutAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: MoveOutAlertEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlerts(alerts: List<MoveOutAlertEntity>)

    @Query("UPDATE move_out_alerts SET isSubscribed = :isSubscribed, subscribersCount = subscribersCount + (CASE WHEN :isSubscribed = 1 THEN 1 ELSE -1 END) WHERE id = :id")
    suspend fun toggleSubscription(id: Long, isSubscribed: Boolean)
}

@Dao
interface MaintenanceDao  {
    @Query("SELECT * FROM maintenance_tickets ORDER BY id DESC")
    fun getAllTickets(): Flow<List<MaintenanceTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: MaintenanceTicketEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTickets(tickets: List<MaintenanceTicketEntity>)

    @Query("UPDATE maintenance_tickets SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
}

@Dao
interface PaymentDao  {
    @Query("SELECT * FROM payments ORDER BY id DESC")
    fun getAllPayments(): Flow<List<PaymentRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPayments(payments: List<PaymentRecordEntity>)
}

@Dao
interface ForumDao  {
    @Query("SELECT * FROM forum_posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<ForumPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ForumPostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(posts: List<ForumPostEntity>)

    @Query("UPDATE forum_posts SET isLiked = :isLiked, likesCount = :newLikesCount WHERE id = :id")
    suspend fun updateLike(id: Long, isLiked: Boolean, newLikesCount: Int)

    @Query("SELECT * FROM community_events ORDER BY id ASC")
    fun getAllEvents(): Flow<List<CommunityEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CommunityEventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEvents(events: List<CommunityEventEntity>)

    @Query("UPDATE community_events SET isRsvpd = :isRsvpd, rsvpCount = :newRsvpCount WHERE id = :id")
    suspend fun updateRsvp(id: Long, isRsvpd: Boolean, newRsvpCount: Int)
}
