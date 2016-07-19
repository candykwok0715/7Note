package comp.db;

import java.util.Properties;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@PropertySource("classpath:config.properties")
public class DatabaseConfig {
	
	@Autowired
    Environment env;
	
	@Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] {"comp.bean"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
	
	private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
        return properties;        
    }
	
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		BasicDataSource ds = new BasicDataSource();
	    //String homedev = env.getProperty("homedev");
	    //if (homedev!=null){
	    	ds.setUrl(env.getProperty("db.home.url"));
	    	ds.setDriverClassName("org.mariadb.jdbc.Driver");
	   /* }else{
	    	ds.setUrl(env.getProperty("db.local.url"));
	    	ds.setDriverClassName("com.mysql.jdbc.Driver");
	    }*/
		ds.setUsername(env.getProperty("db.user"));
		ds.setPassword((env.getProperty("db.pw")));
		return ds;
	}
	
	@Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(sessionFactory);
       return txManager;
    }
	
}
