package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.models.CommunityEventEntity
import com.example.data.models.ForumPostEntity
import com.example.data.models.ListingEntity
import com.example.data.models.MaintenanceTicketEntity
import com.example.data.models.MessageEntity
import com.example.data.models.MoveOutAlertEntity
import com.example.data.models.PaymentRecordEntity

@Database(
    entities = [
        ListingEntity::class,
        MessageEntity::class,
        MoveOutAlertEntity::class,
        MaintenanceTicketEntity::class,
        PaymentRecordEntity::class,
        ForumPostEntity::class,
        CommunityEventEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()  {
    abstract fun listingDao(): ListingDao
    abstract fun messageDao(): MessageDao
    abstract fun moveOutAlertDao(): MoveOutAlertDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun paymentDao(): PaymentDao
    abstract fun forumDao(): ForumDao

    companion object  {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase  {
            return INSTANCE ?: synchronized(this)  {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "urban_room_db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
