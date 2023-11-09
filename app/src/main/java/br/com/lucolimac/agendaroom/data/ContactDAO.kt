package br.com.lucolimac.agendaroom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDAO {
    @Insert
    fun createContact(contact: Contact)

    @Query("SELECT * FROM contact ORDER BY name")
    suspend fun getContacts(): List<Contact>

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)
}