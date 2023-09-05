package ly.explora;

import java.io.*;
import java.text.Collator;
import java.util.*;

public class TestOrdreAlphabetique {
    public static void main(String[] args) throws IOException{

    }

    public static void ordreCourriels(String filename) throws IOException {
        FileReader fileReader = new FileReader("./resultat/courriels.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        //List<String> lines = new ArrayList<String>();
        Set<String> lines = new HashSet<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (!lines.equals(line))
            lines.add(line);
        }

        List<String> list = new ArrayList<String>(lines);
        bufferedReader.close();
        Collections.sort(list, Collator.getInstance());
        FileWriter filewriter = new FileWriter("./resultat/courriels.txt");
        for(String str: list) {
            filewriter.write(str + "\r\n");
        }
        filewriter.close();
    }

}
