package com.easyvvon.contactmanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.easyvvon.contactmanager.model.ContactRepository;

public class ContactViewModel extends AndroidViewModel {

    private ContactRepository contactRepository;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        this.contactRepository = new ContactRepository(application);
    }
}
