package com.tir38.android.AdjustableSplitPane;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyDetailFragment extends Fragment {

    private static final String ARG_EMAIL = "MyDetailFragment.ARG_EMAIL";
    private Email mEmail;

    /**
     * public factory
     * @param email
     * @return
     */
    public static MyDetailFragment newInstance(Email email) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EMAIL, email);

        MyDetailFragment fragment = new MyDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pull email from args
        mEmail = (Email) getArguments().getSerializable(ARG_EMAIL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mydetail, null);

        if (mEmail == null) {
            Log.e("MyDetailFragment", "no email to display");
            return null;
        }

        TextView fromTestView = (TextView) view.findViewById(R.id.fragment_mydetail_from_text_view);
        fromTestView.setText(mEmail.getFrom());

        TextView toTextView = (TextView) view.findViewById(R.id.fragment_mydetail_to_text_view);
        toTextView.setText(mEmail.getTo());

        TextView titleTextView = (TextView) view.findViewById(R.id.fragment_mydetail_title_text_view);
        titleTextView.setText(mEmail.getTitle());

        TextView bodyTextView = (TextView) view.findViewById(R.id.fragment_mydetail_body_text_view);
        bodyTextView.setText(mEmail.getBody());

        return view;
    }
}