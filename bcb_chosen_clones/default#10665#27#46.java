    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream("moduls/ESBClient/test/files/firma.xml");
        XMLStreamReader reader = StAXUtils.createXMLStreamReader(in);
        StAXOMBuilder builder = new StAXOMBuilder(reader);
        OMElement documentElement = builder.getDocumentElement();
        AXIOMXPath xpath = null;
        OMElement node = null;
        xpath = new AXIOMXPath("/AFIRMA/CONTENT");
        node = (OMElement) xpath.selectSingleNode(documentElement);
        String hashB64Xades = node.getText();
        System.out.println(" ------------ HASH B64 EN XADES: " + hashB64Xades);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
        copy(new FileInputStream("moduls/ESBClient/test/files/datos.txt"), bos);
        byte[] datosDoc = bos.toByteArray();
        MessageDigest dig = MessageDigest.getInstance("SHA1");
        byte[] hash = dig.digest(datosDoc);
        String hashB64Doc = new String(org.apache.commons.codec.binary.Base64.encodeBase64(hash));
        System.out.println(" ------------ HASH B64 DOC:      " + hashB64Doc);
        System.out.println(" ------------ IGUALES?: " + hashB64Doc.equals(hashB64Xades));
    }
