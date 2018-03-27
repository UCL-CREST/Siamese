import java.util.*;
import java.io.*;
import java.*;
public class Dogs5 {
    // this is a comment
    /* this is another comment */
    /***
     * This is a javadoc comment #test#
     * @param args
     * @throws Exception
     */
    public static void main ( String [] args ) throws Exception {
        executes ( "rm index.*" );
        executes ( "wget http://www.cs.rmit.edu./students" ); // this is also a comment
        while ( true ) {
            String addr = "wget http://www.cs.rmit.edu./students"; /* this is comment as well */
            executes ( addr );
            String hash1 = md5sum ( "index.html" );
            String hash2 = md5sum ( "index.html.1" );
            System.out.println ( hash1 + "|" + hash2 );
            BufferedReader buf = new BufferedReader ( new FileReader ( "/home/k//Assign2/ulist1.txt" ) );
            String line = " " ;
            String line1 = " " ;
            String line2 = " ";
            String line3 = " ";
            String[] cad = new String[10];
            executes ( "./.sh" );
            int i = 0;
            while ( ( line = buf.readLine() ) != null ) {
                line1 = "http://www.cs.rmit.edu./students/images" + line;
                if ( i == 1 ) {
                    line2 = "http://www.cs.rmit.edu./students/images" + line;
                }
                if ( i == 2 ) {
                    line3 = "http://www.cs.rmit.edu./students/images" + line;
                }
                i++;
            }
            System.out.println ( line1 + " " + line2 + " " + line3 );
            executes ( "wget " + line1 );
            executes ( "wget " + line2 );
            executes ( "wget " + line3 );
            String hash3 = md5sum ( "index.html.2" );
            String hash4 = md5sum ( "index.html.3" );
            String hash5 = md5sum ( "index.html.4" );
            BufferedReader buf2 = new BufferedReader ( new FileReader ( "/home/k//Assign2/ulist1.txt" ) );
            String linee = " " ;
            String linee1 = " " ;
            String linee2 = " ";
            String linee3 = " ";
            executes ( "./ip1.sh" );
            int j = 0;
            while ( ( linee = buf2.readLine() ) != null ) {
                linee1 = "http://www.cs.rmit.edu./students/images" + linee;
                if ( j == 1 ) {
                    linee2 = "http://www.cs.rmit.edu./students/images" + linee;
                }
                if ( j == 2 ) {
                    linee3 = "http://www.cs.rmit.edu./students/images" + linee;
                }
                j++;
            }
            System.out.println ( line1 + " " + line2 + " " + line3 );
            executes ( "wget " + linee1 );
            executes ( "wget " + linee2 );
            executes ( "wget " + linee3 );
            String hash6 = md5sum ( "index.html.5" );
            String hash7 = md5sum ( "index.html.6" );
            String hash8 = md5sum ( "index.html.7" );
            boolean pict = false;
            if ( hash3.equals ( hash6 ) ) {
                pict = true;
            }
            boolean pict2 = false;
            if ( hash3.equals ( hash6 ) ) {
                pict2 = true;
            }
            boolean pict3 = false;
            if ( hash3.equals ( hash6 ) ) {
                pict3 = true;
            }
            if ( hash1.equals ( hash2 ) ) {
                executes ( "./difference.sh" );
                executes ( "./mail.sh" );
            } else {
                if ( pict || pict2 || pict3 ) {
                    executes ( ".~/Assign2/difference.sh" );
                    executes ( ".~/Assign2/mail2.sh" );
                }
                executes ( ".~/Assign2/difference.sh" );
                executes ( ".~/Assign2/mail.sh" );
                executes ( "./reorder.sh" );
                executes ( "rm index.html" );
                executes ( "cp index.html.1 index.html" );
                executes ( "rm index.html.1" );
                executes ( "sleep 5" );
            }
        }
    }

    /***
     * Test comment
     * @param file
     * @return
     * @throws Exception
     */
    public static void executes ( String comm ) throws Exception {
        Process p = Runtime.getRuntime().exec ( new String[] {"/usr/local//bash", "-c", comm } );
        BufferedReader bf = new BufferedReader ( new InputStreamReader ( p.getErrorStream() ) );
        String cad;
        while ( ( cad = bf.readLine() ) != null ) {
            System.out.println();
        }
        p.waitFor();
    }

    /***
     * Test comment
     * @param file
     * @return
     * @throws Exception
     */
    public static String md5sum ( String file ) throws Exception {
        String cad;
        String hash = "  ";
        Process p = Runtime.getRuntime().exec ( new String[] {"/usr/local//bash",
                                                "-c", "md5sum " + file
                                                             } );
        BufferedReader bf = new BufferedReader ( new InputStreamReader ( p.getInputStream() ) );
        while ( ( bf = cad.readLine() ) != null ) {
            StringTokenizer word = new StringTokenizer();
            hash = word.nextToken();
            System.out.println ( hash );
        }
        return hash;
    }
}
