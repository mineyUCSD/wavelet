import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;



class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    
    static ArrayList<String> reservoir = new ArrayList<String>();

    String reservoirToString(ArrayList<String> reservoir){

        String enumerationOfStrings = "";
            for(String string : reservoir){
                enumerationOfStrings += string + "\n";
            }
            return enumerationOfStrings;

    }

    String obtainSearchedString(ArrayList<String> reservoir, String searched){
        for(String string : reservoir){
            if(string.equals(searched)){
                return string;
            }
        }
        return "";
    }

    int num = 0;

    public String handleRequest(URI url) {

        if (url.getPath().equals("/")) {

            return "List of Strings:\n" + reservoirToString(reservoir);
            
        } else if (url.getPath().equals("/clear")) {
            reservoir.clear();
            return String.format("Cleared List!\n") + "List of Strings:\n" + reservoirToString(reservoir);
        } else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("String")) {
                    reservoir.add(parameters[1]);
                    return String.format("Added %s! The reservoir is now contains %d strings", parameters[1], reservoir.size())
                    + "\nList of Strings:\n" + reservoirToString(reservoir);
                }
            }
            if(url.getPath().contains("/search")){
                String[] parameters = url.getQuery().split("=");
                if(parameters[0].equals("String")){
                    if(reservoir.contains(parameters[1])){
                        return "Searched Word Found: " + obtainSearchedString(reservoir, parameters[1]);
                    }
                    return "Word Not Found";
                }
            }
            return "404 Not Found!";
        }
    }
}

public class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    } 
}
