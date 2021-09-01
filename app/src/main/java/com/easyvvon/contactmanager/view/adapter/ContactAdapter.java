package com.easyvvon.contactmanager.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.easyvvon.contactmanager.R;
import com.easyvvon.contactmanager.model.entity.Contact;
import com.easyvvon.contactmanager.view.MainActivity;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contactList;
    private MainActivity mainActivity;

    public ContactAdapter(Context context, List<Contact> contacts, MainActivity mainActivity) {
        this.context = context;
        this.contactList = contacts;
        this.mainActivity = mainActivity;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public TextView tv_email;

        public ContactViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_email = view.findViewById(R.id.tv_email);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {

        final Contact contact = contactList.get(position);

        holder.tv_name.setText(contact.getName());
        holder.tv_email.setText(contact.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addAndEditContacts(true, contact, position);
            }
        });

    }

    @Override
    public int getItemCount() {

        return contactList.size();
    }

}