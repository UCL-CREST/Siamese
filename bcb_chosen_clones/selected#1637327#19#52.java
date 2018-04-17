    public void toZipFile(String filePath, String outFile) {
        ArrayList fileNames = new ArrayList();
        ArrayList files = new ArrayList();
        try {
            File rootFile = new File(filePath);
            listFile(rootFile, fileNames, files);
            if (files != null && files.size() > 0) {
                FileOutputStream fileOut = new FileOutputStream(outFile);
                CheckedOutputStream ch = new CheckedOutputStream(fileOut, new CRC32());
                BufferedOutputStream bout = new BufferedOutputStream(ch);
                ZipOutputStream outputStream = new ZipOutputStream(bout);
                for (int loop = 0; loop < files.size(); loop++) {
                    FileInputStream fileIn = new FileInputStream((File) files.get(loop));
                    InputStreamReader inReader = new InputStreamReader(fileIn, "ISO8859_1");
                    BufferedReader in = new BufferedReader(inReader);
                    outputStream.putNextEntry(new ZipEntry((String) fileNames.get(loop)));
                    int c;
                    while ((c = in.read()) != -1) {
                        outputStream.write(c);
                    }
                    fileIn.close();
                    in.close();
                    outputStream.closeEntry();
                }
                for (int i = 0; i < files.size(); i++) {
                    File file = (File) files.get(i);
                    file.delete();
                }
                outputStream.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
