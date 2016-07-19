package comp.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import comp.bean.Note;
import comp.db.notedao.NoteDao;

@Transactional
@RestController
public class NoteActivity { 
	
	@Autowired
	NoteDao noteDao;
	
	@RequestMapping(value = "/note", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public ResponseEntity<List<Note>> update(@RequestBody NoteSyncRequest req){
		noteDao.saveNoteList(req.getUserId(), req.getNotes());
		List<Note> dbResultNoteList = noteDao.findNoteByUserId(req.getUserId());
		return new ResponseEntity<List<Note>>(dbResultNoteList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/note", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<Note>> getNotes(@RequestParam(value="userId",required=true) Long userId) {
		List<Note> notes = noteDao.findNoteByUserId(userId);
		return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
    }
	
}
