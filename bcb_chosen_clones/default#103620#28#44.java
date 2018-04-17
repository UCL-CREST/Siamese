    public XMLConfigReader() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            String xmlQueryString = new String("<CommandList>\n<GetRegisteredUser />\n</CommandList>");
            InputStream in = new ByteArrayInputStream(xmlQueryString.getBytes("UTF-8"));
            this.xmlQuery = builder.parse(in);
            this.xmlConfig = builder.parse(new File("trunk/engine/files/xml.conf"));
            this.xmlResponse += "\n<CommandList>\n";
            this.parse(this.xmlQuery.getElementsByTagName("CommandList"));
            this.xmlResponse += "</CommandList>";
            System.out.println(this.xmlResponse);
        } catch (Exception e) {
            System.out.println("Error...");
            e.printStackTrace();
        }
    }
