package ly.explora;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestExtraireCourriels {

    public static void main(String[] args) throws IOException
    {
// PrintWriter for writing email id
        // to output.txt file
        FileWriter pw = new FileWriter("./resultat/courriels.txt", true);
        Pattern p = Pattern.compile("[a-zA-Z0-9]" + "[a-zA-Z0-9_.]" + "*@[a-zA-Z0-9]" + "+([.][a-zA-Z]+)+");
        BufferedReader br = new BufferedReader(new FileReader(("./resultat/3N5-Prog3/z/index.html")));
        emailFinder("coucouc joris@deguet.org prout kjflskdf jslkfjs dfjsld fkjslkf sdlfs aaa@aaa.org", pw, p);
    }
        public static void emailFinder(String line, FileWriter pw, Pattern p) throws IOException {
            String space = "\n";
            Matcher m = p.matcher(line);
            // If any match
            while (m.find()) {
                // write the email id
                // to output.txt file
                pw.write(m.group() + space);
            }
            pw.flush();
            pw.close();
        }

}
