    private static void replaceEntityMappings(File signserverearpath, File entityMappingXML) throws ZipException, IOException {
        ZipInputStream earFile = new ZipInputStream(new FileInputStream(signserverearpath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream tempZip = new ZipOutputStream(baos);
        ZipEntry next = earFile.getNextEntry();
        while (next != null) {
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            byte[] data = new byte[30000];
            int numberread;
            while ((numberread = earFile.read(data)) != -1) {
                content.write(data, 0, numberread);
            }
            if (next.getName().equals("signserver-ejb.jar")) {
                content = replaceEntityMappings(content, entityMappingXML);
                next = new ZipEntry("signserver-ejb.jar");
            }
            tempZip.putNextEntry(next);
            tempZip.write(content.toByteArray());
            next = earFile.getNextEntry();
        }
        earFile.close();
        tempZip.close();
        FileOutputStream fos = new FileOutputStream(signserverearpath);
        fos.write(baos.toByteArray());
        fos.close();
    }
