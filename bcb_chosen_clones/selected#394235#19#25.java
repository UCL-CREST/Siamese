    public static Attachment create(File f) throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {
        FileInputStream fis = new FileInputStream(f);
        byte[] data = IOUtils.toByteArray(fis);
        fis.close();
        String mimeType = Magic.getMagicMatch(f, false).getMimeType();
        return create(f.getName(), mimeType, data);
    }
