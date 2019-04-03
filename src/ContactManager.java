import util.Input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
        System.out.println("5. Edit a contact");
    }

    public static String formatNum(String pNum) {
        if (pNum.indexOf("-") == 3 && pNum.lastIndexOf("-") == 7 && pNum.length() == 12) {
            return pNum;
        } else if (pNum.indexOf("-") == 3 && pNum.length() == 8) {
            return pNum;
        } else if (pNum.length() == 10) {
            String firstThree = pNum.substring(0,3);
            String midThree = pNum.substring(3,6);
            String lastFour = pNum.substring(6,10);
            return firstThree + "-" + midThree + "-" + lastFour;
        } else if (pNum.length() == 7) {
            String firstThree = pNum.substring(0,3);
            String lastFour = pNum.substring(3,7);
            return firstThree + "-" + lastFour;
        } else {
            return "notgood";
        }
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

    public static String getPNum (){
        String cPhoneNumber;
        while (true) {
            System.out.println("Please enter phone number:");
            cPhoneNumber = sc.getString();
            try {
                return formatNum(cPhoneNumber);
            } catch (Exception e) {
                System.out.println("That's no good...");
            }
        }
    }

    public static void userAddsContact() {
        int hashSize = contactList.size();
        boolean running = true;
        while (running) {
            System.out.println("Please enter name:");
            String cName = sc.getString();
            String cPhoneNumber = getPNum();
            if (!cPhoneNumber.equalsIgnoreCase("notgood")) {
                Contact addMe = new Contact(cName, cPhoneNumber);
                addContact(addMe);
                if (hashSize == contactList.size()) {
                    System.out.println("Contact already exists in contact list. Would you like to edit this" +
                            " contact?");
                    boolean answer = sc.yesNo();
                    if (answer) {
                        editContact(addMe);
                        running = false;
                        System.out.println("Contact information successfully changed");
                    } else {
                        System.out.println("Returning to main menu...");
                    }
                } else {
                    running = false;
                    System.out.println("Contact added!");
                }
            } else {
                System.out.println("Phone Number is invalid... Try again.");
            }
        }
    }

    public static int longestName() {
        int length = 0;
        for (String name : contactList.keySet()) {
            if (name.length() > length) {
                length = name.length();
            }
        }
        return length;
    }

    private static HashMap addContact(Contact contact) {
        contactList.putIfAbsent(contact.getName(), contact.getPhoneNum());
        return contactList;
    }

    private static HashMap editContact(Contact contact) {
        contactList.replace(contact.getName(), contact.getPhoneNum());
        return contactList;
    }

    public static void showAll() {
        String header = "| Name";
        String line = "===~~~";
        for (int i = longestName() - 4; i > 0; i--){
            header += " ";
            line += "-";
        }
        header += " | Phone Number |";
        line += "-----------~~~===";
        System.out.println(line);
        System.out.println(header);
        System.out.println(line);
        Set<String> names = new TreeSet<>(contactList.keySet());
        for (String name : names) {
            String pNum = contactList.get(name);
            String returnName = "| " + name;
            int startAt = longestName() - name.length();
            if (name.length() == longestName()) {
                if(pNum.length() == 12) {
                    System.out.println("| " + name + " | " + pNum + " |");
                } else {
                    System.out.println("| " + name + " | " + pNum + "     |");
                }
            } else {
                for (int i = startAt; i > 0; i--){
                    returnName += " ";
                } if(pNum.length() == 12) {
                    System.out.println(returnName + " | " + pNum + " |");
                } else {
                    System.out.println(returnName + " | " + pNum + "     |");
                }
            }
        }
        System.out.println(line);
    }

    public static void userEditsContact() {
        boolean running = true;
        while (running) {
            System.out.println("Please enter the name to edit contact:");
            String cName = sc.getString();
            String cPhoneNumber = getPNum();
            if (!cPhoneNumber.equalsIgnoreCase("notgood")) {
                Contact addMe = new Contact(cName, cPhoneNumber);
                if (contactList.containsKey(cName)) {
                    System.out.println("Are you sure you want to edit the contact? Previous contact information" +
                            " will be lost.");
                    boolean answer = sc.yesNo();
                    if (answer) {
                        editContact(addMe);
                        running = false;
                        System.out.println("Contact information successfully changed");
                    } else {
                        System.out.println("Contact information unchanged");
                    }
                } else {
                    System.out.println("Contact does not exist in list. Returning to main menu...");
                }
            } else {
                System.out.println("Phone Number is invalid... Try again.");
            }
        }
    }

    public static void writeToTXT() {
        List<String> addMe = new ArrayList<>();
        Set<String> names = new TreeSet<>(contactList.keySet());
        for (String name : names) {
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
            userInput = sc.getIntegerSecret(0 , 6);
            switch (userInput) {
                case 1 :
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
                case 5:
                    sc.clear();
                    userEditsContact();
                    break;
                case 6 :
                    youGotTheMonster();
                    break;
                default:
                    sc.clear();
                    System.out.println("Are you sure you'd like to quit?");
                    boolean answer = sc.yesNo();
                    if (!answer) {
                        userInput = 1;
                        break;
                    } else {
                        break;
                    }
            }
        } while (userInput != 0);
        writeToTXT();
        System.out.println("Have fun on your phone!");
    }














































































































































    public static void youGotTheMonster() {
        System.out.println("@                                                                                              \n" +
                "                                                     *****    %***                             \n" +
                "                                                     (****    ******                           \n" +
                "                                                      ***    ******                            \n" +
                "                                                      ***    *****                             \n" +
                "                                                      ***   *****    ***                       \n" +
                "            ***                                       **(   ****   ******                      \n" +
                "   ******# ****                                       **   ***   *****                         \n" +
                "    ***********                                            **  ****                            \n" +
                "****************                                 (((          **                               \n" +
                "   **************                           (((((((((((                                        \n" +
                "  ****************                           (((((((((((                                       \n" +
                "    ****************                      #*((((((                                             \n" +
                "      ***************                #//**...*********/    ((((((((                            \n" +
                "       **************/             ///...****************&    (((((((                          \n" +
                "       *************////         ///.,(((/****((((((********   ((((                            \n" +
                "        ************/////      /////**(***/(,...   ..(*******    ((                            \n" +
                "        ***/*******///////#   /////((***/(...        .(********                       ***      \n" +
                "         **//******///////// ////(((***/(...   /      (...,*****                    %*****     \n" +
                "          ////****//////////////((((***/(....(/%/.  ..(    ./****                /********     \n" +
                "          /////***////////////(((/(****/(,..........,((    ./****            ///*****/*******  \n" +
                "           /////*////////////(((..,(****//(,,,...,((**#.....(*****        /////****************\n" +
                "           //////////////////((((.((*****//////////***(....//**((*     ///////************** **\n" +
                "            ////////////////(((((,.((******************/////******  /////////******************\n" +
                "             ///////////////(((((...((**/**********************/**//////////*******/******  ***\n" +
                "             */////////////((((((...(((***************/***/****(**/////////******/***   ***    \n" +
                "              //////////////.,(((((((..((*********************(***////////*****//***           \n" +
                "               ////////////(.((((((((....((******************(****///////****///**/            \n" +
                "                ////////////(...((((((((((.,(((***********((******//////**////***              \n" +
                "                *///////////(..(((((((((((.....,((((((((/.(*****/////////////***               \n" +
                "                 ////////////((...((((((((....(.....((...(*/////////////////**                 \n" +
                "                  ////////////(...*/////((((((((...((((((//////////////////*                   \n" +
                "                   /////////////(..../////////..//(((((((/////////////////                     \n" +
                "                  (///////////////(./....////////,//((,.((//////////////                       \n" +
                "                  //////////////////((..//.....//...((...(////////////                         \n" +
                "                 ///////////////////////((.....//....((/////////////                           \n" +
                "                 ////////////////////////////////////////////////                              \n" +
                "                ///////////////////////////,,,,,,,/////////////                                \n" +
                "               //////////////////////////(//////////((////////(                                \n" +
                "              ///////////////////////////////(((//////////////                                 \n" +
                "             /////////////////////////////////////////////////                                 \n" +
                "            #////////////////////////////////////////((//////                                  \n" +
                "           %////////////////////////////////////////((///////                                  \n" +
                "           /////////////////////////////////////////////////                                   \n" +
                "         #/////////////////////////////////////////(((//////                                   \n" +
                "        /////////////////////////////////////////((((//////                                    \n" +
                "       //////////////////////////////////////((/(((////////                                    \n" +
                "      /////////////////////////////////////(((////////////                                     \n" +
                "     ////////////////////////////////////////////////////(                                     \n" +
                "   //////////////////////////////////////////////////////                                      \n" +
                "\n SECRET OPTION");
    }
}
