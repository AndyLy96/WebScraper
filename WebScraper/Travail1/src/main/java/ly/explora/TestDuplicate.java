package ly.explora;

public class TestDuplicate {


    public static void main( String[] args ) {
        System.out.println(contientDesDuplicates("pipo popi pipo"));
        System.out.println(contientDesDuplicates("pipo popi"));

    }

    public static boolean contientDesDuplicates(String source) {
        // decomposer la String en liste de String
        //System.out.println(source);
        String[] splited = source.split(" ");

        //

        for (int i = 0; i < splited.length; i++) {
            for (int j = i + 1 ; j < splited.length; j++) {
                if (splited[i].equals(splited[j])) { // got the duplicate element
                     //System.out.println("Coucou");
                     return true;
                }
            }
        }
        return false;

    }
}
