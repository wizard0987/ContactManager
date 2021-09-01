package com.easyvvon.contactmanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.easyvvon.contactmanager.model.ContactRepository;
import com.easyvvon.contactmanager.model.entity.Contact;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContactRepository contactRepository;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        this.contactRepository = new ContactRepository(application);
    }

    public LiveData<List<Contact>> getContactList() {
        return contactRepository.getContactListLiveData();
    }

    public void insertContact(String name, String email) {
        contactRepository.insertContact(name, email);
    }

    public void updateContact(Contact contact) {
        contactRepository.updateContact(contact);
    }

    public void deleteContact(Contact contact) {
        contactRepository.deleteContact(contact);
    }

    public void clear() {
        contactRepository.clear();
    }
}
