import Adapter.MainController;

public class Main {
    public static void main(String[] args) {

        if (args.length != 1 || (!args[0].equals("0") && !args[0].equals("1"))) {
            System.out.println("Please enter an argument of 0 to run the program for the first time, or an argument " +
                    "of 1 to run it subsequent times.");
        } else {
            MainController main = new MainController(args[0]);
            main.mainRun();
        }
    }
}
