package com.easyvvon.contactmanager.model;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.easyvvon.contactmanager.model.entity.Contact;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContactRepository {

    private Application application;
    private ContactRoomDatabase contactRoomDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Contact>> contactListLiveData = new MutableLiveData<>();
    private Long insertedRowId;

    public ContactRepository(Application application) {
        this.application = application;
        contactRoomDatabase = Room.databaseBuilder(
                                      application.getApplicationContext(),
                                      ContactRoomDatabase.class,
                                      "ContactDB"
                              ).build();

        compositeDisposable.add(
                contactRoomDatabase.getContactDAO().getContacts()
                        .subscribeOn(Schedulers.computation()) // io 대신 computation을 사용한 이유는, Schedulers.io 대신 '이벤트 루프'와 'Callback 처리'와 같은 계산 작업에 사용되기 때문이다.
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                contacts -> contactListLiveData.postValue(contacts),
                                throwable -> {}
                        )
        );
    }

    public MutableLiveData<List<Contact>> getContactListLiveData() {
        return contactListLiveData;
    }

    public void insertContact(final String name, final String email) {

        compositeDisposable.add(
                Completable.fromAction(() -> {
                    insertedRowId = contactRoomDatabase.getContactDAO().addContact(new Contact(0, name, email));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    public void onComplete() {
                        Toast.makeText(
                                application.getApplicationContext(),
                                "Contact added successful : " + insertedRowId,
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(
                                application.getApplicationContext(),
                                "Contact added failed, Error occurred",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
        );
    }

    public void updateContact(final Contact contact) {

        compositeDisposable.add(
                Completable.fromAction(() -> {
                    contactRoomDatabase.getContactDAO().updateContact(contact);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    public void onComplete() {
                        Toast.makeText(
                                application.getApplicationContext(),
                                "Contact updated successful",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(
                                application.getApplicationContext(),
                                "Contact updated failed, Error occurred",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
        );
    }

    public void deleteContact(final Contact contact) {

        compositeDisposable.add(
                Completable.fromAction(() -> {
                    contactRoomDatabase.getContactDAO().deleteContact(contact);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    public void onComplete() {
                        Toast.makeText(
                                application.getApplicationContext(),
                                "Contact deleted successful",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(
                                application.getApplicationContext(),
                                "Contact deleted failed, Error occurred",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
        );

    }

    public void clear() {
        compositeDisposable.clear();
    }

}
