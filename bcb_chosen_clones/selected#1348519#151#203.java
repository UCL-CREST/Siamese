    public static void test() {
        addToCounter("computeracces.html");
        try {
            JFrame jfr = new JFrame("Webconnect");
            URL url = new URL("http://apps.sourceforge.net/piwik/lyricscatcher/piwik.php?idsite=1");
            JEditorPane jep = new JEditorPane();
            jfr.add(jep);
            String urlstr = "http://apps.sourceforge.net/piwik/lyricscatcher/piwik.php?url=http%3a%2f%2flyricscatcher.sourceforge.net%2fpiwik.php&action_name=&idsite=1&res=1440x900&h=";
            Calendar cal = Calendar.getInstance();
            urlstr += cal.get(Calendar.HOUR_OF_DAY);
            urlstr += "&m=";
            urlstr += cal.get(Calendar.MINUTE);
            urlstr += "&s=";
            urlstr += cal.get(Calendar.SECOND);
            urlstr += "&fla=1&dir=1&qt=1&realp=1&pdf=1&wma=1&java=1&cookie=0&title=JAVAACCESS&urlref=http%3a%2f%2flyricscatcher.sourceforge.net%2fcomputeraccespage.html";
            System.out.println(urlstr);
            URL nurl = new URL(urlstr);
            InputStream ist = nurl.openStream();
            InputStreamReader isr = new InputStreamReader(ist);
            BufferedReader in = new BufferedReader(isr);
            String line = "";
            String inputline = "";
            while ((inputline = in.readLine()) != null) {
                line += inputline + "\n";
            }
            System.out.println("finished: length of correct url=" + line.length());
            URL myurl = new URL(urlstr);
            URLConnection urlc = myurl.openConnection();
            urlc.getContent();
            System.out.println(urlc.getLastModified());
            System.out.println(urlc.getPermission());
            System.out.println(urlc.getRequestProperties());
            System.out.println(urlc.getContentEncoding());
            System.out.println(urlc.getContentLength());
            urlc.connect();
            InputStream dist = myurl.openStream();
            while (ist.available() >= 0) {
                ist.read();
            }
            ist.close();
            Document d = jep.getEditorKitForContentType("html").createDefaultDocument();
            d.getDefaultRootElement();
            jep.setContentType("text/html");
            jep.setText("<html><img src=\"http://apps.sourceforge.net/piwik/lyricscatcher/piwik.php?idsite=1\" alt=\"there's a problem...\"/><img src=\"" + urlstr + "\" alt=\"Another problem\" style=\"border:0\" /></html>");
            jfr.setLocationByPlatform(true);
            jfr.setSize(100, 100);
            jfr.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
