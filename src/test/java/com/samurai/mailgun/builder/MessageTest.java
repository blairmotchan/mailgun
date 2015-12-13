package com.samurai.mailgun.builder;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MessageTest {

    @Test
    public void testAddTo() {
        Message message = new Message();
        message.addTo("email1@email.com", "email2@email.com");

        assertThat(message.getToList().get(0), is("email1@email.com"));
        assertThat(message.getToList().get(1), is("email2@email.com"));
    }

    @Test
    public void testAddCc() {
        Message message = new Message();
        message.addCc("email1@email.com", "email2@email.com");

        assertThat(message.getCcList().get(0), is("email1@email.com"));
        assertThat(message.getCcList().get(1), is("email2@email.com"));
    }

    @Test
    public void testAddBcc() {
        Message message = new Message();
        message.addBcc("email1@email.com", "email2@email.com");

        assertThat(message.getBccList().get(0), is("email1@email.com"));
        assertThat(message.getBccList().get(1), is("email2@email.com"));
    }

    @Test
    public void testAddHeader() {
        Message message = new Message();
        message.addHeader("key", "value");

        assertThat(message.getHeaders().get("key"), is("value"));
    }

    @Test
    public void testAddVariable() {
        Message message = new Message();
        message.addVariable("key", "value");

        assertThat(message.getVariables().get("key"), is("value"));
    }

    @Test
    public void testAddTag() {
        Message message = new Message();
        message.addTag("tag1", "tag2");

        assertThat(message.getTagList().get(0), is("tag1"));
        assertThat(message.getTagList().get(1), is("tag2"));
    }
}
