    public byte[] get(String uri) throws IOException {
        String name = (String) uriHt.get(uri);
        if (name == null) return null;
        File f = new File(name);
        if (!f.exists()) return null;
        FileInputStream is = new FileInputStream(f);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int nRead; (nRead = is.read(buf, 0, 1024)) > 0; ) os.write(buf, 0, nRead);
        return os.toByteArray();
    }
