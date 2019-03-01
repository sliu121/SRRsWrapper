package GetHTMLPage;

/*
 * GetHTMLPage.java
 */
import java.net.*;
import java.io.*;

/**
 * Retrieve the content of the HTML page.
 *
 * @author Yiyao Lu, Can Lin
 * last updated on April 29, 2012
 */
public class GetHTMLPage {

    /** Creates a new instance of GetHTMLPage */
    public GetHTMLPage()
    {
    }

    private static InputStream getRemoteConnection(String docURL)
    {
        try
        {
            URL url = new URL(docURL);
            HttpURLConnection uc = (HttpURLConnection)url.openConnection();

            return uc.getInputStream();
        }
        catch (Exception e)
        {
            System.out.println("\tGetRemotePage Exception: " + e);
            return null;
        }
    }

    /**
     * Get the HTML page content specified by docURL from remote server.
     * @param docURL The URL of the document to be retrieved.
     * @return The string containing processed webpage.
     */
    public static String getRemotePageContent(String docURL)
    {
        InputStream stream = getRemoteConnection(docURL);
        if(stream == null)
        {
            System.out.println("\tError: NULL INPUT STREAM!");
            return null;
        }

        String rawPage;
        try
        {
            rawPage = "";
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            // read from input stream line by line
            String inputLine;
            while((inputLine = r.readLine()) != null)
                rawPage += inputLine + "\n";
            r.close();

            return rawPage;
        }
        catch(IOException ie)
        {
            System.out.println("\tRead InputStream into String Exception: " + ie);
            return null;
        }
    }

    public static void main(String[] args)
    {
        String s = GetHTMLPage.getRemotePageContent("https://www.bing.com/search?q=who%20are%20you&cbir=sbi&imageBin=&qs=n&form=QBRE&sp=-1&pq=who%20are%20you&sc=9-11&sk=&cvid=8C93A19BDEB8435494CF19D5D7C6E6B3");

        if(s==null)
            System.out.println("\tError: No Page Content!");
        else
            try(PrintWriter out = new PrintWriter("test.html")){
                out.println(s);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(s);
    }
}
