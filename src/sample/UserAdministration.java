package sample;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;

public class UserAdministration {

    ArrayList<User> users;

    String filename = "Users.txt";

    public UserAdministration() {
        users = new ArrayList<>();
        try {
            setUsers();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for (User user: users) {
            System.out.println("name: " + user.getName());
            System.out.println("key: " + Arrays.toString(user.getKey()));
        }

    }

    public void setUsers() throws IOException{
        File file = new File(filename);

        if (file.exists()) {
            byte[] encoded = Files.readAllBytes(Paths.get(filename));

            String usersString =  new String(encoded, "UTF-8");

            String[] userStrings = usersString.split(";");

            for (String userString: userStrings) {
                String[] attributes = userString.split("/");
                if(attributes.length == 3) {
                    users.add(new User(Integer.parseInt(attributes[0]), attributes[1], attributes[2].getBytes(Charset.forName("UTF-8"))));
                }
            }
        }
    }

    public void createUser(int id, String name, String key) {
        MessageDigest sha;
        try {
            byte[] shaKey = key.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            users.add(new User(id, name, sha.digest(shaKey)));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void saveUsers() throws IOException {
        //TODO save Users

        String usersString = "";

        for (User user: users) {
            usersString = usersString + Integer.toString(user.getId()) + "/" + user.getName() + "/" + Arrays.toString(user.getKey()) + ";";
            usersString.trim();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        fileOutputStream.write(usersString.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public ArrayList<String> getObservableUserNames() {
        ArrayList<String> userNames = new ArrayList<>();

        for (User user: users) {
            userNames.add(user.getName());
        }

        return userNames;
    }
}