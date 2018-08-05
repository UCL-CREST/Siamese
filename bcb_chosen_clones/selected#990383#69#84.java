    private static void writeJarFile(ZipOutputStream myZip) throws Exception {
        myZip.putNextEntry(new ZipEntry("org/ea/Extractor.class"));
        String p = System.getProperty("java.class.path");
        ZipInputStream myJar = new ZipInputStream(new FileInputStream(p));
        ZipEntry ze = null;
        while ((ze = myJar.getNextEntry()) != null) {
            if (ze.getName().compareTo("org/ea/Extractor.class") == 0) {
                int i = 0;
                while ((i = myJar.read()) != -1) {
                    myZip.write(i);
                }
            }
        }
        myJar.close();
        myZip.closeEntry();
    }
