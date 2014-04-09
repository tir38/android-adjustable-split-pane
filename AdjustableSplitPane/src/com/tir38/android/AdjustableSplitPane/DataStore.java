package com.tir38.android.AdjustableSplitPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Store for emails
 */
public class DataStore {

    private static DataStore sDataStore;
    private List<Email> mEmails;

    public static DataStore get() {
        if (sDataStore == null) {
            sDataStore = new DataStore();
        }
        return sDataStore;
    }

    public DataStore() {
        mEmails = new ArrayList<Email>();
        buildEmailList(20);
    }

    public void buildEmailList(int numberOfEmails) {
        for (int i = 0; i < numberOfEmails; i++) {
            mEmails.add(new Email());
        }
    }

    public List<Email> getEmails() {
        return mEmails;
    }

    public void setEmails(List<Email> emails) {
        mEmails = emails;
    }
}
