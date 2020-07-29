    public static void generateCode(File flowFile, String packagePath, File destDir, File scriptRootFolder) throws IOException {
        InputStream javaSrcIn = generateCode(flowFile, packagePath, scriptRootFolder);
        File outputFolder = new File(destDir, packagePath.replace('.', File.separatorChar));
        String fileName = flowFile.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf(".") + 1) + Consts.FILE_EXTENSION_GROOVY;
        File outputFile = new File(outputFolder, fileName);
        OutputStream javaSrcOut = new FileOutputStream(outputFile);
        IOUtils.copyBufferedStream(javaSrcIn, javaSrcOut);
        javaSrcIn.close();
        javaSrcOut.close();
    }
