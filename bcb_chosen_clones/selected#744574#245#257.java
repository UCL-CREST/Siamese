    private static void zipFichier(File f, String currentDir, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        String path;
        if (currentDir.equals("")) path = f.getName(); else path = currentDir + "/" + f.getName();
        ZipEntry anEntry = new ZipEntry(path);
        zos.putNextEntry(anEntry);
        while ((bytesIn = fis.read(readBuffer)) != -1) {
            zos.write(readBuffer, 0, bytesIn);
        }
        fis.close();
    }
