    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filePath = "/Users/francisbaril/Downloads/test-1.pdf";
        String testFilePath = "/Users/francisbaril/Desktop/testpdfbox/test.pdf";
        File file = new File(filePath);
        final File testFile = new File(testFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
        IOUtils.copy(new FileInputStream(file), new FileOutputStream(testFile));
        System.out.println(getLongProperty(new FileInputStream(testFile), PROPRIETE_ID_IGID));
    }
