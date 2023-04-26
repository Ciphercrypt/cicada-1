package org.aissms.cicada.stego;

class Call {
    // email1 is the caller
    String email1;
    String email2;
    boolean callStarted;
    boolean store;
    String imageLinkFromEmail1;
    String imageLinkFromEmail2;
    long ringEndTime;

    public Call(String user1, String user2, long ringEndTime) {
        this.email1 = user1;
        this.email2 = user2;
        this.ringEndTime = ringEndTime;
        this.store = true;
    }

    public String getEmail1() {
        return email1;
    }

    public String getEmail2() {
        return email2;
    }

    public boolean isCallStarted() {
        return callStarted;
    }

    public void setCallStarted(boolean callStarted) {
        this.callStarted = callStarted;
    }
    public void stopStoring() {
        store = false;
    }
    public boolean canStore() {
        return store;
    }
    public long getRingEndTime() {
        return ringEndTime;
    }
    public boolean ringExpired() {
        return System.currentTimeMillis() > ringEndTime;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public String getImageLinkFromEmail1() {
        return imageLinkFromEmail1;
    }

    public void setImageLinkFromEmail1(String imageLinkFromEmail1) {
        this.imageLinkFromEmail1 = imageLinkFromEmail1;
    }

    public String getImageLinkFromEmail2() {
        return imageLinkFromEmail2;
    }

    public void setImageLinkFromEmail2(String imageLinkFromEmail2) {
        this.imageLinkFromEmail2 = imageLinkFromEmail2;
    }

    public void setRingEndTime(long ringEndTime) {
        this.ringEndTime = ringEndTime;
    }

    public void setImageLink(String name, String url) {
        if(name.equals(email1)) {
            setImageLinkFromEmail1(url);
        } else if(name.equals(email2)) {
            setImageLinkFromEmail2(url);
        }
    }

    public boolean alreadyUploaded(String name) {
        if(email1.equals(name)) {
            return imageLinkFromEmail1 != null;
        } else if(email2.equals(name)) {
            return imageLinkFromEmail2 != null;
        }
        return true;
    }

    public boolean imagesReady() {
        return imageLinkFromEmail1 != null && imageLinkFromEmail2 != null;
    }
}