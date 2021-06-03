
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class WordPreprocessing {

    private final HashMap<String, String> wordDictionary = new HashMap<>();

    public WordPreprocessing() throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream("onethousand.txt"));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] split = line.split("[\\s\\-\\s]");
            String firstWord = split[0].toLowerCase();
            String thirdWord = split[2].toLowerCase();
            wordDictionary.put(firstWord, thirdWord);
        }
        sc.close();
    }
    
    public String processSentence(String input) {
        String sentence = input.toLowerCase();
        String[] split = sentence.split(" ");
        String ans = "";
        for (String split1 : split) {
            if (wordDictionary.containsKey(split1)) {
                ans += wordDictionary.get(split1) + " ";
            } else if (wordDictionary.containsValue(split1)) {
                ans += wordDictionary.get(split1) + " ";
            } else {
                ans += split1 + " ";
            }
        }
        return ans;
    }
}
