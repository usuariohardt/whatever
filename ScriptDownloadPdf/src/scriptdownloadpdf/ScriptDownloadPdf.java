
package scriptdownloadpdf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author daniel
 */
public class ScriptDownloadPdf {
    
    public static String DIRETORIO_DOWNLOAD = "C:\\DownloadPDF\\";
    public static String URL_DOWNLOAD = "http://www.portalalba.org/biblioteca/";
    
    public static void main(String[] args) {
        new ScriptDownloadPdf().iniciarDownload();
    }
    //--------------------------------------------------------------------------
    private void iniciarDownload(){
        ThreadDownload threadDownload = new ThreadDownload();
        Thread thread = new Thread(threadDownload);
        thread.start();
    }
    //--------------------------------------------------------------------------
    private void baixarLivros() throws IOException{
        File dir = new File(DIRETORIO_DOWNLOAD);
        if(!dir.exists())
            dir.mkdir();
        Document doc = Jsoup.connect(URL_DOWNLOAD).get();
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String stringlink = link.attr("abs:href");
            if(stringlink.toLowerCase().endsWith(".pdf")){
                System.out.println("Baixando " + link.text());
                saveUrl(DIRETORIO_DOWNLOAD + link.text(), stringlink);
            }
        }
    }
    //--------------------------------------------------------------------------
    public void saveUrl(final String filename, final String urlString)
        throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);
            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
    //--------------------------------------------------------------------------
    protected class ThreadDownload extends Thread {
        @Override
        public void run() {
            try {
                baixarLivros();
            } catch (IOException ex) {
                Logger.getLogger(ScriptDownloadPdf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //--------------------------------------------------------------------------
}
