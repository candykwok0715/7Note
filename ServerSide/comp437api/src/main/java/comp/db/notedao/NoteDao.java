package comp.db.notedao;

import java.util.List;

import comp.bean.Note;

public interface NoteDao {
	void addNote(Note note);
	
	void updateNote(Note note);
	
    void saveNoteList(Long userId, List<Note> notes);
     
    List<Note> findNoteByUserId(Long userId);
}