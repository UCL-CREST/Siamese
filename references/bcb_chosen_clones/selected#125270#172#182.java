    public byte[] getConfigFile(String fileName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        File file = new File("config/" + fileName);
        if (file.exists() == false) {
            throw new IOException("source not found, name was: " + fileName);
        }
        FileInputStream in = new FileInputStream(file);
        Copy.copy(in, out);
        byte[] result = out.toByteArray();
        return result;
    }
