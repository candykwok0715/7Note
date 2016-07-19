package comp.db.userdao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import comp.bean.User;
import comp.db.AbstractDao;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao implements UserDao {

	@Override
	public User addNewUser(String name, String pw) {
		User existingUser = getUserByName(name);
		if (existingUser!=null){
			return null;
		}
		User newUser = new User(name, pw);
		persist(newUser);
		existingUser = getUserByName(name);
		return existingUser;
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUserByName(String name){
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("name",name));
		List<User> existingUsers = (List<User>) criteria.list();
		if (existingUsers.isEmpty()){
			return null;
		}
		return existingUsers.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public User login(String name, String pw) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("name",name));
		criteria.add(Restrictions.eq("pw",pw));
		List<User> existingUsers = (List<User>) criteria.list();
		if (existingUsers.isEmpty()){
			return null;
		}
		return existingUsers.get(0);
	}
}
