package com.easyvvon.contactmanager.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easyvvon.contactmanager.R;
import com.easyvvon.contactmanager.model.entity.Contact;
import com.easyvvon.contactmanager.view.adapter.ContactAdapter;
import com.easyvvon.contactmanager.viewmodel.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private ContactAdapter ContactAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ContactViewModel contactViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacts Manager");

        // ViewModel 생성
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        recyclerView = findViewById(R.id.recycler_view_contacts);

        // 위 영화 정보들을 ContactAdapter에 전달
        ContactAdapter = new ContactAdapter(this, contactList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ContactAdapter);

        // ViewModel의 getContext() 메서드는 연락처 리스트를 실시간 데이터(LiveData)로 제공한다.
        contactViewModel.getContactList().observe(this, new Observer<List<Contact>>() {
            public void onChanged(List<Contact> contacts) {
                contactList.clear();
                contactList.addAll(contacts);
                ContactAdapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContacts(false, null, -1);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mode1: return true;
            case R.id.mode2: return true;
        }

        return false;
    }

    public void addAndEditContacts(final boolean isUpdate, final Contact contact, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_contact, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView contactTitle        = view.findViewById(R.id.tv_contactTitle);
        final EditText newContact    = view.findViewById(R.id.ev_name);
        final EditText contactEmail  = view.findViewById(R.id.ev_email);

        contactTitle.setText(!isUpdate ? "Add New Contact" : "Edit Contact");

        if (isUpdate && (contact != null)) {
            newContact.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton( // '수정/확인' 역할 버튼 설정
                        isUpdate ? "Update" : "Save",
                        (dialogBox, id) -> {}
                )
                .setNegativeButton( // '삭제/취소' 역할 버튼 설정
                        isUpdate ? "Delete" : "Cancel",
                        (dialogBox, id) -> {
                            if (isUpdate) {
                                deleteContact(contact, position);
                            } else {
                                dialogBox.cancel();
                            }
                        }
                );


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 연락처 이름 작성란에 문자열이 없는 경우,
                if (TextUtils.isEmpty(newContact.getText().toString())) {
                    Toast.makeText(
                            MainActivity.this,
                            "Enter contact name!",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                } else {
                    // 연락처 이름 작성란에 문자열이 존재하는 경우 alertDialog 내리기(dismiss)
                    alertDialog.dismiss();
                }

                if (isUpdate && (contact != null)) {
                    updateContact(newContact.getText().toString(), contactEmail.getText().toString(), position);
                } else {
                    insertContact(newContact.getText().toString(), contactEmail.getText().toString());
                }
            }
        });
    }

    private void deleteContact(final Contact contact, int position) {
        contactViewModel.deleteContact(contact);
    }

    private void updateContact(String name, String email, int position) {

        final Contact contact = contactList.get(position);

        contact.setName(name);
        contact.setEmail(email);

        contactViewModel.updateContact(contact);

    }

    private void insertContact(final String name, final String email) {
        contactViewModel.insertContact(name, email);
    }

    protected void onDestroy() {
        super.onDestroy();
        contactViewModel.clear();
    }
}