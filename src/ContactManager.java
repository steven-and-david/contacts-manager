import util.Input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.awt.SystemColor.info;

public class ContactManager {

    public static HashMap<String, String> contactList = new HashMap<>();
    public static Input sc = new Input();
    public static Path p = Paths.get("./src/contacts.txt").normalize();

    public static void buildOutContacts() {

        List<String> contacts = new ArrayList<>();
        try {
            contacts = Files.readAllLines(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String contactInfo : contacts){
            String[] info = contactInfo.split(" \\| ");
            contactList.putIfAbsent(info[0], info[1]);
        }
    }

    public static void userMenu() {
        System.out.println("\nContacts Manager");

        System.out.println("\n\nWhat would you like to do?");
        System.out.println("0. Exit");
        System.out.println("1. Show all contacts");
        System.out.println("2. Add a contact");
        System.out.println("3. Delete a contact");
        System.out.println("4. Search for a contact");

    }


    public static void removeContact() {
        System.out.println("Please enter the name of the contact you would like to remove");
        String cName = sc.getString();
        if (contactList.containsKey(cName)) {
            contactList.remove(cName);
            System.out.println("Contact has successfully been removed!");
        } else {
            System.out.println("Contact is not in list");
        }
    }

    public static void searchContact() {
        System.out.println("Please enter the name of the contact you would like to search");
        String cName = sc.getString();
        if (contactList.containsKey(cName)) {
            System.out.println(cName + " | " + contactList.get(cName));
        } else {
            System.out.println("Contact is not in list");
        }
    }

    public static void userAddsContact() {
        System.out.println("Please enter name:");
        String cName = sc.getString();
        System.out.println("Please enter phone number:");
        String cPhoneNumber = sc.getString();
        Contact addMe = new Contact(cName, cPhoneNumber);
        addContact(addMe);
    }

    public static HashMap addContact(Contact contact) {
        contactList.put(contact.getName(), contact.getPhoneNum());
        return contactList;
    }

    public static void showAll() {
        for (String name : contactList.keySet()) {
            String pNum = contactList.get(name);
            System.out.println(name + " | " + pNum);
        }
    }

    public static void writeToTXT() {
        List<String> addMe = new ArrayList<>();

        for (String name : contactList.keySet()) {
            String pNum = contactList.get(name);
            addMe.add(name + " | " + pNum);
        }

        try {
            Files.write(p, addMe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int userInput;
        buildOutContacts();


        do {
            userMenu();
            userInput = sc.getIntegerSecret(0 , 5);
            switch (userInput) {
                case (1) :
                    showAll();
                    break;
                case 2 :
                    sc.clear();
                    userAddsContact();
                    break;
                case 3 :
                    sc.clear();
                    removeContact();
                    break;
                case 4 :
                    sc.clear();
                    searchContact();
                    break;
            }
        } while (userInput != 0);
    }
}
