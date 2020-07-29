    public static void appendZip(String srcItem, String destZipFile) throws Exception {
        File zip = new File(destZipFile);
        File fileAppend = new File(srcItem);
        ArrayList<File> files = new ArrayList<File>();
        if (FileResource.isFile(srcItem)) {
            files.add(fileAppend);
        } else {
            listFiles(files, fileAppend);
        }
        FileInputStream inStream = null;
        byte[] data = new byte[BUFFER];
        ZipFile zipFile = new ZipFile(zip);
        Enumeration entries = zipFile.entries();
        String tempPath = System.getProperty("java.io.tmpdir") + zip.getName();
        ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(tempPath));
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            InputStream is = zipFile.getInputStream(entry);
            outStream.putNextEntry(entry);
            int count = 0;
            while ((count = is.read(data)) > 0) {
                outStream.write(data, 0, count);
            }
            outStream.closeEntry();
            is.close();
        }
        for (int i = 0; i < files.size(); ++i) {
            File file = files.get(i);
            inStream = new FileInputStream(file);
            outStream.putNextEntry(new ZipEntry(file.getName()));
            int count = 0;
            while ((count = inStream.read(data)) != -1) {
                outStream.write(data, 0, count);
            }
            outStream.closeEntry();
            inStream.close();
        }
        outStream.flush();
        outStream.close();
        zip.delete();
        XItem.copy(tempPath, destZipFile);
    }
