package com.tir38.android.AdjustableSplitPane;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MyListFragment extends ListFragment {

    private List<Email> mEmails;
    private Callbacks mCallbacks;

    /**
     * public factory
     *
     * @return
     */
    public static Fragment newInstance() {
        Fragment fragment = new MyListFragment();
        return fragment;
    }

    /**
     * Hosting Activity(s) must implement Callbacks
     */
    public interface Callbacks {
        void onEmailSelected(int emailIndex);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mCallbacks.onEmailSelected(position);
    }

    /**
     * private inner class to extend ArrayAdapter
     */
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
