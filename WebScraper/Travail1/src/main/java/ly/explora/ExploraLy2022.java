package ly.explora;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ly.explora.TestDuplicate.contientDesDuplicates;
import static ly.explora.TestEcritDossierEtFichiers.creerFichier;
import static ly.explora.TestExtraireCourriels.emailFinder;
import static ly.explora.TestOrdreAlphabetique.ordreCourriels;


/**
 * Hello world!
 *
 */
public class ExploraLy2022 {

    public static void main(String[] args) throws IOException
    {

        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";

        if (args.length != 2)
        {
            System.err.println("Merci de nous fournir 2 arguments :\n" +
                    "l'URL de la page de départ, la liste de mots clés à ignorer séparés par des espaces entre guillemets.\n" +
                    "Par exemple : https://info.cegepmontpetit.ca/3N5-Prog3/z/index.html et allo");
            return;
        }

        if (!estValide(args[0]))
        {
            System.err.println("Nous avons rencontré un problème avec l'URL fournie : \n" + "htps:/info.cegepmontpetit.ca/" + "\nMerci de fournir une URL bien formée");
            return;
        }

        if (contientDesDuplicates(args[1]))
        {
            System.err.println("Mots clés fournis incorrects: " + args[1]  + "." +
                    "\nMerci de ne pas avoir de répétitions dans les mots clés.");
            return;
        }

        String ancienLien = args[0];
        String nouveauLien = "";
        List listDeLien = new ArrayList();

        Document doc1 = Jsoup.connect(args[0]).get();
        if(args.length == 2 && !contientDesDuplicates(args[1]) && estValide(args[0]))
        {
            System.out.println("Les arguments sont corrects, nous commençons l'exploration de " + args[0]);
        }

        while (true)
        {

            Document doc = Jsoup.connect(ancienLien).get();
            Elements nbLien = doc.select("a[href]");
            creerFichier(ancienLien, doc.html());

            FileWriter pw = new FileWriter("./resultat/courriels.txt", true);
            Pattern p = Pattern.compile("[a-zA-Z0-9]" + "[a-zA-Z0-9_.]" + "*@[a-zA-Z0-9]" + "+([.][a-zA-Z]+)+");
            String s = new URL(ancienLien).getFile();
            File f = new File("./resultat"+s);
            //BufferedReader br = new BufferedReader(new FileReader((f)));
            emailFinder(doc.html(), pw, p);
            ordreCourriels("./resultat/courriels.txt");


            String[] motClés = args[1].split(" ");

            if (nbLien.size() != 0)
            {
                System.out.println("Titre: " + doc.title() + "          URL : " + ancienLien + "    liens: " + nbLien.size());
                nouveauLien = "";

                for (Element lien : nbLien)
                {
                    String candidatAbsolu = lien.attr("abs:href");
                    if (motClés.length == 2)
                    {
                        if (!memePage(ancienLien,candidatAbsolu) && estValide(candidatAbsolu) && (!listDeLien.contains(candidatAbsolu)) && !candidatAbsolu.contains(motClés[0]) && !candidatAbsolu.contains(motClés[1]) )
                        {
                            if (!estExistant(candidatAbsolu))
                            {
                                System.out.println(ANSI_RED + "URL ignorée : "+ candidatAbsolu + ANSI_RESET);
                                continue;
                            }
                            nouveauLien = candidatAbsolu;
                            break;
                        }
                    }
                    else
                    {
                        if (!candidatAbsolu.contains("#") && estValide(candidatAbsolu) && (!listDeLien.contains(candidatAbsolu)) && !candidatAbsolu.contains(motClés[0]))
                        {
                            if (!estExistant(candidatAbsolu))
                            {
                                System.out.println(ANSI_RED + "URL ignorée : "+ candidatAbsolu + ANSI_RESET);
                                continue;
                            }
                            nouveauLien = candidatAbsolu;
                            break;
                        }
                    }
                    if (listDeLien.contains(candidatAbsolu))
                    {
                        System.out.println("L'exploration s'est arrêtée car nous avons rencontré une URL déjà explorée " + candidatAbsolu);
                        return;
                    }
                    if(!estValide(candidatAbsolu))
                    {
                        System.out.println(ANSI_RED + "URL ignorée : "+ candidatAbsolu + ANSI_RESET);
                    }
                }
            }
            else
            {
                System.out.println("Titre: " + doc.title() + "          URL : " + ancienLien);
                System.out.println("L'exploration s'est arrêtée, la page " + ancienLien + " ne contient aucun lien valide.");
                return;
            }
            listDeLien.add(ancienLien);
            ancienLien = nouveauLien;
            nouveauLien = "";
        }

    }

    public  static boolean memePage(String url1, String url2)
    {
        try {
            URL url_1 = new URL(url1);
            URL url_2 = new URL(url2);
            return url_1.sameFile(url_2);
        }catch(MalformedURLException e)
        {
            return false;
        }
    }

    public static boolean estValide(String url)
    {
        try
        {
            new URL(url).toURI();
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public static boolean estExistant(String url)
    {
        try
        {
            Document doc = Jsoup.connect(url).get();
            return true;
        } catch (IOException e)
        {
            return false;
        }

    }

    //Methode pour sauvegarder les fichiers et copier les courriels
    public static ArrayList<String> getEmailAddressesInString(String text)
    {
        ArrayList<String> emails = new ArrayList<String >();

        Matcher matcher = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").matcher(text);
        while (matcher.find())
        {
            emails.add(matcher.group());
        }
        return emails;
    }

    public static void getEmailsFromFile() throws IOException
    {
        // email ids written to myOutputFile.txt file
        PrintWriter p = new PrintWriter("myOutputFile.txt");
        // Regular expression for email id
        Pattern pat=Pattern.compile( "[a-zA-Z0-9]" + "[a-zA-Z0-9_.]" + "*@[a-zA-Z0-9]" + "+([.][a-zA-Z]+)+");
        BufferedReader b = new BufferedReader(new FileReader("myInputFile.txt"));
        //reading myInputFile.txt file
        String l = b.readLine();
        while (l != null)
        {
            Matcher mat = pat.matcher(l);
            while (mat.find())
            {
                p.println(mat.group());
            }
            l = b.readLine();
        }
        p.flush();
    }


}




