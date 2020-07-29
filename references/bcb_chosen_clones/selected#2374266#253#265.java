    private static void processFile(File file, ZipOutputStream zos, boolean verboseFlag) throws IOException {
        String path = file.getPath().replace('\\', '/');
        if (verboseFlag) {
            System.out.println("adding: " + path);
        }
        ZipEntry zEntry = new ZipEntry(path);
        zos.putNextEntry(zEntry);
        FileInputStream fis = new FileInputStream(file);
        byte fileContent[] = new byte[(int) file.length()];
        fis.read(fileContent);
        zos.write(fileContent);
        zos.closeEntry();
    }
