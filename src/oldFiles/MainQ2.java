package oldFiles;

import model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainQ2 {
    public static void main(String[] args) {



        ContactManager contactManager = new ContactManager();

        System.out.println("////////////////////////////// add: ///////////////////////////////////");
//        Arezoo: +
        System.out.println("Arezoo");

        List<String> phoneNumbersArezoo = new ArrayList<>();
        phoneNumbersArezoo.add("+48 555-5555");
        phoneNumbersArezoo.add("+48 555-1111");
        phoneNumbersArezoo.add("+48 555-3333");
        phoneNumbersArezoo.add("+48 555-2222");

        Contact arezoo = contactManager.addContact("Arezoo",phoneNumbersArezoo);
        System.out.println("contact Arezoo: " + arezoo);

        contactManager.addContact("Arezoo",phoneNumbersArezoo);


        System.out.println();
        System.out.println("//////////////////////////////////////////////////////////////////////////");
//         Ali: 0
        System.out.println("Ali");

        List<String> phoneNumbersAli = new ArrayList<>();
        phoneNumbersAli.add("+48 555-5555");

        Contact ali = contactManager.addContact("Ali",phoneNumbersAli);
        System.out.println("contact Ali: " + ali);


        System.out.println();
        System.out.println("//////////////////////////////////////////////////////////////////////////");
//         Sara: +
        System.out.println("Sara");

        List<String> phoneNumbersSara = new ArrayList<>();
        phoneNumbersSara.add("+48 555-4555");
        phoneNumbersSara.add("+48 455-5555");
        phoneNumbersSara.add("+48 555-4333");
        phoneNumbersSara.add("+48 555-4222");


//        contactManager.addContact("Sara",phoneNumbersSara);

        Contact sara = contactManager.addContact("Sara",phoneNumbersSara);
        System.out.println("contact Sara: " + sara);
        contactManager.searchByName("sa");


        System.out.println();
        System.out.println("////////////////////////////// Test: ///////////////////////////////////");
//      show the security bug:
//      if   Contact newContact = new Contact(name, phoneNumbers);

//        ina dg alan kar nemikonan
        phoneNumbersSara.clear();
        phoneNumbersSara.add("+48 555-5555");
        System.out.println("Test contact sara with the number +48 555-5555: " + sara);


        System.out.println();
        System.out.println("////////////////////////////// delete: ///////////////////////////////////");
//         Sara: -
        System.out.println("Sara");
        contactManager.deleteContact("Sara");
        System.out.println("contact sara: " + sara);

//          Sara: +
        contactManager.addContact("Sara",phoneNumbersSara);


        System.out.println();
        System.out.println("////////////////////////////// searchByName: ///////////////////////////////////");
//         --> Arezoo
        contactManager.searchByName("ar");


        System.out.println();
        System.out.println("////////////////////////////// searchByPhone: ///////////////////////////////////");
//         --> Arezoo
        contactManager.searchByPhone("5555");


        System.out.println();
        System.out.println("////////////////////////////// add: ///////////////////////////////////");
//        Lilly: +
        System.out.println("Lilly");

        List<String> phoneNumbersLilly = new ArrayList<>();
        phoneNumbersLilly.add("+48 255-5555");
        phoneNumbersLilly.add("+48 525-1111");
        phoneNumbersLilly.add("+48 255-3333");
        phoneNumbersLilly.add("+48 525-2222");

        Contact lilly = contactManager.addContact("Lilly",phoneNumbersLilly);
        System.out.println("contact Lilly: " + lilly);



        System.out.println();
        System.out.println("////////////////////////////// getAllContacts: ///////////////////////////////////");

        contactManager.getAllContacts();
        System.out.println();

        System.out.println("////////////////////////////// updateContact: ///////////////////////////////////");
        contactManager.updateContact("arezoo", "arezoo1", new ArrayList<>(List.of("+48 300-7788", "+48 300-7777")));
        contactManager.updateContact("diva", "max", new ArrayList<>(List.of("+48 999-8000", "+48 999-8800")));


        System.out.println();
        System.out.println("////////////////////////////// getAllContacts: ///////////////////////////////////");

        contactManager.getAllContacts();
        System.out.println();


    }
//
//    public void addContact2 () {
//        Scanner scanner = new Scanner(System.in);
//        String name = scanner.nextLine();
//        String phone = scanner.nextLine();
//    }


}
