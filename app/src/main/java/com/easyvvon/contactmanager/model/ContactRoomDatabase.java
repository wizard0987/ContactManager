package com.easyvvon.contactmanager.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.easyvvon.contactmanager.model.entity.Contact;

// 이 프로젝트의 다른 부분에서 ContactsRoomDatabase 인스턴스를 만들고
// 이 getContactDAO() 메서드를 호출하여 ContactDAO에서 선언한 모든 메서드를 호출하여 Room DB와 통신할 수 있다.
@Database(entities = {Contact.class}, version = 1)
public abstract class ContactRoomDatabase extends RoomDatabase {

    public abstract ContactDAO getContactDAO();
}
