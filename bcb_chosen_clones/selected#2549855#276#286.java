    public static Document validateXml(File messageFile, URL inputUrl, String[] catalogs) throws IOException, ParserConfigurationException, Exception, SAXException, FileNotFoundException {
        InputSource source = new InputSource(inputUrl.openStream());
        Document logDoc = DomUtil.getNewDom();
        XMLReader reader = SaxUtil.getXMLFormatLoggingXMLReader(log, logDoc, true, catalogs);
        reader.parse(source);
        InputStream logStream = DomUtil.serializeToInputStream(logDoc, "utf-8");
        System.out.println("Creating message file \"" + messageFile.getAbsolutePath() + "\"...");
        OutputStream fos = new FileOutputStream(messageFile);
        IOUtils.copy(logStream, fos);
        return logDoc;
    }
