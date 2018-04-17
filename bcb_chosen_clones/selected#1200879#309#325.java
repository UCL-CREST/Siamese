    private File unzipArchive(File zipArchive, File outDir, String nameInZipArchive) throws IOException {
        File mainFile = null;
        ZipEntry entry = null;
        ZipInputStream zis = new ZipInputStream(new FileInputStream((zipArchive)));
        FileOutputStream fos = null;
        byte buffer[] = new byte[4096];
        int bytesRead;
        while ((entry = zis.getNextEntry()) != null) {
            File outFile = new File(outDir, entry.getName());
            if (entry.getName().equals(nameInZipArchive)) mainFile = outFile;
            fos = new FileOutputStream(outFile);
            while ((bytesRead = zis.read(buffer)) != -1) fos.write(buffer, 0, bytesRead);
            fos.close();
        }
        zis.close();
        return mainFile;
    }
