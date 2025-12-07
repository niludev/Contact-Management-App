package service;

import model.Contact;
import repository.interfaces.ContactRepository;
import service.exeptions.ContactValidationException;

import java.util.ArrayList;
import java.util.List;

public class ContactServiceImpl implements ContactService{

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }


    @Override
    public Contact addContact(String name, List<String> phoneNumbers) {

        for (String phone : phoneNumbers) {
            if (!isValidPhoneNumber(phone)) {
                throw new ContactValidationException("Invalid phone number: " + phone);
            }
        }

        if (name == null) {
            throw new ContactValidationException("Name cannot be null");
        }

        name = name.toLowerCase().trim();
        if (name.isEmpty()) {
            throw new ContactValidationException("Name cannot be empty");
        }

        if (phoneNumbers == null) {
            phoneNumbers = new ArrayList<>();
        }
        Contact existing = contactRepository.findByName(name);

//      uniq name:
        if (existing != null) {
            throw new ContactValidationException("Contact with name '" + name + "' already exists");
        }


//      uniq numbers:
        List<String> cleanedPhones = new ArrayList<>();
        for (String phoneNumber : phoneNumbers) {
            if (phoneNumber == null) {
                continue;
            }

            String normalizedPhone = phoneNumber.trim();
            if (normalizedPhone.isEmpty()) {
                continue;
            }

            if (contactRepository.existsPhoneNumber(normalizedPhone)) {
                throw new ContactValidationException("Phone number '" + normalizedPhone + "' already exists");
            }

            cleanedPhones.add(normalizedPhone);
        }


//      if every thing's fine:
        Contact contact = new Contact(name, cleanedPhones);
        contactRepository.add(contact);

        return contact;
    }

    // ---------------------- addContact and updateContact Helper ----------------------

    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }

        for (char ch : phone.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }


    @Override
    public Contact deleteContact(String name) {

        if (name == null) {
            throw new ContactValidationException("Name cannot be null");
        }

        name = name.toLowerCase().trim();
        if (name.isEmpty()) {
            throw new ContactValidationException("Name cannot be empty");
        }

        Contact existing = contactRepository.findByName(name);
        if (existing == null) {
            throw new ContactValidationException("Contact with name '" + name + "' not found");
        }

        return contactRepository.remove(name);
    }

    @Override
    public List<Contact> searchByNamePart(String namePart) {

        namePart = namePart.toLowerCase().trim();
        return contactRepository.searchByNamePart(namePart);
    }

    @Override
    public List<Contact> searchByPhonePart(String phonePart) {

        phonePart = phonePart.trim();
       return contactRepository.searchByPhonePart(phonePart);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contact updateContact(String oldName, String newName, List<String> newPhoneNumbers) {

        for (String phone : newPhoneNumbers) {
            if (!isValidPhoneNumber(phone)) {
                throw new ContactValidationException("Invalid phone number: " + phone);
            }
        }

        if (oldName == null || newName == null) {
            throw new ContactValidationException("Names cannot be null");
        }

        oldName = oldName.toLowerCase().trim();
        newName = newName.toLowerCase().trim();

        if (oldName.isEmpty() || newName.isEmpty()) {
            throw new ContactValidationException("Names cannot be empty");
        }

        if (newPhoneNumbers == null) {
            newPhoneNumbers = new ArrayList<>();
        }

//      checking if this name exists
        Contact existingContact = contactRepository.findByName(oldName);
        if (existingContact == null) {
            throw new ContactValidationException("Contact with name '" + oldName + "' not found");
        }

//      if newName is unique:
        if (!newName.equals(oldName) && contactRepository.findByName(newName) != null) {
            throw new ContactValidationException("Contact with name '" + newName + "' already exists");
        }

//      checking if the number is unique:
        List<String> cleanedPhones = new ArrayList<>();

        for (String phoneNumber : newPhoneNumbers) {

            if (phoneNumber == null) {
                continue;
            }

            String normalizedPhone = phoneNumber.trim();
            if (normalizedPhone.isEmpty()) {
                continue;
            }

            if(contactRepository.existsPhoneNumberForOtherContacts(normalizedPhone, oldName)) {
                throw new ContactValidationException("Phone number '" + normalizedPhone + "' already exists for another contact");
            }

            cleanedPhones.add(normalizedPhone);
        }


//      update the phone numbers:
        existingContact.setPhoneNumbers(cleanedPhones);

        if (!newName.equals(oldName)) {
            contactRepository.remove(oldName);
            existingContact.setName(newName);
            contactRepository.update(existingContact);
        } else {
            contactRepository.update(existingContact);
        }

        return existingContact;
    }

    @Override
    public boolean existsPhoneNumber(String phone) {
        if (phone == null) {
            return false;
        }

        phone = phone.trim();

        return contactRepository.existsPhoneNumber(phone);
    }

//    old way:
//    @Override
//    public boolean existsPhoneNumber(String phone) {
//
//        List<Contact> allContacts = contactRepository.findAll();
//
//        for (Contact contact : allContacts) {
//            if (contact.getPhoneNumbers().contains(phone)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
}
