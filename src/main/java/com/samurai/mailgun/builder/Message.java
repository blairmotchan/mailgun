package com.samurai.mailgun.builder;

import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Message {

    public enum TestMode {
        YES
    }

    public enum Tracking {
        YES,
        NO
    }

    public enum TrackingOpens {
        YES,
        NO
    }

    public enum TrackingClicks {
        YES,
        NO,
        HTMLONLY
    }

    public enum Dkim {
        YES,
        NO
    }

    private List<String> toList = new ArrayList<>();
    private List<String> ccList = new ArrayList<>();
    private List<String> bccList = new ArrayList<>();
    private List<String> tagList = new ArrayList<>();
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> variables = new HashMap<>();

    private String from;
    private String subject;
    private String text;
    private String html;
    private String campaign;
    private TestMode testMode;
    private Dkim dkim;
    private Tracking tracking;
    private TrackingClicks trackingClicks;
    private TrackingOpens trackingOpens;
    private Date deliveryTime;


    public Message addTo(String... to) {
        addListValueInternal(to, toList);
        return this;
    }

    public Message addCc(String... cc) {
        addListValueInternal(cc, ccList);
        return this;
    }

    public Message addBcc(String... bcc) {
        addListValueInternal(bcc, bccList);
        return this;
    }

    public Message addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Message addVariable(String key, String value) {
        variables.put(key, value);
        return this;
    }

    public Message addTag(String... tag) {
        addListValueInternal(tag, tagList);
        return this;
    }

    private void addListValueInternal(String[] provideValues, List<String> internalList) {
        if (provideValues != null) {
            for (String email : provideValues) {
                if (StringUtils.isNotBlank(email) && !internalList.contains(email))
                    internalList.add(email);
            }
        }
    }

    public List<String> getToList() {
        return toList;
    }

    public List<String> getCcList() {
        return ccList;
    }

    public List<String> getBccList() {
        return bccList;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public String getFrom() {
        return from;
    }

    public Message setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Message setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getText() {
        return text;
    }

    public Message setText(String text) {
        this.text = text;
        return this;
    }

    public String getHtml() {
        return html;
    }

    public Message setHtml(String html) {
        this.html = html;
        return this;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public void setCcList(List<String> ccList) {
        this.ccList = ccList;
    }

    public void setBccList(List<String> bccList) {
        this.bccList = bccList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getCampaign() {
        return campaign;
    }

    public Message setCampaign(String campaign) {
        this.campaign = campaign;
        return this;
    }

    public TestMode getTestMode() {
        return testMode;
    }

    public Message setTestMode(TestMode testMode) {
        this.testMode = testMode;
        return this;
    }

    public Dkim getDkim() {
        return dkim;
    }

    public Message setDkim(Dkim dkim) {
        this.dkim = dkim;
        return this;
    }

    public Tracking getTracking() {
        return tracking;
    }

    public Message setTracking(Tracking tracking) {
        this.tracking = tracking;
        return this;
    }

    public TrackingClicks getTrackingClicks() {
        return trackingClicks;
    }

    public Message setTrackingClicks(TrackingClicks trackingClicks) {
        this.trackingClicks = trackingClicks;
        return this;
    }

    public TrackingOpens getTrackingOpens() {
        return trackingOpens;
    }

    public Message setTrackingOpens(TrackingOpens trackingOpens) {
        this.trackingOpens = trackingOpens;
        return this;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public Message setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
        return this;
    }
}
