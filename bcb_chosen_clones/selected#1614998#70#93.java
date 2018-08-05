    private static void setEnvEntry(File fromEAR, File toEAR, String ejbJarName, String envEntryName, String envEntryValue) throws Exception {
        ZipInputStream earFile = new ZipInputStream(new FileInputStream(fromEAR));
        FileOutputStream fos = new FileOutputStream(toEAR);
        ZipOutputStream tempZip = new ZipOutputStream(fos);
        ZipEntry next = earFile.getNextEntry();
        while (next != null) {
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            byte[] data = new byte[30000];
            int numberread;
            while ((numberread = earFile.read(data)) != -1) {
                content.write(data, 0, numberread);
            }
            if (next.getName().equals(ejbJarName)) {
                content = editEJBJAR(next, content, envEntryName, envEntryValue);
                next = new ZipEntry(ejbJarName);
            }
            tempZip.putNextEntry(next);
            tempZip.write(content.toByteArray());
            next = earFile.getNextEntry();
        }
        earFile.close();
        tempZip.close();
        fos.close();
    }
