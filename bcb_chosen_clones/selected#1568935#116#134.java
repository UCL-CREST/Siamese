    private ByteBuffer readProgram(URL url) throws IOException {
        StringBuilder program = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                program.append(line).append("\n");
            }
        } finally {
            if (in != null) in.close();
        }
        ByteBuffer buffer = BufferUtils.createByteBuffer(program.length());
        for (int i = 0; i < program.length(); i++) {
            buffer.put((byte) (program.charAt(i) & 0xFF));
        }
        buffer.flip();
        return buffer;
    }
