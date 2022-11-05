package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    static final String[] options = {"add", "remove", "list", "exit"};
    static final String file = "src/main/java/pl/coderslab/tasks.csv";
    static String[][] tasks;

    public static void main(String[] args) {
        tasks = dataFromCSV(file);
        selectOption(options);

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String choise = scanner.nextLine();

            switch (choise){
                case "exit":
                    save(tasks, file);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                case "add":
                     add(scanner);
                     break;
                case "remove":
                     remove(tasks, chooseNumber(scanner));
                     break;
                case "list":
                     printTasks(tasks);
                    break;
                default:
                    System.out.println("Please select a correct option.");
               }

            selectOption(options);
        }
    }


     public static void selectOption (String[] arr){
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);

        for(String options : arr){
            System.out.println(options);
        }
    }
    public static String[][] dataFromCSV (String fileName){
        String[][] arr = null;

        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            System.err.println("File not exists");
            System.exit(0);
        }
        try{
            List<String> text = Files.readAllLines(path);
            arr = new String[text.size()][text.get(0).split(",").length];

            for(int i = 0; i < text.size(); i++){
                String[] split = text.get(i).split(",");
                for(int j = 0; j < split.length; j++){
                    arr[i][j] = split[j];
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Coś nie działa");
        }

        return arr;
    }
    public static void printTasks (String[][] array){
        for(int i = 0; i < array.length; i++){
            System.out.print(i + " : ");
            for(int j = 0; j < array[i].length; j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void add (Scanner scanner){
        scanner = new Scanner(System.in);

        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String date = scanner.nextLine();
        System.out.println("Is your task important: true/false");
        String isImportant = scanner.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = date;
        tasks[tasks.length - 1][2] = isImportant;
    }
    public static boolean isNumber (String number){
        if(NumberUtils.isParsable(number)){
            return Integer.parseInt(number) >= 0;
        }
        return false;
    }

    public static int chooseNumber (Scanner scanner){
        scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        String number = scanner.nextLine();
        while (!isNumber(number)){
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scanner.nextLine();
        }

        return Integer.parseInt(number);
    }

    public static void remove (String[][] arr, int x){
        try{
            if(x < arr.length){
                tasks = ArrayUtils.remove(arr, x);
            }
        } catch(ArrayIndexOutOfBoundsException e){
            System.err.println("Element not exist in tab");
        }
    }

    public static void save (String[][] arr, String file){
        Path path = Paths.get(file);

        String [] tab = new String[arr.length];
        for(int i = 0; i < arr.length; i++){
            tab[i] = String.join(",", arr[i]);
        }

        try{
            Files.write(path, Arrays.asList(tab));
        } catch(IOException e){
            System.err.println("Problem z napisem");
            e.printStackTrace();
        }
    }
}
