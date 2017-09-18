package com.example.s522050.challenge.model;

public class DataItem  {

    private String mCustomerEmail;
    private String mTotalSpent;
    private int mQuantity;


    public DataItem(String email, String totalSpent, int quantity) {

        mCustomerEmail = email;

        mTotalSpent = totalSpent;
        mQuantity = quantity;
    }

    public void setCustomerEmail(String customerEmail) {
        mCustomerEmail = customerEmail;
    }

    public String getCustomerEmail() {

        return mCustomerEmail;
    }

    public void setTotalSpent(String totalSpent) {
        mTotalSpent = totalSpent;
    }


    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }


    public String getTotalSpent() {
        return mTotalSpent;
    }


    public int getQuantity() {
        return mQuantity;
    }
}