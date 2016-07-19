package comp.controller;

import java.util.List;

import comp.bean.Note;

public class NoteSyncRequest {
	private Long userId;
	private List<Note> notes;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<Note> getNotes() {
		return notes;
	}
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
}
