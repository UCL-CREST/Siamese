    private static void fileToZip(File argFile, File argZipFile) throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(argZipFile);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
            zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
            ZipEntry zipEntry = new ZipEntry(argFile.getName());
            zipOutputStream.putNextEntry(zipEntry);
            FileInputStream fileInputStream = new FileInputStream(argFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, BUFFER);
            byte data[] = new byte[BUFFER];
            int count;
            while ((count = bufferedInputStream.read(data, 0, BUFFER)) != -1) {
                zipOutputStream.write(data, 0, count);
            }
            bufferedInputStream.close();
        } finally {
            zipOutputStream.close();
            fileOutputStream.close();
        }
    }
