package oldFiles;

import model.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactManager {

    private Map<String, Contact> contactsByName =  new HashMap<>();

//    void
    public Contact addContact(String name, List<String> phoneNumbers) {

//        normalize the name:
//        ali , Ali ==> ali
        name = name.toLowerCase().trim();

//        uniq name:
        if (contactsByName.containsKey((name))) {
            System.out.println("Contact " + name + " already exists");
            return null;
        }

//        uniq numbers:
        for (Contact contact : contactsByName.values()) {  // Collection<Contact> ---> listi az contact ha
            for (String phoneNumber : phoneNumbers) { // phoneNumber --> String
                if (contact.getPhoneNumbers().contains(phoneNumber)) {
                    System.out.println("Contact " + name + " cannot be added. " +
                            "phone number " + phoneNumber + " already belongs to " + contact.getName());
                    return null;
                }
            }
        }

//        if every thing's fine:
//            Contact newContact = new Contact(name, phoneNumbers);
//        baraye imenie bishtar, chon inja be reference phoneNumbers eshare mishe behtare ye copy azash sakhte beshe va inja ezafe she. agar dar Main masalan phoneNumbersSara dastkari beshe
        Contact newContact = new Contact(name, new ArrayList<>(phoneNumbers));
        contactsByName.put(name, newContact);
        System.out.println("Contact " + name + " has been added.");

        return newContact;
    }

    public void deleteContact(String name) {
        name = name.toLowerCase().trim();

        if (!contactsByName.containsKey(name)) {
            System.out.println("Contact " + name + " does not exist");
        } else {
            contactsByName.remove(name);
            System.out.println("Contact " + name + " has been deleted.");
        }
    }

    public List<Contact> searchByName (String namePart) {
        namePart = namePart.toLowerCase().trim();

//        momkene chand mokhatab peyda beshe:

        List<Contact> results = new ArrayList<>();

//      way 1: value ro az map begirim  XXX
//        for (String name : contactsByName.keySet()) {
//            if (namePart.equals(name)) {
//                results.add(contactsByName.get(name));
//            }
//        }

//        way 2: mostaghim value ha ro darim
        for (Contact contact : contactsByName.values()) {
            if(contact.getName().contains(namePart)) {
                results.add(contact);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No results.");
        }

        System.out.println("Searching for " + namePart + ": " + results);

        return results;
    }

    public List<Contact> searchByPhone(String phonePart) {

        phonePart = phonePart.trim();
        List<Contact> result = new ArrayList<>();

        for (Contact contact : contactsByName.values()) {
            for (String phoneNumber : contact.getPhoneNumbers()) {
                if (phoneNumber.contains(phonePart)) {
                    result.add(contact);
                }
            }
        }

        if (result.isEmpty()) {
            System.out.println("No results.");
        }

        System.out.println("Searching for " + phonePart + ": " + result);
        return result;
    }

//    void
    public List<Contact> getAllContacts() {

        List<Contact> results = new ArrayList<>();

        System.out.println("📚 {");
        for(Contact contact : contactsByName.values()){
            results.add(contact);
//            System.out.println(contact.getName() + ": " + contact.getPhoneNumbers());  XXX

            System.out.println(contact);
        }
        System.out.println("}");
        return results;
    }

    public Contact updateContact(String oldName, String newName, List<String> newPhoneNumbers) {
        oldName = oldName.toLowerCase().trim();
        newName = newName.toLowerCase().trim();


//          checking if this name exists
        if (!contactsByName.containsKey(oldName)) {
            System.out.println("Contact " + oldName + " cannot find");
            return null;
        }

        Contact existingContact = contactsByName.get(oldName);

//        if newName is unique:
//        when newName and oldName are the same, an existing contact want to change -->   sara -> sara
//        I want only change the phone numbers:
        if (!newName.equals(oldName) && contactsByName.containsKey(newName)) {
            System.out.println("Contact " + newName + " already exists");
            return null;
        }


//          checking if the number is unique
        for (Contact contact : contactsByName.values()) {  // in contactsByName

//           If this is the object we are updating:
//           Do these two variables point to the exact same object in memory?
            if (contact == existingContact) {
                continue;
            }

            for (String phoneNumber : newPhoneNumbers) {    // in newPhonNumbers

                if (contact.getPhoneNumbers().contains(phoneNumber)) {
                    System.out.println("phone number " + phoneNumber + " cannot add, it already belongs to " + contact.getName());
                    return null;
                }
            }
        }

//        تا وقتی حداقل یک Reference به آبجکت وجود داره، اون آبجکت پاک نمی‌شه.

//        update the phone numbers:
        existingContact.setPhoneNumbers(new ArrayList<>(newPhoneNumbers));

//        update name:
        if (!newName.equals(oldName)) {
            contactsByName.remove(oldName);
            existingContact.setName(newName);
            contactsByName.put(newName, existingContact);
        }

        System.out.println("Contact " + oldName + " updated: " + existingContact);

        return existingContact;
    }
}
