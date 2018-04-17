    public static String readURL(String urlStr, boolean debug) {
        if (debug) System.out.print("    trying: " + urlStr + "\n");
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (java.net.MalformedURLException e) {
            System.out.print("test failed: using URL: ");
            System.out.print(e.getMessage());
            System.out.print('\n');
            return null;
        }
        HttpURLConnection huc = null;
        try {
            huc = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.out.print("test failed: using URL: ");
            System.out.print(e.getMessage());
            System.out.print('\n');
            return null;
        }
        String contentType = huc.getContentType();
        if (contentType == null || contentType.indexOf("text/xml") < 0) {
            System.out.print("*** Warning ***  Content-Type not set to text/xml");
            System.out.print('\n');
            System.out.print("    Content-type: ");
            System.out.print(contentType);
            System.out.print('\n');
        }
        InputStream urlStream = null;
        try {
            urlStream = huc.getInputStream();
        } catch (java.io.IOException e) {
            System.out.print("test failed: opening URL: ");
            System.out.print(e.getMessage());
            System.out.print('\n');
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(urlStream));
        boolean xml = true;
        String href = null, inputLine = null;
        StringBuffer content = new StringBuffer(), stylesheet = null;
        Transformer transformer = null;
        try {
            inputLine = in.readLine();
        } catch (java.io.IOException e) {
            System.out.print("test failed: reading first line of response: ");
            System.out.print(e.getMessage());
            System.out.print('\n');
            return null;
        }
        if (inputLine == null) {
            System.out.print("test failed: No input read from URL");
            System.out.print('\n');
            return null;
        }
        if (!inputLine.startsWith("<?xml ")) {
            xml = false;
            content.append(inputLine);
        }
        if (xml) {
            int offset = inputLine.indexOf('>');
            if (offset + 2 < inputLine.length()) {
                inputLine = inputLine.substring(offset + 1);
                offset = inputLine.indexOf('<');
                if (offset > 0) inputLine = inputLine.substring(offset);
            } else try {
                inputLine = in.readLine();
            } catch (java.io.IOException e) {
                System.out.print("test failed: reading response: ");
                System.out.print(e.getMessage());
                System.out.print('\n');
                return null;
            }
            content.append(inputLine);
        }
        try {
            while ((inputLine = in.readLine()) != null) content.append(inputLine);
        } catch (java.io.IOException e) {
            System.out.print("test failed: reading response: ");
            System.out.print(e.getMessage());
            System.out.print('\n');
            return null;
        }
        String contentStr = content.toString();
        if (transformer != null) {
            StreamSource streamXMLRecord = new StreamSource(new StringReader(contentStr));
            StringWriter xmlRecordWriter = new StringWriter();
            try {
                transformer.transform(streamXMLRecord, new StreamResult(xmlRecordWriter));
                System.out.print("        successfully applied stylesheet '");
                System.out.print(href);
                System.out.print("'");
                System.out.print('\n');
            } catch (javax.xml.transform.TransformerException e) {
                System.out.print("unable to apply stylesheet '");
                System.out.print(href);
                System.out.print("'to response: ");
                System.out.print(e.getMessage());
                System.out.print('\n');
                e.printStackTrace();
            }
        }
        return contentStr;
    }
