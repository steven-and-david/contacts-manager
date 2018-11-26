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
            Files.write(p, addMe, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        buildOutContacts();
        userAddsContact();
        showAll();
        writeToTXT();
    }
}
