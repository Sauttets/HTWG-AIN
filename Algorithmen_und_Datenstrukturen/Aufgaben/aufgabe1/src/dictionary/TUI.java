package dictionary;

import java.io.*;
import java.util.Scanner;

public class TUI {
    public static final String ANSI_PURPLE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001b[38;2;255;128;0m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private static Dictionary<String, String> dictionary = new SortedArrayDictionary();
    private static int dictstate = 0;

    public static void main(String[] args){
        welcome();
        //System.out.println(ANSI_RED + "Welcome to the Dictionary!" + ANSI_RESET);
        //help();
        Scanner scanner = new Scanner(System.in);



        while(true){
            System.out.print("pormpt: ");
            String input = scanner.nextLine();
            String[] command = decode(input);

            if(command[0].equals("create")){
                create(command[1]);
            } else if (command[0].equals("r")) {
                read(command[1], command[2]);
            } else if (command[0].equals("p")) {
                print();
            } else if (command[0].equals("s")) {
                search(command[1]);
            } else if (command[0].equals("i")) {
                insert(command[1], command[2] );
            } else if (command[0].equals("d")) {
                delete(command[1]);
            }  else if (command[0].equals("exit")) {
                exit();
            } else if (command[0].equals("h")) {
                help();
            } else if(command[0].equals("size")) {
                System.out.println(dictionary.size());
            } else if(command[0].equals("time")) {
                testTime(command[1], command[2]);
            } else if (command[0].equals("pp")){
                printPretty();
            }
            else {
                System.out.println("Invalid input use the h command for help");

            }

        }
    }

    private static String[] decode(String input){
        String[] command = input.split(" ");
        if (command.length == 1){
            return new String[]{command[0], null, null};
        } else if (command.length == 2) {
            return new String[]{command[0], command[1], null};
        } else {
            return new String[]{command[0], command[1], command[2]};
        }
    }
    private static void create(String arg) {
        if (arg == null) {
            dictionary = new SortedArrayDictionary<>();
            System.out.println("Sorted Array Dictionary created");
            dictstate = 0;
        }
        else if (arg.equals("h")) {
            dictionary = new HashDictionary<>(3);
            System.out.println("HashDictionary erstellt");
            dictstate = 1;
        }
        else if (arg.equals("b")) {
            dictionary = new BinaryTreeDictionary<>();
            System.out.println("BinaryTreeDictionary created");
            dictstate = 2;
        } else if (arg.equals("s")) {
            dictionary = new SortedArrayDictionary<>();
            System.out.println("Sorted Array Dictionary created");
            dictstate = 0;
        } else
            System.out.println("wrong input");
    }

    //long startTime = System.nanoTime();
    //long endTime = System.nanoTime();
    //long timeTaken = (endTime - startTime) / 1000000;

/*
    private static void read(String ammount) {
        int n = 0;
        if (ammount != null) {
            n = Integer.parseInt(ammount);
        }
        JFileChooser chooser = new JFileChooser();
        int returnValue = chooser.showOpenDialog(null);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                long startTime = System.nanoTime();
                BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                if (n == 0) {
                    while(true) {
                        String line = reader.readLine();
                        if (line != null) {
                            String[] lineArr = line.split(" ");
                            dictionary.insert(lineArr[0], lineArr[1]);
                            continue;
                        }
                        break;
                    }
                    long endTime = System.nanoTime();
                    long timeTaken = (endTime - startTime) / 1000000;
                    System.out.println("Datei eingelesen; Dauer: " + timeTaken + "ms");
                } else {
                    for (int i = 0; i < n; i++) {
                        String line = reader.readLine();
                        if (line != null) {
                            String[] lineArr = line.split(" ");
                            dictionary.insert(lineArr[0], lineArr[1]);
                            continue;
                        }
                        break;
                    }
                    long endTime = System.nanoTime();
                    long timeTaken = (endTime - startTime) / 1000000;
                    System.out.println("Datei eingelesen; Dauer: " + timeTaken + "ms");
                }
                //System.out.println(dictionary);
            } catch (FileNotFoundException fe) {
                System.out.println("File not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

 */

    private static void read(String name, String ammount){
        int n = 0;
        int s = 0;
        if (ammount != null) {
            s = Integer.parseInt(ammount);
        }
        if (s == 0){
            try {
                File myObj = new File(name);
                Scanner myReader = new Scanner(myObj);
                long startTime = System.nanoTime();
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] lineArr = data.split(" ");
                    dictionary.insert(lineArr[0], lineArr[1]);
                }
                long endTime = System.nanoTime();
                long timeTaken = (endTime - startTime) / 1000000;
                System.out.println("read with time: " + timeTaken + "ms");
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                exit();
            }
        } else {
            try {
                File myObj = new File(name);
                Scanner myReader = new Scanner(myObj);
                long startTime = System.nanoTime();
                while (myReader.hasNextLine() && n < s) {
                    String data = myReader.nextLine();
                    String[] lineArr = data.split(" ");
                    dictionary.insert(lineArr[0], lineArr[1]);
                    n++;
                }
                long endTime = System.nanoTime();
                long timeTaken = (endTime - startTime) / 1000000;
                System.out.println("read with time: " + timeTaken + "ms");
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                exit();
            }
        }

    }

    private static void testTime(String type, String ammount) {
        int n = 0;
        if (type == null || ammount == null) {
            System.out.println("invalid input");
        } else if (type.equals("s")) {
            int s = Integer.parseInt(ammount);
            try {
                File myObj = new File("german_words.txt");
                Scanner myReader = new Scanner(myObj);
                long startTime = System.nanoTime();
                while (myReader.hasNextLine() && n < s) {
                    String data = myReader.nextLine();
                    dictionary.search(data);
                    n++;
                }
                long endTime = System.nanoTime();
                long timeTaken = (endTime - startTime) / 1000000;
                System.out.println("sucessSearch completed with time: " + timeTaken + "ms");
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                exit();
            }
        } else if (type.equals("f")) {
            int s = Integer.parseInt(ammount);
            try {
                File myObj = new File("english_words.txt");
                Scanner myReader = new Scanner(myObj);
                long startTime = System.nanoTime();
                while (myReader.hasNextLine() && n < s) {
                    String data = myReader.nextLine();
                    dictionary.search(data);
                    n++;
                }
                long endTime = System.nanoTime();
                long timeTaken = (endTime - startTime) / 1000000;
                System.out.println("failSearch completed with time: " + timeTaken + "ms");
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                exit();
            }
        }
    }
    private static void print(){
        if(dictionary == null){
            System.out.println("Dictionary not created");
            return;
        } else if (!dictionary.iterator().hasNext()) {
            System.out.println("Dictionary is empty");
        }
        for(Dictionary.Entry<String, String> e : dictionary){
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
    private static void printPretty(){
        if(dictstate == 2){
            BinaryTreeDictionary<String, String> btd = new BinaryTreeDictionary<>();
            for (Dictionary.Entry<String, String> e : dictionary) {
                btd.insert(e.getKey(), e.getValue());
            }
            btd.prettyPrint();
        } else{
            System.out.println("pp only works on binaryTreeDictionary!");
        }
    }
    private static void search(String word){
        if (word == null) {
            System.out.println("No word given");
            return;
        }
        long startTime = System.nanoTime();
        for (Dictionary.Entry<String, String> e : dictionary) {
            if (e.getKey().equals(word)) {
                System.out.println(word + " -> " + e.getValue());
            }
        }
    }
    private static void insert(String word, String translation){
        if (word == null || translation == null) {
            System.out.println("Use format: [i] [german-word] [english-word]");
            return;
        }
        dictionary.insert(word, translation);
    }
    private static void delete(String word){
        if (word == null) {
            System.out.println("Use format: [d] [german-word]");
            return;
        }
        var t = dictionary.remove(word);
        if(t == null){
            System.out.println("Word not contained in dictionary");
        } else {
            System.out.println("deleted " + t);
        }
    }
    private static void exit(){
        System.exit(0);
    }
    private static void help(){
        System.out.println(ANSI_PURPLE +
                "create [s|h|b] \t\t\t\tcreates a [s] sortedArray, [h] hash or [b] binary dictionary \n" +
                "r [n] \t\t\t\t\t\treads n lines from file\n" +
                "p \t\t\t\t\t\t\tprints dictionary\n" +
                "s [word] \t\t\t\t\tsearches for word\n" +
                "i [word] [translation] \t\tinserts word and translation\n" +
                "d [word] \t\t\t\t\tdeletes word\n" +
                "exit \t\t\t\t\t\texits program\n" +
                "size \t\t\t\t\t\treturns the size of the dictionary\n" +
                "time [f|s] [n] \t\t\t\ttimes [f] fail or [s] success search with n elements\n"+
                "pp \t pritty print (only binary tree)\n"+
                "h \t\t\t\t\t\t\thelp (this menu)\n"
                +ANSI_RESET);
    }
    private static void welcome() {

        System.out.println(ANSI_PURPLE +"██████╗ ██╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗ █████╗ ██████╗ ██╗   ██╗");
        System.out.println("██╔══██╗██║██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║██╔══██╗██╔══██╗╚██╗ ██╔╝");
        System.out.println("██║  ██║██║██║        ██║   ██║██║   ██║██╔██╗ ██║███████║██████╔╝ ╚████╔╝ ");
        System.out.println("██║  ██║██║██║        ██║   ██║██║   ██║██║╚██╗██║██╔══██║██╔══██╗  ╚██╔╝  ");
        System.out.println("██████╔╝██║╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║██║  ██║██║  ██║   ██║   ");
        System.out.println("╚═════╝ ╚═╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   "+ ANSI_RESET);
        System.out.println(ANSI_RED + "Type 'h' for help"+ ANSI_RESET);

    }
}


