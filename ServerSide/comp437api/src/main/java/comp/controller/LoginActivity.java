package comp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import comp.bean.User;
import comp.db.userdao.UserDao;

@Transactional
@RestController
public class LoginActivity {
	
	@Autowired
	UserDao userDao;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public ResponseEntity<User> login(@RequestBody User user) {
    	String name = user.getName();
    	String pw = user.getPw();
    	User existingUser = userDao.login(name, pw);
    	if (existingUser==null){
    		return new ResponseEntity<User>(existingUser, HttpStatus.UNAUTHORIZED);
    	}
    	return new ResponseEntity<User>(existingUser, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/reguser", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public ResponseEntity<User> regUser(@RequestBody User user) {
    	String name = user.getName();
    	String pw = user.getPw();
    	User newUser = userDao.addNewUser(name, pw);
    	if (newUser==null){
    		return new ResponseEntity<User>(newUser, HttpStatus.UNAUTHORIZED);
    	}
    	return new ResponseEntity<User>(newUser, HttpStatus.OK);
    }
	
	
}
