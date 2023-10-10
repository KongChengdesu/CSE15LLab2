import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    int num = 0;

    ArrayList<String> savedEntries = new ArrayList<String>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return this.listAll();
        } else if (url.getPath().equals("/add")) {
            if(url.getQuery() == null){
                return "404 Not Found!";
            }
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                this.add(parameters[1]);
                return String.format("Entry added!");
            }
        } else {
            if (url.getPath().contains("/search")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    num += Integer.parseInt(parameters[1]);
                    return String.format("Number increased by %s! It's now %d", parameters[1], num);
                }
            }
            return "404 Not Found!";
        }
    }

    public void add(String entry) {
        savedEntries.add(entry);
    }

    public String search(String query) {
        String result = "";
        for (int i = 0; i < savedEntries.size(); i++) {
            if (savedEntries.get(i)contains(query)) {
                result += savedEntries.get(i) + "\n";
            }
        }
        return result;
    }

    public String listAll(){
        String result = "";
        for (int i = 0; i < savedEntries.size(); i++) {
            result += savedEntries.get(i) + "\n";
        }
        return result;
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
