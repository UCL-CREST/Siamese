    public static void compressFiles(String newFileName, String[] inputFilePaths, String destDir) throws Exception {
        String zipPathName = destDir + "/" + newFileName + ".zip";
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPathName));
        for (int i = 0; i < inputFilePaths.length; i++) {
            File inputFile = new File(inputFilePaths[i]);
            if (inputFile.exists() && inputFile.isFile()) {
                FileInputStream in = new FileInputStream(inputFile);
                zipOut.putNextEntry(new ZipEntry(inputFile.getName()));
                int nNumber;
                byte[] buffer = new byte[512];
                while ((nNumber = in.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, nNumber);
                }
                in.close();
            }
        }
        zipOut.close();
    }
