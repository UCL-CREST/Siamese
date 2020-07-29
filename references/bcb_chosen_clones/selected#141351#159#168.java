    public static void addStringToZip(String text, String entryName, ZipOutputStream zOut) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(text));
        ZipEntry zipEntry = new ZipEntry(entryName);
        zOut.putNextEntry(zipEntry);
        int i;
        while ((i = reader.read()) != -1) {
            zOut.write(i);
        }
        zOut.closeEntry();
    }
