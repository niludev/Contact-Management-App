package config;

import model.Contact;

import repository.InMemoryContactRepositoryImpl;
import repository.JdbcContactRepositoryImpl;

import repository.interfaces.ContactRepository;

import service.ContactServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static ApplicationContext instance;
    private Connection connection;
    private Map<String, Contact> contactsInMemory;



    // Repositories
    private ContactRepository  inMemoryContactRepositoryImpl;
    private ContactRepository jdbcContactRepositoryImpl;



    // Services
    private ContactServiceImpl contactServiceImpl;



    private ApplicationContext() {}

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        ApplicationProperties.DATABASE_URL,
                        ApplicationProperties.DATABASE_USER,
                        ApplicationProperties.DATABASE_PASSWORD
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return connection;
    }


    // ---------- In Memory ----------


    public Map<String, Contact> getContactsInMemory () {
        if (contactsInMemory == null) {
            contactsInMemory =  new HashMap<>();
        }
        return contactsInMemory;
    }



    // ---------- Repositories ----------


//    lazy load:

    public ContactRepository getInMemoryContactRepositoryImpl() {
        if (inMemoryContactRepositoryImpl == null) {
            inMemoryContactRepositoryImpl = new InMemoryContactRepositoryImpl();
        }
        return inMemoryContactRepositoryImpl;
    }

    public ContactRepository getJdbcContactRepositoryImpl() {
        if (jdbcContactRepositoryImpl == null) {
            jdbcContactRepositoryImpl = new JdbcContactRepositoryImpl();
        }
        return jdbcContactRepositoryImpl ;
    }

    // ---------- Services ----------

    public ContactServiceImpl getContactServiceImpl(boolean useDatabase ) {

        if (useDatabase) {
            if(contactServiceImpl == null) {
                contactServiceImpl = new ContactServiceImpl(getJdbcContactRepositoryImpl());
            }
            return contactServiceImpl;
        } else {
            if(contactServiceImpl == null) {
                contactServiceImpl = new ContactServiceImpl(getInMemoryContactRepositoryImpl());
            }
            return contactServiceImpl;
        }


//        better way:
//        if (contactServiceImpl == null) {
//            ContactRepository repo = useDatabase
//                    ? getJdbcContactRepositoryImpl()
//                    : getInMemoryContactRepositoryImpl();
//
//            contactServiceImpl = new ContactServiceImpl(repo);
//        }
//        return contactServiceImpl;
    }
}
