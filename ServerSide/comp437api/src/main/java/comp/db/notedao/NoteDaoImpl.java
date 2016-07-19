package comp.db.notedao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import comp.bean.Note;
import comp.db.AbstractDao;

@Repository("noteDao")
public class NoteDaoImpl extends AbstractDao implements NoteDao{
	@Override
	public void addNote(Note note) {
		if (note.getLastupdate()==null || note.getLastupdate()==0){
			note.setLastupdate(System.currentTimeMillis());
		}
		persist(note);
	}
	
	@Override
	public void updateNote(Note note) {
		update(note);
	}

	@Override 
	public void saveNoteList(Long userId, List<Note> notes) {
		List<Note> dbOriginalNoteList = findNoteByUserId(userId);
		Map<Long,Note> noteMap = new HashMap<Long,Note>();
		for (Note note:dbOriginalNoteList){
			noteMap.put(note.getNoteId(), note);
		}
		for (Note reqNote:notes){
			Long noteId = reqNote.getNoteId();
			if (noteId==null||noteId==0){
				System.out.println(reqNote.toString());
				reqNote.setNoteId(null);
				addNote(reqNote);
			}else{
				Note oriNote = noteMap.get(noteId);
				Long oriNoteLastUpdate = oriNote.getLastupdate()==null?0:oriNote.getLastupdate();
				Long reqNoteLastUpdate = reqNote.getLastupdate()==null?0:reqNote.getLastupdate();
				if (reqNoteLastUpdate>oriNoteLastUpdate){
					oriNote.setTitle(reqNote.getTitle());
					oriNote.setContent(reqNote.getContent());
					oriNote.setLastupdate(System.currentTimeMillis());
					oriNote.setIsdeleted(reqNote.isIsdeleted());
					update(oriNote);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Note> findNoteByUserId(Long userId) {
		Criteria criteria = getSession().createCriteria(Note.class);
		criteria.add(Restrictions.eq("userid",userId));
		criteria.addOrder(Order.desc("lastupdate"));
		criteria.addOrder(Order.desc("id"));
		List<Note> notes = (List<Note>) criteria.list();
        return notes;
	}
}
