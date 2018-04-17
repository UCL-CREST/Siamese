    private static void copy(String srcFilename, String dstFilename, boolean override) throws IOException, XPathFactoryConfigurationException, SAXException, ParserConfigurationException, XPathExpressionException {
        File fileToCopy = new File(rootDir + "test-output/" + srcFilename);
        if (fileToCopy.exists()) {
            File newFile = new File(rootDir + "test-output/" + dstFilename);
            if (!newFile.exists() || override) {
                try {
                    FileChannel srcChannel = new FileInputStream(rootDir + "test-output/" + srcFilename).getChannel();
                    FileChannel dstChannel = new FileOutputStream(rootDir + "test-output/" + dstFilename).getChannel();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    srcChannel.close();
                    dstChannel.close();
                } catch (IOException e) {
                }
            }
        }
    }
