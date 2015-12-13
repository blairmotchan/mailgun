package com.samurai.mailgun.exception;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AuthenticationExceptionTest {

    @Test
    public void testConstructor() {
        MailGunException iee = new MailGunException("Invalid API Key/mailgun combination");
        assertThat(iee.getMessage(), is("Invalid API Key/mailgun combination"));
    }
}
