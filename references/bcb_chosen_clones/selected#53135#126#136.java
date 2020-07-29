    private static void addFileToZip(String base, File file, ZipOutputStream out) throws FileNotFoundException, IOException {
        byte[] data = new byte[BUFFER];
        BufferedInputStream srcBIS = new BufferedInputStream(new FileInputStream(file), BUFFER);
        ZipEntry entry = new ZipEntry(file.getAbsolutePath().replace(base, ""));
        out.putNextEntry(entry);
        int count;
        while ((count = srcBIS.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
        }
        srcBIS.close();
    }
