    public XFDLDocument(String inputFile) throws IOException, ParserConfigurationException, SAXException {
        fileLocation = inputFile;
        try {
            File f = new File(inputFile);
            if (!f.exists()) {
                throw new IOException("Specified File could not be found!");
            }
            FileInputStream fis = new FileInputStream(inputFile);
            fis.skip(FILE_HEADER_BLOCK.length());
            Base64.InputStream bis = new Base64.InputStream(fis, Base64.DECODE);
            GZIPInputStream gis = new GZIPInputStream(bis);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(gis);
            gis.close();
            bis.close();
            fis.close();
        } catch (ParserConfigurationException pce) {
            throw new ParserConfigurationException("Error parsing XFDL from file.");
        } catch (SAXException saxe) {
            throw new SAXException("Error parsing XFDL into XML Document.");
        }
    }
