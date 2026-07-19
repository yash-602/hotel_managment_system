package com.hotel.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 2L;

    private int customerId;
    private String name;
    private String contact;
    private int allocatedRoomNumber;
    private boolean loyaltyMember;

    public Customer(int customerId, String name, String contact,
                    boolean loyaltyMember) {
        this.customerId          = customerId;
        this.name                = name;
        this.contact             = contact;
        this.allocatedRoomNumber = -1;
        this.loyaltyMember       = loyaltyMember;
    }

    public int    getCustomerId()           { return customerId; }
    public String getName()                 { return name; }
    public String getContact()              { return contact; }
    public int    getAllocatedRoomNumber()   { return allocatedRoomNumber; }
    public boolean isLoyaltyMember()        { return loyaltyMember; }
    public void setAllocatedRoomNumber(int r){ this.allocatedRoomNumber = r; }
    public void setLoyaltyMember(boolean l) { this.loyaltyMember = l; }
}