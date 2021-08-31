package com.easyvvon.contactmanager.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.easyvvon.contactmanager.model.entity.Contact;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ContactDAO {

    @Insert
    public Long addContact(Contact contact);

    @Update
    public void updateContact(Contact contact);

    @Delete
    public void deleteContact(Contact contact);

    @Query("select * from contact")
    public Flowable<List<Contact>> getContacts();

    @Query("select * from contact where contact_id == :contact_id")
    public Contact getContacts(Long contact_id);
}