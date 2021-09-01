package com.easyvvon.contactmanager.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.easyvvon.contactmanager.model.entity.Contact;
import com.easyvvon.contactmanager.view.MainActivity;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public ContactAdapter(Context context, List<Contact> contacts, MainActivity mainActivity) {
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public ContactViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}