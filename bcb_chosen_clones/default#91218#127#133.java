    private static byte[] getDataFile(String filename) throws Exception {
        filename = "moduls/alfrescoClient/test/" + filename;
        FileInputStream fis = new FileInputStream(filename);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
        copy(fis, bos);
        return bos.toByteArray();
    }
