    private static void insertModuleInEar(File fromEar, File toEar, String moduleType, String moduleName, String contextRoot) throws Exception {
        ZipInputStream earFile = new ZipInputStream(new FileInputStream(fromEar));
        FileOutputStream fos = new FileOutputStream(toEar);
        ZipOutputStream tempZip = new ZipOutputStream(fos);
        ZipEntry next = earFile.getNextEntry();
        while (next != null) {
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            byte[] data = new byte[30000];
            int numberread;
            while ((numberread = earFile.read(data)) != -1) {
                content.write(data, 0, numberread);
            }
            if (next.getName().equals("META-INF/application.xml")) {
                content = insertModule(earFile, next, content, moduleType, moduleName, contextRoot);
                next = new ZipEntry("META-INF/application.xml");
            }
            tempZip.putNextEntry(next);
            tempZip.write(content.toByteArray());
            next = earFile.getNextEntry();
        }
        earFile.close();
        tempZip.close();
        fos.close();
    }
