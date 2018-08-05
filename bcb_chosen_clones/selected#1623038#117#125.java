    private byte[] readFile(String file) throws IOException {
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        for (int read = in.read(buffer, 0, buffer.length); read != -1; read = in.read(buffer, 0, buffer.length)) {
            content.write(buffer, 0, read);
        }
        return content.toByteArray();
    }
