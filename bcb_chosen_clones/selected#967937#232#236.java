    public static void setContentFromFile(IDfSysObject document, File file) throws DfException, IOException {
        FileInputStream fileStream = new FileInputStream(file);
        byte[] content = IOUtils.toByteArray(fileStream);
        setContent(document, content);
    }
