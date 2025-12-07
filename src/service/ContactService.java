package service;

import model.Contact;

import java.util.List;

public interface ContactService {

    Contact addContact(String name, List<String> phoneNumbers);

    Contact deleteContact(String name);

    List<Contact> searchByNamePart(String namePart);

    List<Contact> searchByPhonePart(String phonePart);

    List<Contact> getAllContacts();

    Contact updateContact(String oldName, String newName, List<String> newPhoneNumbers);

    boolean existsPhoneNumber(String phone);


}
