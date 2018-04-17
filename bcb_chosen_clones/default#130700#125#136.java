    public byte[] loadBytes(String name) throws IOException {
        FileInputStream in = null;
        in = new FileInputStream(name);
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int ch;
            while ((ch = in.read()) != -1) buffer.write(ch);
            return buffer.toByteArray();
        } finally {
            in.close();
        }
    }
