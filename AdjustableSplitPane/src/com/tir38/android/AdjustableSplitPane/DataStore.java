package com.tir38.android.AdjustableSplitPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Store for emails
 */
public class DataStore {

    private static DataStore sDataStore;
    private List<Email> mEmails;

    private String[] mDummyEmailAddresses = {"Silly@mail.com",
    "Voracious@mail.com",
    "Angry@mail.com",
    "Weary@mail.com",
    "Abounding@mail.com",
    "Needless@mail.com",
    "Few@mail.com",
    "Psychedelic@mail.com",
    "Bewildered@mail.com",
    "Flat@mail.com",
    "Squalid@mail.com",
    "Truculent@mail.com",
    "Economic@mail.com",
    "Unwritten@mail.com",
    "Noiseless@mail.com",
    "Male@mail.com",
    "Impolite@mail.com",
    "Nippy@mail.com",
    "Arrogant@mail.com",
    "Staking@mail.com",
    "Towering@mail.com",
    "Alert@mail.com"};


    public static DataStore get() {
        if (sDataStore == null) {
            sDataStore = new DataStore();
        }
        return sDataStore;
    }

    public DataStore() {
        mEmails = new ArrayList<Email>();
        buildEmailList(50);
    }

    public void buildEmailList(int numberOfEmails) {
        for (int i = 0; i < numberOfEmails; i++) {

            // get random to and from email addresses
            String to = mDummyEmailAddresses[(i+3) % mDummyEmailAddresses.length];
            String from = mDummyEmailAddresses[(i*i) % mDummyEmailAddresses.length];
            String title = "Loren Ipsum Email";

            mEmails.add(new Email(from, to, title));
        }
    }

    public List<Email> getEmails() {
        return mEmails;
    }

    public void setEmails(List<Email> emails) {
        mEmails = emails;
    }
}
