    SXCParser() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document doc = null;
            InputStream istr = new ZipFile("essai1.sxc").getInputStream(new ZipEntry("content.xml"));
            doc = db.parse(istr, "file:///home/raph/DOM/SXC/nulldtd/office.dtd");
            FileWriter f1 = new FileWriter("save.xml");
            new org.apache.soap.util.xml.DOM2Writer().serializeAsXML(doc, f1);
            f1.close();
            ZipOutputStream zostr = new ZipOutputStream(new FileOutputStream("essai2.sxc"));
            zostr.putNextEntry(new ZipEntry("content.xml"));
            OutputStreamWriter osw = new OutputStreamWriter(zostr);
            new org.apache.soap.util.xml.DOM2Writer().serializeAsXML(doc, osw);
            osw.close();
            DisplayNotes(doc);
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        } catch (org.xml.sax.SAXException se) {
            System.err.println(se.getMessage());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }
    }
