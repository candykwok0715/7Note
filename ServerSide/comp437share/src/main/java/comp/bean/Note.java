package comp.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "note")
public class Note {
	private Long noteId;
	private Long lastupdate;
	private String title;
	private String content;
	private long userid;
	private boolean isdeleted;
	
	public Note() {
		super();
	}

	public Note(long noteId, Long lastupdate, String title, String content, long userid) {
		super();
		this.noteId = noteId;
		this.lastupdate = lastupdate;
		this.title = title;
		this.content = content;
		this.userid = userid;
		this.isdeleted = false;
	}
	
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "ID")
	public Long getNoteId() {
		return noteId;
	}
	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}
	@Column(name = "lastupdate", nullable = false)
	public Long getLastupdate() {
		return lastupdate;
	}
	public void setLastupdate(Long lastupdate) {
		this.lastupdate = lastupdate;
	}
	@Column(name = "title", nullable = true, length = 100)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "content", nullable = true, length = 1000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "userid", nullable = false)
	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}
	@Column(name = "isdeleted", nullable = false)
	public boolean isIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(boolean isdeleted) {
		this.isdeleted = isdeleted;
	}
	
	@Override
	public String toString(){
		return "noteId: "+noteId+"; lastupdate :"+lastupdate+"; title: "+title+"; content: "+content+"; userid: "+userid + "; isdeleted"+isdeleted;
	}
}
