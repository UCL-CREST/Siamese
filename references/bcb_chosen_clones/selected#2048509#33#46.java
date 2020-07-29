    private static void addFolderToZip(File folder, ZipOutputStream zip, String baseName) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                addFolderToZip(file, zip, baseName);
            } else {
                String name = file.getAbsolutePath().substring(baseName.length());
                ZipEntry zipEntry = new ZipEntry(name);
                zip.putNextEntry(zipEntry);
                IOUtils.copy(new FileInputStream(file), zip);
                zip.closeEntry();
            }
        }
    }
