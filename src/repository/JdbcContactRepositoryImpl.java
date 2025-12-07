package repository;

import config.ApplicationContext;
import model.Contact;
import repository.interfaces.ContactRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcContactRepositoryImpl implements ContactRepository {

    Connection connection;

    public JdbcContactRepositoryImpl () {
        this.connection = ApplicationContext.getInstance().getConnection();
    }

//   -------------- 1. insert into contacts tables 2. insert into tables contact_phone_numbers --------------
    @Override
    public Contact add(Contact contact) {

        String sql = "INSERT INTO contacts (name) VALUES (?)";

        try {
            PreparedStatement pps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

//            1. insert into contacts
            pps.setString(1, contact.getName());
            pps.executeUpdate();

            ResultSet rs = pps.getGeneratedKeys();

            if (rs.next()) {
                contact.setId(rs.getLong(1));
            }
//            2. insert into contact_phone_numbers
//            savePhoneNumbers(contact.getId(), contact.getPhoneNumbers());
            List<String> phoneNumbers = contact.getPhoneNumbers();
            if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
                savePhoneNumbers(contact.getId(), phoneNumbers);
            }

            return contact;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    -------------- Helper for add --------------
    public void savePhoneNumbers(Long contact_id, List<String> phoneNumbers) {
        String sql = "INSERT INTO contact_phone_numbers (contact_id, phone_number) VALUES (?, ?)";

        try {
            PreparedStatement pps = connection.prepareStatement(sql);

            for (String phoneNumber : phoneNumbers) {
                pps.setLong(1, contact_id);
                pps.setString(2, phoneNumber);
                pps.executeUpdate();

//                better performance
//                pps.addBatch();
            }

//                pps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Contact remove(String name) {

        Contact contact = findByName(name);
        if (contact == null) {
            return null;
        }

        String sql = "DELETE FROM contacts WHERE name = ?";

        try {
            PreparedStatement pps = connection.prepareStatement(sql);

            pps.setString(1, name);
            pps.executeUpdate();

            return contact;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Contact findByName(String name) {

        String sql = "SELECT id, name FROM contacts WHERE name = ?";

        try {
            PreparedStatement pps = connection.prepareStatement(sql);

            pps.setString(1, name);
            ResultSet rs = pps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Long contactId = rs.getLong("id");
            String contactName = rs.getString("name");

            List<String> phoneNumbers = loadAllPhoneNumbersForEachContact(contactId);

            Contact contact = new Contact(contactName, phoneNumbers);
            contact.setId(contactId);

            return contact;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    -------------- Helper for findByName --------------
    private List<String> loadAllPhoneNumbersForEachContact(Long contactId) {
        String sql = "SELECT phone_number FROM contact_phone_numbers WHERE contact_id = ?";

        List<String> foundedPhoneNumbers = new ArrayList<>();

        try {

            PreparedStatement pps = connection.prepareStatement(sql);
            pps.setLong(1, contactId);

            ResultSet rs = pps.executeQuery();


            while (rs.next()) {
                foundedPhoneNumbers.add(rs.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return foundedPhoneNumbers;
    }


//    @Override
//    public Contact findContactByPhoneNumber(String phoneNumber) {
//        return null;
//    }

//    Delete all numbers and save again:
    @Override
    public Contact update(Contact contact) {

//        if sara --> bob ==> remove = null
//        if sara --> sara ==> remove old sara contact and add new sara contact
        remove(contact.getName());
        add(contact);
        return contact;
    }

    @Override
    public List<Contact> findAll() {

        String sql = "SELECT id, name FROM contacts ORDER BY name";

        List<Contact> contactList = new ArrayList<>();

        try {
            PreparedStatement pps = connection.prepareStatement(sql);
            ResultSet rs = pps.executeQuery();

            while (rs.next()) {
                Long contactID = rs.getLong("id");
                String name = rs.getString("name");

                List<String> phoneNumbers = loadAllPhoneNumbersForEachContact(contactID);

                Contact contact = new Contact(name, phoneNumbers);
                contact.setId(contactID);

                contactList.add(contact);
            }

            return contactList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsPhoneNumber(String phoneNumber) {

//        Just check the existence of the record:
        String sql = "SELECT 1 FROM contact_phone_numbers WHERE phone_number = ?";

        try {
            PreparedStatement pps = connection.prepareStatement(sql);

            pps.setString(1, phoneNumber);
            ResultSet rs = pps.executeQuery();

//            If phone exists a row is returned with the value 1
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public List<Contact> searchByPhonePart(String phonePart) {

//        Shows each contact only once ....> DISTINCT
        String sql = "SELECT DISTINCT c.id, c.name " +
                     "FROM contacts c " +
                     "JOIN contact_phone_numbers p " +
                     "ON c.id = p.contact_id " +
                     " WHERE phone_number LIKE ?";

        List<Contact> foundedContacts = new ArrayList<>();

        try {
            PreparedStatement pps = connection.prepareStatement(sql);

            pps.setString(1, "%" + phonePart + "%");
            ResultSet rs = pps.executeQuery();

            while (rs.next()) {
                Long contactId = rs.getLong(1);
                String name = rs.getString(2);

                List<String> phoneNumbers = loadAllPhoneNumbersForEachContact(contactId);

                Contact contact = new Contact(name, phoneNumbers);
                contact.setId(contactId);
                foundedContacts.add(contact);
            }

            return foundedContacts;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Contact> searchByNamePart(String namePart) {
        String sql = "SELECT id, name FROM contacts WHERE name LIKE ?";

        List<Contact> foundedContacts = new ArrayList<>();

        try {
            PreparedStatement pps = connection.prepareStatement(sql);

            pps.setString(1, "%" + namePart + "%");
            ResultSet rs = pps.executeQuery();

            while (rs.next()) {
                Long contactId = rs.getLong(1);
                String name = rs.getString(2);

//                N + 1 query
                List<String> phoneNumbers = loadAllPhoneNumbersForEachContact(contactId);

                Contact contact = new Contact(name, phoneNumbers);
                contact.setId(contactId);

                foundedContacts.add(contact);
            }


//            11 queries: 1 + 10 ---> when there is 10 results:
//            while (rs.next()) {
//                foundedContacts.add(findByName(rs.getString(2)));
//            }

            return foundedContacts;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsPhoneNumberForOtherContacts(String phone, String excludedName) {

        String sql = "SELECT 1 " +
                "FROM contacts c " +
                "JOIN contact_phone_numbers p ON c.id = p.contact_id " +
                " WHERE phone_number = ? AND name <> ?";

        try {
            PreparedStatement pps = connection.prepareStatement(sql);

            pps.setString(1, phone);
            pps.setString(2, excludedName);

            ResultSet rs = pps.executeQuery();

            if(rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
