/*
 * Copyright (C) 2017 Clivern. <https://clivern.com>
 */
package com.clivern.racter.receivers.webhook;

import com.clivern.racter.contract.templates.ReceiverTemplate;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Message Read Webhook
 */
public class MessageReadWebhook implements ReceiverTemplate {

    /*
    {
        "sender":{
            "id":"USER_ID"
        },
        "recipient":{
            "id":"PAGE_ID"
        },
        "timestamp":1458668856463,
        "read":{
            "watermark":1458668856253,
            "seq":38
        }
    }
    */

    protected String user_id;

    protected String page_id;

    protected Long watermark;

    protected Integer seq;

    protected Long timestamp;


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
     * Set Watermark
     *
     * @param watermark
     */
    public void setWatermark(Long watermark)
    {
        this.watermark = watermark;
    }

    /**
     * Set Seq
     *
     * @param seq
     */
    public void setSeq(Integer seq)
    {
        this.seq = seq;
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
     * Check if Watermark Exist
     *
     * @return Boolean
     */
    public Boolean hasWatermark()
    {
        return (this.watermark != null);
    }

    /**
     * Check if Seq Exist
     *
     * @return Boolean
     */
    public Boolean hasSeq()
    {
        return (this.seq != null);
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
     * Get Watermark
     *
     * @return Long
     */
    public Long getWatermark()
    {
        return this.watermark;
    }

    /**
     * Get Seq
     *
     * @return Integer
     */
    public Integer getSeq()
    {
        return this.seq;
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
}