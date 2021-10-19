package com.swufestu.mycount;

public class Item {
    private String cname;
    private String cval;

    public Item(String cname, String cval) {
        this.cname = cname;
        this.cval = cval;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCval() {
        return cval;
    }

    public void setCval(String cval) {
        this.cval = cval;
    }
}
