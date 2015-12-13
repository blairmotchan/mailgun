package com.samurai.mailgun.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samurai.mailgun.builder.Message;
import com.samurai.mailgun.data.MessageResponse;
import com.samurai.mailgun.exception.MailGunException;
import com.samurai.mailgun.service.MailgunService;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class MailgunServiceImpl implements MailgunService {

    public MailgunServiceImpl() {
    }

    public MailgunServiceImpl(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    private String apiKey;

    private String baseUrl;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

    @Override
    public MessageResponse sendEmail(Message message) {

        Form messageData = new Form();
        messageData.param("to", getJoinedString(message.getToList()));
        messageData.param("from", message.getFrom());

        if (message.getCcList().size() > 0) {
            messageData.param("cc", getJoinedString(message.getCcList()));
        }

        if (message.getBccList().size() > 0) {
            messageData.param("bcc", getJoinedString(message.getBccList()));
        }

        if (StringUtils.isNotBlank(message.getSubject()))
            messageData.param("subject", message.getSubject());

        if (StringUtils.isNotBlank(message.getText()))
            messageData.param("text", message.getText());

        if (StringUtils.isNotBlank(message.getHtml()))
            messageData.param("html", message.getHtml());

        if (message.getTestMode() != null)
            messageData.param("o:testmode", message.getTestMode().toString().toLowerCase());

        if (message.getDkim() != null)
            messageData.param("o:dkim", message.getDkim().toString().toLowerCase());

        if (message.getTracking() != null)
            messageData.param("o:tracking", message.getTracking().toString().toLowerCase());

        if (message.getTrackingClicks() != null)
            messageData.param("o:tracking-clicks", message.getTrackingClicks().toString().toLowerCase());

        if (message.getTrackingOpens() != null)
            messageData.param("o:tracking-opens", message.getTrackingOpens().toString().toLowerCase());

        for (Map.Entry<String, String> entry : message.getHeaders().entrySet()) {
            messageData.param("h:" + entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> entry : message.getVariables().entrySet()) {
            messageData.param("v:" + entry.getKey(), entry.getValue());
        }

        for (String tag : message.getTagList()) {
            messageData.param("o:tag", tag);
        }

        if (StringUtils.isNotBlank(message.getCampaign()))
            messageData.param("o:campaign", message.getCampaign());

        if (message.getDeliveryTime() != null)
            messageData.param("o:deliverytime", SDF.format(message.getDeliveryTime()));


        Response response = sendMessage(messageData);
        responseHandler(response);

        MessageResponse mailGunResponse;

        try {
            HashMap<String, Object> result = new ObjectMapper().readValue(response.readEntity(String.class), HashMap.class);
            mailGunResponse = new MessageResponse((String) result.get("id"), (String) result.get("message"));
        } catch (IOException e) {
            throw new MailGunException("Error while resolving mailgun response");
        }

        return mailGunResponse;
    }

    private Response sendMessage(Form messageData) {
        ClientConfig clientConfigMail = new ClientConfig();
        Client clientMail = ClientBuilder.newClient(clientConfigMail);
        clientMail.register(HttpAuthenticationFeature.basic("api", apiKey));
        WebTarget targetMail = clientMail.target(baseUrl + "/messages");

        return targetMail.request().post(Entity.entity(messageData, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    }

    //https://documentation.mailgun.com/api-intro.html#errors
    private void responseHandler(Response response) {
        int code = response.getStatus();
        if (code != 200) {
            String message;
            switch (code) {
                case 400:
                    message = "400 Bad Request - Required parameter likely missing";
                    break;
                case 401:
                    message = "401 Unauthorized - No Valid API key provided";
                    break;
                case 402:
                    message = "402 Request Failed - Parameters were valid but request failed";
                    break;
                case 404:
                    message = "404 Not Found - The requested item doesn't exist";
                    break;
                case 500:
                case 502:
                case 503:
                case 504:
                    message = code + " Server Error - something is wrong on Mailgun’s end";
                    break;
                default:
                    message = code + " Unknown Error";
                    break;
            }
            throw new MailGunException(message);
        }
    }

    private String getJoinedString(List<String> list) {
        StringJoiner joiner = new StringJoiner(", ");

        for (String item : list) {
            joiner.add(item);
        }

        return joiner.toString();
    }

    private void setBooleanValue(Form form, String key, boolean value) {
        form.param(key, value ? "yes" : "no");
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
