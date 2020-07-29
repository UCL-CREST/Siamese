    public void loadSourceCode() {
        int length = MAX_SOURCE_LENGTH;
        try {
            File file = new File(filename);
            length = (int) file.length();
        } catch (SecurityException ex) {
        }
        char[] buff = new char[length];
        InputStream is;
        InputStreamReader isr;
        CodeViewer cv = new CodeViewer();
        URL url;
        try {
            url = getClass().getResource(filename);
            is = url.openStream();
            isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            sourceCode = new String("<html><pre>");
            String line = reader.readLine();
            while (line != null) {
                sourceCode += cv.syntaxHighlight(line) + " \n ";
                line = reader.readLine();
            }
            sourceCode += "</pre></html>";
        } catch (Exception ex) {
            sourceCode = getString("SourceCode.error");
        }
    }
