package org.aissms.cicada.messaging;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class MyMessageDto {
    String messagetype = "MESSAGE";
    private int blockIndex;
    private int messageIndex;
    private String author;
    private String content;
    @JsonInclude(value = Include.NON_NULL)
    private String imageLink;
    private Date date;
    

    public int getBlockIndex() {
        return blockIndex;
    }
    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }
    public int getMessageIndex() {
        return messageIndex;
    }
    public void setMessageIndex(int messageIndex) {
        this.messageIndex = messageIndex;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getMessagetype() {
        return messagetype;
    }
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }
    public String getImageLink() {
        return imageLink;
    }
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
