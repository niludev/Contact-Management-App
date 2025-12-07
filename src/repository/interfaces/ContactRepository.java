package repository.interfaces;

import model.Contact;

import java.util.List;

public interface ContactRepository {

    Contact add(Contact contact);

    Contact remove(String name);

    Contact findByName(String name);

//    Contact findContactByPhoneNumber(String phoneNumber);

    Contact update(Contact contact);

    List<Contact> findAll();

    boolean existsPhoneNumber(String phone);

    List<Contact> searchByPhonePart(String phonePart);

    List<Contact> searchByNamePart(String namePart);

    boolean existsPhoneNumberForOtherContacts(String phone, String excludedName);
}