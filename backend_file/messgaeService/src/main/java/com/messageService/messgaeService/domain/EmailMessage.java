package com.messageService.messgaeService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class EmailMessage {

    private String to;
    private String subject;
    private String message;
    private String mailContent;
    private String contentType;

    public EmailMessage(){
        this.contentType = "text/html";
    }

    @Override
    public String toString() {
        return "EmailMessage{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", mailContent='" + mailContent + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
