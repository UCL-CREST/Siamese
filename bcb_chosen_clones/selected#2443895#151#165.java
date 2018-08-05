    private String readHtmlFile(String htmlFileName) {
        StringBuffer buffer = new StringBuffer();
        java.net.URL url = getClass().getClassLoader().getResource("freestyleLearning/homeCore/help/" + htmlFileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String string = " ";
            while (string != null) {
                string = reader.readLine();
                if (string != null) buffer.append(string);
            }
        } catch (Exception exc) {
            System.out.println(exc);
        }
        return new String(buffer);
    }
