/*
 * Copyright (C) 2017 Clivern. <https://clivern.com>
 */
package com.clivern.racter.receivers.webhook;

import com.clivern.racter.contract.templates.ReceiverTemplate;
import java.util.HashMap;
import java.util.Map;
import java.lang.*;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Message Webhook Class
 */
public class MessageReceivedWebhook implements ReceiverTemplate {


    protected String user_id;

    protected String page_id;

    protected Long timestamp;

    protected String message_id;

    protected String message_text;

    protected String quick_reply_payload;

    protected Map<String, String> attachments = new HashMap<String, String>();


    /**
     * Set User ID
     *
     * @param user_id
     */
    public void setUserId(String user_id)
    {
        this.user_id = user_id;
    }

    /**
     * Set Page ID or Receiver ID
     *
     * @param page_id
     */
    public void setPageId(String page_id)
    {
        this.page_id = page_id;
    }

    /**
     * Set Timestamp
     *
     * @param timestamp
     */
    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * Set Message ID
     *
     * @param message_id
     */
    public void setMessageId(String message_id)
    {
        this.message_id = message_id;
    }

    /**
     * Set Message Text
     *
     * @param message_text
     */
    public void setMessageText(String message_text)
    {
        this.message_text = message_text;
    }

    /**
     * Set Quick Reply Payload
     *
     * @param quick_reply_payload
     */
    public void setQuickReplyPayload(String quick_reply_payload)
    {
        this.quick_reply_payload = quick_reply_payload;
    }

    /**
     * Set Attachment
     *
     * @param type
     * @param payload
     */
    public void setAttachment(String type, String payload)
    {
        attachments.put(type, payload);
    }

    /**
     * Set Attachment
     *
     * @param type
     * @param loc_lat
     * @param loc_long
     */
    public void setAttachment(String type, Long loc_lat, Long loc_long)
    {
        attachments.put(type, Long.toString(loc_lat) + "," + Long.toString(loc_long));
    }

    /**
     * Check if User ID Exist
     *
     * @return Boolean
     */
    public Boolean hasUserId()
    {
        return (this.user_id != null);
    }

    /**
     * Check if Page ID Exist
     *
     * @return Boolean
     */
    public Boolean hasPageId()
    {
        return (this.page_id != null);
    }

    /**
     * Check if Timestamp Exist
     *
     * @return Boolean
     */
    public Boolean hasTimestamp()
    {
        return (this.timestamp != null);
    }

    /**
     * Check if Message ID Exist
     *
     * @return Boolean
     */
    public Boolean hasMessageId()
    {
        return (this.message_id != null);
    }

    /**
     * Check if Message Text Exist
     *
     * @return Boolean
     */
    public Boolean hasMessageText()
    {
        return (this.message_text != null);
    }

    /**
     * Check if Quick Reply Payload Exist
     *
     * @return Boolean
     */
    public Boolean hasQuickReplyPayload()
    {
        return (this.quick_reply_payload != null);
    }

    /**
     * Check if Attachment Exist
     *
     * @return Boolean
     */
    public Boolean hasAttachment()
    {
        return !this.attachments.isEmpty();
    }

    /**
     * Get User ID
     *
     * @return String
     */
    public String getUserId()
    {
        return this.user_id;
    }

    /**
     * Get Page ID
     *
     * @return String
     */
    public String getPageId()
    {
        return this.page_id;
    }

    /**
     * Get Timestamp
     *
     * @return Long
     */
    public Long getTimestamp()
    {
        return this.timestamp;
    }

    /**
     * Get Message ID
     *
     * @return String
     */
    public String getMessageId()
    {
        return this.message_id;
    }

    /**
     * Get Message Text
     *
     * @return String
     */
    public String getMessageText()
    {
        return this.message_text;
    }

    /**
     * Get Quick Reply Payload
     *
     * @return String
     */
    public String getQuickReplyPayload()
    {
        return this.quick_reply_payload;
    }

    /**
     * Get Attachment
     *
     * @return Map<String, String>
     */
    public Map<String, String> getAttachment()
    {
        return this.attachments;
    }
}