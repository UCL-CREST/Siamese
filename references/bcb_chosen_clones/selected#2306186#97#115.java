    private void nioBuild() {
        try {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 4);
            final FileChannel out = new FileOutputStream(dest).getChannel();
            for (File part : parts) {
                setState(part.getName(), BUILDING);
                FileChannel in = new FileInputStream(part).getChannel();
                while (in.read(buffer) > 0) {
                    buffer.flip();
                    written += out.write(buffer);
                    buffer.clear();
                }
                in.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
