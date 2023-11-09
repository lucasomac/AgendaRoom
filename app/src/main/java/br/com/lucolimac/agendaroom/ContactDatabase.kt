package br.com.lucolimac.agendaroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.lucolimac.agendaroom.data.Contact
import br.com.lucolimac.agendaroom.data.ContactDAO

@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDAO(): ContactDAO

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null
        fun getDatabase(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, ContactDatabase::class.java, "agendaroom.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}