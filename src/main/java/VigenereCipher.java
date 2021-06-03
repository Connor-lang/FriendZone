
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VigenereCipher {

    private static final String CHARS = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    public VigenereCipher() {
    }

    protected List<Integer> getCharPos(String s) {
        List<Integer> charsAtPos = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            charsAtPos.add(CHARS.indexOf(s.charAt(i)));
        }
        return charsAtPos;
    }

    protected String generateKey(String s) {
        java.util.Random rand = new java.util.Random();
        String key = "";
        int keyLength = rand.nextInt(s.length() - 1) + 2;
        for (int i = 0; i < keyLength; i++) {
            key += (char) (rand.nextInt(27) + 'A');
        }
        String keyGenerated = "";
        while (keyGenerated.length() < s.length()) {
            for (int i = 0; i < key.length(); i++) {
                keyGenerated += key.charAt(i);
                if (keyGenerated.length() == s.length()) {
                    break;
                }
            }
        }
        return keyGenerated;
    }

    protected String encrypt(String text, String key) {
        List<Integer> textCharAtPos = getCharPos(text);
        List<Integer> keyCharAtPos = getCharPos(key);

        int textLength = text.length();
        int keyLength = key.length();
        String cipher = "";
        int letter;
        for (int i = 0; i < textLength; i++) {
            letter = (textCharAtPos.get(i) + keyCharAtPos.get(i % keyLength));
            letter %= CHARS.length();
            cipher += CHARS.charAt(letter);
        }
        return cipher;
    }

    protected String decrypt(String cipher, String key) {
        List<Integer> cipherCharAtPos = getCharPos(cipher);
        List<Integer> keyCharAtPos = getCharPos(key);

        int cipherLength = cipher.length();
        int keyLength = key.length();
        String plaintext = "";
        int letter;

        for (int i = 0; i < cipherLength; i++) {
            letter = (cipherCharAtPos.get(i) - keyCharAtPos.get(i % keyLength));
            letter += CHARS.length();
            letter %= CHARS.length();
            plaintext += CHARS.charAt(letter);
        }
        return plaintext;
    }
}

class Main {

    public static void main(String[] args) {
        VigenereCipher v = new VigenereCipher();
        String text, key;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter text: ");
        text = sc.nextLine();
        String keyGenerated = v.generateKey(text);
        System.out.println("Key generated: " + keyGenerated);
        String cipher = v.encrypt(text, keyGenerated);
        String plaintext = v.decrypt(cipher, keyGenerated);
        System.out.println("Cipher: " + cipher);
        System.out.println("Plaintext: " + plaintext);
    }
}
