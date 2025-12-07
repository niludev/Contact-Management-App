package view;

import model.Contact;
import service.ContactService;
import service.ContactServiceImpl;
import service.exeptions.ContactValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactConsoleApp {

    private final ContactServiceImpl contactService;

    private final Scanner scanner = new Scanner(System.in);


    public ContactConsoleApp(ContactServiceImpl contactService) {
        this.contactService = contactService;
    }

    public void run() {
        System.out.println("===== Contact Management =====");
        mainMenu();
    }

    private void mainMenu() {

        while (true) {
            System.out.println("--- Main Menu ---");
            System.out.println("1. Add contact");
            System.out.println("2. Edit contact");
            System.out.println("3. View contact list");
            System.out.println("4. Search contact by name");
            System.out.println("5. Search contact by phone number");
            System.out.println("6. Delete contact");
            System.out.println("7. Exit");

            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addContactMenu();
                    break;
                case "2":
                    editContactMenu();
                    break;
                case "3":
                    listContactsMenu();
                    break;
                case "4":
                    searchByNameMenu();
                    break;
                case "5":
                    searchByPhoneMenu();
                    break;
                case "6":
                    deleteContactMenu();
                    break;
                case "7":
                    System.out.println("Exit from app ...");
                    return;
                default:
                    System.out.println("Wrong option!");
            }
//            if(choice.equals("1")) {
//                .....();
//                return;
//            } else if (choice.equals("2")) {
//                .....();
//                return;
//            } else if (choice.equals("3")) {
//                System.out.println("Exit from app ...");
//                return;
//            } else {
//                System.out.println("Wrong option!");
//                return;
//            }
        }
    }

    private void addContactMenu() {
        System.out.println();
        System.out.println("--- Add Contact ---");
        System.out.println("Enter 0 at any time to go back.");

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.equals("0")) {
            return;
        }

        List<String> phoneNumbers = readPhoneNumbersFromUser();
        if (phoneNumbers == null) { // user chose to go back
            return;
        }

        try {
            Contact contact = contactService.addContact(name, phoneNumbers);
            System.out.println("Contact added: " + contact.getName());
        } catch (ContactValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void editContactMenu() {
        System.out.println();
        System.out.println("--- Edit Contact ---");
        System.out.println("Enter 0 to go back.");

        System.out.print("Old name: ");
        String oldName = scanner.nextLine().trim();
        if (oldName.equals("0")) {
            return;
        }

        System.out.print("New name (leave empty to keep old name): ");
        String newName = scanner.nextLine().trim();
        if (newName.equals("0")) {
            return;
        }
        if (newName.isEmpty()) {
            newName = oldName;
        }

        System.out.println("Enter new phone numbers (0 to cancel / back):");
        List<String> newPhones = readPhoneNumbersFromUser();
        if (newPhones == null) {
            return;
        }

        try {
            Contact updated = contactService.updateContact(oldName, newName, newPhones);
            System.out.println("Contact updated: " + updated.getName());
        } catch (ContactValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listContactsMenu() {
        System.out.println();
        System.out.println("--- Contact List ---");

        List<Contact> all = contactService.getAllContacts();
        if (all.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        int index = 1;
        for (Contact c : all) {
            System.out.println(index++ + ". " + formatContact(c));
        }
    }

    private void searchByNameMenu() {
        System.out.println();
        System.out.println("--- Search by Name ---");
        System.out.println("Enter 0 to go back.");

        System.out.print("Name part: ");
        String namePart = scanner.nextLine().trim();
        if (namePart.equals("0")) {
            return;
        }

        List<Contact> found = contactService.searchByNamePart(namePart);

        if (found.isEmpty()) {
            System.out.println("No contacts found for: " + namePart);
            return;
        }

        System.out.println("Found contacts:");
        int index = 1;
        for (Contact c : found) {
            System.out.println(index++ + ". " + formatContact(c));
        }
    }


    private void searchByPhoneMenu() {
        System.out.println();
        System.out.println("--- Search by Phone ---");
        System.out.println("Enter 0 to go back.");

        System.out.print("Phone part: ");
        String phonePart = scanner.nextLine().trim();
        if (phonePart.equals("0")) {
            return;
        }

        List<Contact> found = contactService.searchByPhonePart(phonePart);

        if (found.isEmpty()) {
            System.out.println("No contacts found for phone part: " + phonePart);
            return;
        }

        System.out.println("Found contacts:");
        int index = 1;
        for (Contact c : found) {
            System.out.println(index++ + ". " + formatContact(c));
        }
    }


    private void deleteContactMenu() {
        System.out.println();
        System.out.println("--- Delete Contact ---");
        System.out.println("Enter 0 to go back.");

        System.out.print("Name to delete: ");
        String name = scanner.nextLine().trim();
        if (name.equals("0")) {
            return;
        }

        try {
            Contact deleted = contactService.deleteContact(name);
            System.out.println("Deleted contact: " + deleted.getName());
        } catch (ContactValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // ---------------------- Helpers ----------------------

    private List<String> readPhoneNumbersFromUser() {
        System.out.print("Phone numbers: ");
        String line = scanner.nextLine().trim();

        if (line.equals("0")) {
            return null; // signal "back"
        }

        List<String> result = new ArrayList<>();
        if (line.isEmpty()) {
            return result; // empty list = no phone numbers
        }

        String[] parts = line.split(",");
        for (String part : parts) {
            String phone = part.trim();
            if (!phone.isEmpty()) {
                result.add(phone);
            }
        }

        return result;
    }

    private String formatContact(Contact c) {
        return c.getName() + " | phones: " + c.getPhoneNumbers();
    }
}
