    public static void copyFile(File inputFile, File targetDir, String newName) throws IOException {
        InputStream in = new FileInputStream(inputFile);
        OutputStream out = new FileOutputStream(new File(targetDir, newName));
        IOUtils.copy(in, out);
    }
