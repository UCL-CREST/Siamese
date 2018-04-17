    public static void appendZipFile(ZipOutputStream zos, File a_file) throws IOException {
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        FileInputStream fis = new FileInputStream(a_file);
        ZipEntry l_anEntry = new ZipEntry(a_file.getName());
        zos.putNextEntry(l_anEntry);
        while ((bytesIn = fis.read(readBuffer)) != -1) {
            zos.write(readBuffer, 0, bytesIn);
        }
        fis.close();
    }
