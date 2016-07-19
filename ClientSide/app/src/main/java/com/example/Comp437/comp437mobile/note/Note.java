package com.example.kwoksinman.comp437mobile.note;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KwokSinMan on 23/3/2016.
 */
public class Note {
    private long noteId;
    private String title;
    private String content;
    private String userId;
    private String isDeleted;
    private long serverNoteId;
    private long lastUpdate;

    public Note(){}

    public Note(String title, String content, String userId, String isDeleted, long serverNoteId, long lastUpdate){
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.serverNoteId = serverNoteId;
        this.lastUpdate = lastUpdate;
    }

    public Note(long noteId,String title, String content, String userId, String isDeleted, long serverNoteId, long lastUpdate){
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.serverNoteId = serverNoteId;
        this.lastUpdate = lastUpdate;
    }

    public long getNoteId() {
        return noteId;
    }
    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
    public long getServerNoteId() {
        return serverNoteId;
    }
    public void setServerNoteId(long serverNoteId) {
        this.serverNoteId = serverNoteId;
    }
    public long getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String toString(){
        return "noteId="+noteId+" title="+title + " content="+content +" userId="+userId +" isDeleted=" + isDeleted + "serverNoteId="+ serverNoteId+ " lastUpdate="+ lastUpdate;
    }

    public JSONObject getNoteJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("isdeleted", isDeleted);
            json.put("userid", userId);
            json.put("content", content);
            json.put("title", title);
            json.put("lastupdate",lastUpdate);
            json.put("noteId",serverNoteId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

}