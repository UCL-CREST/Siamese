    private byte[] initBuffer() throws IOException {
        FileInputStream input = null;
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] bytes = new byte[BUFFER_SIZE];
        try {
            input = new FileInputStream(sourceLocation);
            while (true) {
                final int len = input.read(bytes, 0, BUFFER_SIZE);
                if (len == -1) {
                    break;
                }
                output.write(bytes, 0, len);
            }
        } finally {
            if (input != null) input.close();
            output.close();
        }
        return output.toByteArray();
    }
