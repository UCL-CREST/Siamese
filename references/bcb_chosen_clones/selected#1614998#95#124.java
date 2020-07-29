    private static ByteArrayOutputStream editEJBJAR(ZipEntry zipEntry, ByteArrayOutputStream content, String envEntryName, String envEntryValue) throws Exception {
        ZipInputStream jarFile = new ZipInputStream(new ByteArrayInputStream(content.toByteArray()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream tempZip = new ZipOutputStream(baos);
        ZipEntry next = jarFile.getNextEntry();
        HashSet<String> addedEntries = new HashSet<String>();
        while (next != null) {
            if (addedEntries.contains(next.getName())) {
                next = jarFile.getNextEntry();
                continue;
            }
            ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            byte[] data = new byte[30000];
            int numberread;
            while ((numberread = jarFile.read(data)) != -1) {
                dataStream.write(data, 0, numberread);
            }
            if (next.getName().equals("META-INF/ejb-jar.xml")) {
                dataStream = editEnvEntry(next, dataStream, envEntryName, envEntryValue);
                next = new ZipEntry("META-INF/ejb-jar.xml");
            }
            addedEntries.add(next.getName());
            tempZip.putNextEntry(next);
            tempZip.write(dataStream.toByteArray());
            next = jarFile.getNextEntry();
        }
        jarFile.close();
        tempZip.close();
        return baos;
    }
