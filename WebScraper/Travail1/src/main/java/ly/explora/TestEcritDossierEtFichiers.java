package ly.explora;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestEcritDossierEtFichiers {

    public static void main(String[] args) throws IOException
    {
        creerFichier("https://info.cegepmontpetit.ca/3N5-Prog3/phase1.html", "coucou");
        // creer le fichier
    }

    public static void creerFichier(String url, String contenu) throws IOException {
        Pattern p = Pattern.compile("[a-zA-Z0-9]" + "[a-zA-Z0-9_.]" + "*@[a-zA-Z0-9]" + "+([.][a-zA-Z]+)+");
        Matcher matcher = p.matcher(contenu);
        List email = new ArrayList();
        while (matcher.find()) {
            email.add(matcher.group().toLowerCase());
        }
        List<String> emaillist = new ArrayList<String>(email);
        Collections.sort(emaillist);


        String s = new URL(url).getFile();
        File f = new File("./resultat"+s);
        f.getParentFile().mkdirs();
        f.createNewFile();
        BufferedWriter ok = Files.newBufferedWriter(f.toPath());
        for (Object duplicate: email)
        {
           if (contenu.contains(duplicate.toString()))
           {
                contenu = contenu.replace(duplicate.toString(), "andy.ly@pipo.org");
           }
        }
        ok.write(contenu);
        ok.close();

    }
}
