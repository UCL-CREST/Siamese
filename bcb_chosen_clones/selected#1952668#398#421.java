    public static void unZip(String unZipfileName, String outputDirectory) throws IOException, FileNotFoundException {
        FileOutputStream fileOut;
        File file;
        ZipEntry zipEntry;
        ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(unZipfileName)), encoder);
        while ((zipEntry = zipIn.getNextEntry()) != null) {
            file = new File(outputDirectory + File.separator + zipEntry.getName());
            if (zipEntry.isDirectory()) {
                createDirectory(file.getPath(), "");
            } else {
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    createDirectory(parent.getPath(), "");
                }
                fileOut = new FileOutputStream(file);
                int readedBytes;
                while ((readedBytes = zipIn.read(buf)) > 0) {
                    fileOut.write(buf, 0, readedBytes);
                }
                fileOut.close();
            }
            zipIn.closeEntry();
        }
    }
