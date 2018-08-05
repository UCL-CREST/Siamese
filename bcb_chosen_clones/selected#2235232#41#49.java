    public File createFileFromClasspathResource(String resourceUrl) throws IOException {
        File fichierTest = File.createTempFile("xmlFieldTestFile", "");
        FileOutputStream fos = new FileOutputStream(fichierTest);
        InputStream is = XmlFieldDomSelectorTest.class.getResourceAsStream(resourceUrl);
        IOUtils.copy(is, fos);
        is.close();
        fos.close();
        return fichierTest;
    }
