package comp.db.userdao;

import comp.bean.User;

public interface UserDao {
	public User addNewUser(String name, String pw);
	
	public User login(String name, String pw);

	User getUserByName(String name);
}
