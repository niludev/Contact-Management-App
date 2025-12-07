package repository;

import config.ApplicationContext;
import model.Contact;
import repository.interfaces.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryContactRepositoryImpl implements ContactRepository {

    private final Map<String, Contact> contactsInMemory;

    public InMemoryContactRepositoryImpl () {
        this.contactsInMemory = ApplicationContext.getInstance().getContactsInMemory();
    }


    @Override
    public Contact add(Contact contact) {

        contactsInMemory.put(contact.getName(), contact);
        return contact;
    }

    @Override
    public Contact remove(String name) {

        Contact contact = contactsInMemory.get(name);
        contactsInMemory.remove(name);
        return contact;
    }

    @Override
    public Contact findByName(String name) {

        return contactsInMemory.get(name);
    }

//    @Override
//    public Contact findContactByPhoneNumber(String phoneNumber) {
//
//        for (Contact contact : contactsInMemory.values()) {
//            if (contact.getPhoneNumbers().contains(phoneNumber)) {
//                return contact;
//            }
//        }
//
//        return null;
//    }

    @Override
    public Contact update(Contact contact) {

//        put meghdare ghablie in key ro bar migardune (age ghablan chizi bude) age key jadid bashe null barmigardune
        contactsInMemory.put(contact.getName(), contact);
        return contact;
    }

    @Override
    public List<Contact> findAll() {

        return new ArrayList<>(contactsInMemory.values());
    }

    @Override
    public boolean existsPhoneNumber(String phone) {

        for (Contact contact : contactsInMemory.values()) {
            if (contact.getPhoneNumbers().contains(phone)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Contact> searchByPhonePart(String phonePart) {
        List<Contact> foundedContacts = new ArrayList<>();

        for (Contact contact : contactsInMemory.values()) {
            for (String phoneNumber : contact.getPhoneNumbers()) {
                if (phoneNumber.contains(phonePart)) {
                    foundedContacts.add(contact);
                    break;  // Each person can only be added once
                }
            }
        }

        return foundedContacts;
    }

    @Override
    public List<Contact> searchByNamePart(String namePart) {
        List<Contact> foundedContacts = new ArrayList<>();

        for (Contact contact : contactsInMemory.values()) {
            if (contact.getName().contains(namePart)) {
                foundedContacts.add(contact);
            }
        }

        return foundedContacts;
    }

    @Override
    public boolean existsPhoneNumberForOtherContacts(String phone, String excludedName) {

        for (Contact contact : contactsInMemory.values()) {
            if (contact.getName().equals(excludedName)) { // skip itself
                continue;
            }

            if (contact.getPhoneNumbers().contains(phone)) {
                return true;
            }
        }

        return false;
    }


}
