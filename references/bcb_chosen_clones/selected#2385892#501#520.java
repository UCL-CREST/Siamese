    private void startScript(wabclient.Attributes prop) throws SAXException {
        dialog.beginScript();
        String url = prop.getValue("src");
        if (url.length() > 0) {
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                String buffer;
                while (true) {
                    buffer = r.readLine();
                    if (buffer == null) break;
                    dialog.script += buffer + "\n";
                }
                r.close();
                dialog.endScript();
            } catch (IOException ioe) {
                System.err.println("[IOError] " + ioe.getMessage());
                System.exit(0);
            }
        }
    }
