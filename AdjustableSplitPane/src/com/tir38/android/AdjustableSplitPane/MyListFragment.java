package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MyListFragment extends ListFragment {

    private List<Email> mEmails;
    private Callbacks mCallbacks;



    public static Fragment newInstance() {
        Log.d("MyListFragment", "inside newInstance");
        Fragment fragment = new MyListFragment();
        return fragment;
    }

    /**
     * Hosting Activity(s) must implement Callbacks
     */
    public interface Callbacks {
        void onEmailSelected(Email email);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyListFragment", "inside onCreate");

        mEmails = DataStore.get().getEmails();

        // create array adapter
        ArrayAdapter<Email> adapter = new EmailAdapter(mEmails);

        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Email email = (Email) getListAdapter().getItem(position);
        mCallbacks.onEmailSelected(email);
    }

    // private inner class to extend ArrayAdapter
    private class EmailAdapter extends ArrayAdapter<Email> {

        public EmailAdapter(List<Email> emails) {
            super(getActivity(), 0, emails);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_email, null);
            }

            Email email = getItem(position);

            // set layout widgets from email values
            TextView fromTextView = (TextView) convertView.findViewById(R.id.list_item_email_from_text_view);
            fromTextView.setText(email.getFrom());

            TextView toTextView = (TextView) convertView.findViewById(R.id.list_item_email_to_text_view);
            toTextView.setText(email.getTo());

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_email_title_text_view);
            titleTextView.setText(email.getTitle());

            return convertView;
        }
    }
}
