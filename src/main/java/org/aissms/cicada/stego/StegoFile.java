package org.aissms.cicada.stego;

public class StegoFile {
    String messagetype = "STEGO_IMAGE";
    String imageLink;
    public StegoFile(String imageLink) {
        this.imageLink = imageLink;
    }
    public String getMessagetype() {
        return messagetype;
    }
    public String getImageLink() {
        return imageLink;
    }
}
