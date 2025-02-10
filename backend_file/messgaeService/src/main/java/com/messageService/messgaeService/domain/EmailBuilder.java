package com.messageService.messgaeService.domain;

import java.io.StringWriter;
import java.util.Properties;


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class EmailBuilder {

    private String subject;

    private String mailTo;

    private String[] mailCC;

    private String mailFrom;

    private String template;

    VelocityContext velocityContext;

    VelocityEngine velocityEngine;

    public EmailBuilder() {
        this.mailTo = "";
        this.mailCC = null;
        this.mailFrom = "";
        this.subject = "";
        this.template = "";
        this.velocityContext = new VelocityContext();

        // Initialize Velocity Engine
        Properties properties = new Properties();
        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        properties.setProperty("resource.loader", "file, class, jar");
        properties.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        this.velocityEngine = new VelocityEngine(properties);

    }

    public EmailBuilder subject(String subject) {

        this.subject = subject;
        return this;
    }

    public EmailBuilder to(String to) {

        this.mailTo = to;
        return this;
    }

    public EmailBuilder cc(String[] cc) {

        this.mailCC = cc;
        return this;
    }

    public EmailBuilder from(String from) {
        this.mailFrom = from;
        return this;
    }

    public EmailBuilder template(String template) {
        this.template = template;
        return this;
    }

    public EmailBuilder addContext(String key, String value) {
        velocityContext.put(key, value);
        return this;
    }

    public EmailBuilder addContext(String key, Object value) {
        velocityContext.put(key, value);
        return this;
    }

    public EmailMessage createMail() throws IllegalArgumentException {
        // Select Template
        Template templateEngine = velocityEngine.getTemplate("templates/" + this.template);

        // Apply template
        StringWriter stringWriter = new StringWriter();
        templateEngine.merge(this.velocityContext, stringWriter);

        // Check state of the mails.
        if (this.mailTo.isEmpty() || this.mailFrom.isEmpty()) {
            throw new IllegalArgumentException("Missing mail headers");
        }

        // Build mail object
        EmailMessage result = new EmailMessage();
        result.setTo(this.mailTo);
        result.setMailContent(stringWriter.toString());
        result.setSubject(this.subject);

        return result;
    }
}