    public void startElement(String uri, String tag, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        wabclient.Attributes prop = new wabclient.Attributes(attributes);
        try {
            if (tag.equals("app")) {
                if (prop == null) {
                    System.err.println("app without properties");
                    return;
                }
                String appname = prop.getValue("name", "");
                String lookandfeel = prop.getValue("lookandfeel", "");
                global.setAppName(appname);
                if (lookandfeel.length() > 0) {
                    if (lookandfeel.equalsIgnoreCase("Windows")) lookandfeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; else if (lookandfeel.equalsIgnoreCase("Motif")) lookandfeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel"; else if (lookandfeel.equalsIgnoreCase("Mac")) lookandfeel = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
                    UIManager.setLookAndFeel(lookandfeel);
                }
            } else if (tag.equals("script")) {
                WABClient c = (WABClient) global;
                c.beginScript();
                String url = prop.getValue("src");
                if (url.length() > 0) {
                    try {
                        BufferedReader r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                        String buffer;
                        while (true) {
                            buffer = r.readLine();
                            if (buffer == null) break;
                            c.script += buffer + "\n";
                        }
                        r.close();
                        c.endScript();
                    } catch (IOException ioe) {
                        System.err.println("[IOError] " + ioe.getMessage());
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
