/**************************************************************************
 *  Copyright (C) 2010 Atlas of Living Australia
 *  All Rights Reserved.
 *
 *  The contents of this file are subject to the Mozilla Public
 *  License Version 1.1 (the "License"); you may not use this file
 *  except in compliance with the License. You may obtain a copy of
 *  the License at http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an "AS
 *  IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  rights and limitations under the License.
 ***************************************************************************/

package org.ala.client.model;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * VO for JSON
 * 
 * @author MOK011
 *
 */

public class LogEventVO implements Serializable {
    private static final long serialVersionUID = 2L;

    private String comment = "";

    private int eventTypeId = 0;

    private String userIP = "";
    
    private ConcurrentMap<String, AtomicInteger> recordCounts = new ConcurrentHashMap<>();

    private String userEmail = "";
    
    private String month = "";
    
    private Integer reasonTypeId;
    
    private Integer sourceTypeId;
    
    /** The URL that caused the event to be logged. This should allow the some reporting on the type of queries people are using to generate downloads */
    private String sourceUrl;
    
    public LogEventVO() {
    }

    public LogEventVO(LogEventType eventType, String userEmail, String comment, String userIP, ConcurrentMap<String, AtomicInteger> recordCounts) {
        this(eventType.getId(), null, null, userEmail, comment, userIP, null, recordCounts);
    }
    
    public LogEventVO(LogEventType eventType, String userEmail, String comment, String userIP, String month, ConcurrentMap<String, AtomicInteger> recordCounts) {
        this(eventType.getId(), null, null, userEmail, comment, userIP, month, recordCounts);
    }

    public LogEventVO(LogEventType eventType, Integer reasonTypeId, Integer sourceTypeId, String userEmail, String comment, String userIP, ConcurrentMap<String, AtomicInteger> recordCounts) {
        this(eventType.getId(), reasonTypeId, sourceTypeId, userEmail, comment, userIP, null, recordCounts);
    }
    
    public LogEventVO(LogEventType eventType, Integer reasonTypeId, Integer sourceTypeId, String userEmail, String comment, String userIP, String month, ConcurrentMap<String, AtomicInteger> recordCounts) {
        this(eventType.getId(), reasonTypeId, sourceTypeId, userEmail, comment, userIP, month, recordCounts);
    }
    /**
     * Create a new LogEventVO with a sourceURL that generated the log message. 
     * 
     * @param eventTypeId
     * @param reasonTypeId
     * @param sourceTypeId
     * @param userEmail
     * @param comment
     * @param userIP
     * @param month
     * @param recordCounts
     * @param sourceUrl
     */
    public LogEventVO(int eventTypeId, Integer reasonTypeId, Integer sourceTypeId, String userEmail, String comment, String userIP, String month, ConcurrentMap<String, AtomicInteger> recordCounts, String sourceUrl) {
       this(eventTypeId, reasonTypeId, sourceTypeId, userEmail, comment, userIP, month, recordCounts);
       this.sourceUrl = sourceUrl;
    }
    
    public LogEventVO(int eventTypeId, Integer reasonTypeId, Integer sourceTypeId, String userEmail, String comment, String userIP, String month, ConcurrentMap<String, AtomicInteger> recordCounts) {
        this.sourceTypeId = sourceTypeId;
        this.reasonTypeId = reasonTypeId;
        this.eventTypeId = eventTypeId;
        if(userEmail != null){
            this.userEmail = userEmail;
        }
        if(comment != null){
            this.comment = comment;
        }
        if(userIP != null){
            this.userIP = userIP;
        }
        if(month != null){
            this.month = month;
        }
        if(recordCounts != null){
            this.recordCounts = recordCounts;
        }       
    }

    public LogEventVO(int eventTypeId, Integer reasonTypeId, Integer sourceTypeId, String userEmail, String comment, String userIP, ConcurrentMap<String, AtomicInteger> recordCounts) {
        this(eventTypeId, reasonTypeId, sourceTypeId, userEmail, comment, userIP, null, recordCounts);
    }
    
    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }
    
    public ConcurrentMap<String, AtomicInteger> getRecordCounts() {
        return this.recordCounts;
    }

    public void setRecordCount(ConcurrentMap<String, AtomicInteger> recordCounts) {
        this.recordCounts = recordCounts;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUserIP() {
        return this.userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }
    
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    /**
     * To-string method.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public Integer getReasonTypeId() {
        return reasonTypeId;
    }

    public void setReasonTypeId(Integer reasonTypeId) {
        this.reasonTypeId = reasonTypeId;
    }

    public Integer getSourceTypeId() {
        return sourceTypeId;
    }

    public void setSourceTypeId(Integer sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }
    
    /*
    private String userId;

    private int month;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }   
*/  
}