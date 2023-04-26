package org.aissms.cicada.stego;

import org.aissms.cicada.mongo.FileController;

public class StegoFile {
    String messagetype = "STEGO_IMAGE";
    String imageLink;
    public StegoFile(String imageLink) {
        this.imageLink = FileController.REQUEST_PATH + imageLink;
    }
    public String getMessagetype() {
        return messagetype;
    }
    public String getImageLink() {
        return imageLink;
    }
}
