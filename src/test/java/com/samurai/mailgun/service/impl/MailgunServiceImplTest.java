package com.samurai.mailgun.service.impl;

import com.samurai.mailgun.builder.Message;
import com.samurai.mailgun.data.MessageResponse;
import com.samurai.mailgun.exception.MailGunException;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.powermock.api.easymock.PowerMock.expectPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MailgunServiceImpl.class)
public class MailgunServiceImplTest {

    private MailgunServiceImpl mailGunService;

    @Before
    public void setup() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        mailGunService = PowerMock.createPartialMock(MailgunServiceImpl.class, "sendMessage", Form.class);
    }

    @Test
    public void testSendMinimalEmail() throws Exception {
        Message message = createMessage(false);
        Response response = mock(Response.class);

        Capture<Form> formCapture = EasyMock.newCapture();

        expectPrivate(mailGunService, "sendMessage", capture(formCapture)).andReturn(response);
        expect(response.getStatus()).andReturn(200);
        expect(response.readEntity(String.class)).andReturn("{\"id\": \"<somecrazylongidstring>\", \"message\":\"success\"}");

        PowerMock.replay(mailGunService);
        EasyMock.replay(response);
        MessageResponse messageResponse = mailGunService.sendEmail(message);
        PowerMock.verify(mailGunService);
        EasyMock.verify(response);

        assertThat(messageResponse.getId(), is("<somecrazylongidstring>"));
        assertThat(messageResponse.getMessage(), is("success"));
        validateFormCapture(formCapture, false);
    }

    @Test
    public void testSendFullEmail() throws Exception {
        Message message = createMessage(true);
        Response response = mock(Response.class);

        Capture<Form> formCapture = EasyMock.newCapture();

        expectPrivate(mailGunService, "sendMessage", capture(formCapture)).andReturn(response);
        expect(response.getStatus()).andReturn(200);
        expect(response.readEntity(String.class)).andReturn("{\"id\": \"<somecrazylongidstring>\", \"message\":\"success\"}");

        PowerMock.replay(mailGunService);
        EasyMock.replay(response);
        MessageResponse messageResponse = mailGunService.sendEmail(message);
        PowerMock.verify(mailGunService);
        EasyMock.verify(response);

        assertThat(messageResponse.getId(), is("<somecrazylongidstring>"));
        assertThat(messageResponse.getMessage(), is("success"));
        validateFormCapture(formCapture, true);
    }

    private Message createMessage(boolean allOptions) {
        Message message = new Message()
                .addTo("to@email.com")
                .setFrom("from@email.com");

        if (allOptions) {

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"));
            cal.set(2000, 1, 1, 1, 1, 1);
            return message
                    .addCc("cc@email.com")
                    .addBcc("bcc@email.com")
                    .setSubject("subject")
                    .setText("text")
                    .setHtml("html")
                    .setTestMode(Message.TestMode.YES)
                    .setDkim(Message.Dkim.YES)
                    .setTracking(Message.Tracking.YES)
                    .setTrackingClicks(Message.TrackingClicks.HTMLONLY)
                    .setTrackingOpens(Message.TrackingOpens.NO)
                    .addHeader("header", "value")
                    .addTag("tag1")
                    .setCampaign("campaign")
                    .setDeliveryTime(ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("America/Chicago")))
                    .addVariable("variable", "value");
        } else {
            return message;
        }
    }

    private void validateFormCapture(Capture<Form> formCapture, boolean fullMessage) {
        Map formMap = formCapture.getValue().asMap();

        assertThat(((LinkedList<String>) formMap.get("to")).get(0), is("to@email.com"));
        assertThat(((LinkedList<String>) formMap.get("from")).get(0), is("from@email.com"));

        if (fullMessage) {
            assertThat(((LinkedList<String>) formMap.get("cc")).get(0), is("cc@email.com"));
            assertThat(((LinkedList<String>) formMap.get("bcc")).get(0), is("bcc@email.com"));
            assertThat(((LinkedList<String>) formMap.get("subject")).get(0), is("subject"));
            assertThat(((LinkedList<String>) formMap.get("text")).get(0), is("text"));
            assertThat(((LinkedList<String>) formMap.get("html")).get(0), is("html"));
            assertThat(((LinkedList<String>) formMap.get("o:testmode")).get(0), is("yes"));
            assertThat(((LinkedList<String>) formMap.get("o:dkim")).get(0), is("yes"));
            assertThat(((LinkedList<String>) formMap.get("o:tracking")).get(0), is("yes"));
            assertThat(((LinkedList<String>) formMap.get("o:tracking-clicks")).get(0), is("htmlonly"));
            assertThat(((LinkedList<String>) formMap.get("o:tracking-opens")).get(0), is("no"));
            assertThat(((LinkedList<String>) formMap.get("h:header")).get(0), is("value"));
            assertThat(((LinkedList<String>) formMap.get("v:variable")).get(0), is("value"));
            assertThat(((LinkedList<String>) formMap.get("o:tag")).get(0), is("tag1"));
            assertThat(((LinkedList<String>) formMap.get("o:campaign")).get(0), is("campaign"));
            assertThat(((LinkedList<String>) formMap.get("o:tag")).get(0), is("tag1"));
            assertThat(((LinkedList<String>) formMap.get("o:deliverytime")).get(0), is("Thu, 01 Jan 2015 00:00:00 CST"));
        } else {
            assertThat(formMap.size(), is(2));
        }
    }

    public void testException(int status, String error) throws Exception {
        Message message = new Message();
        Response response = mock(Response.class);
        String errorMessage = null;

        expectPrivate(mailGunService, "sendMessage", isA(Form.class)).andReturn(response);
        expect(response.getStatus()).andReturn(status);

        try {
            PowerMock.replay(mailGunService);
            EasyMock.replay(response);
            mailGunService.sendEmail(message);
        } catch (MailGunException me) {
            errorMessage = me.getMessage();
        }

        PowerMock.verify(mailGunService);
        EasyMock.verify(response);
        assertThat(errorMessage, is(error));
    }

    @Test
    public void testBadRequest() throws Exception {
        testException(400, "400 Bad Request - Required parameter likely missing");
    }

    @Test
    public void testUnauthorized() throws Exception {
        testException(401, "401 Unauthorized - No Valid API key provided");
    }

    @Test
    public void testRequestFailed() throws Exception {
        testException(402, "402 Request Failed - Parameters were valid but request failed");
    }

    @Test
    public void testNotFound() throws Exception {
        testException(404, "404 Not Found - The requested item doesn't exist");
    }

    @Test
    public void testMailgunException500() throws Exception {
        testException(500, "500 Server Error - something is wrong on Mailgun’s end");
    }

    @Test
    public void testMailgunException502() throws Exception {
        testException(502, "502 Server Error - something is wrong on Mailgun’s end");
    }

    @Test
    public void testMailgunException503() throws Exception {
        testException(503, "503 Server Error - something is wrong on Mailgun’s end");
    }

    @Test
    public void testMailgunException504() throws Exception {
        testException(504, "504 Server Error - something is wrong on Mailgun’s end");
    }

    @Test
    public void unknownException() throws Exception {
        testException(600, "600 Unknown Error");
    }
}
