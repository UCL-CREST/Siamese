    public static void fixEol(File fin) throws IOException {
        File fout = File.createTempFile(fin.getName(), ".fixEol", fin.getParentFile());
        FileChannel in = new FileInputStream(fin).getChannel();
        if (0 != in.size()) {
            FileChannel out = new FileOutputStream(fout).getChannel();
            byte[] eol = AStringUtilities.systemNewLine.getBytes();
            ByteBuffer bufOut = ByteBuffer.allocateDirect(1024 * eol.length);
            boolean previousIsCr = false;
            ByteBuffer buf = ByteBuffer.allocateDirect(1024);
            while (in.read(buf) > 0) {
                buf.limit(buf.position());
                buf.position(0);
                while (buf.remaining() > 0) {
                    byte b = buf.get();
                    if (b == '\r') {
                        previousIsCr = true;
                        bufOut.put(eol);
                    } else {
                        if (b == '\n') {
                            if (!previousIsCr) bufOut.put(eol);
                        } else bufOut.put(b);
                        previousIsCr = false;
                    }
                }
                bufOut.limit(bufOut.position());
                bufOut.position(0);
                out.write(bufOut);
                bufOut.clear();
                buf.clear();
            }
            out.close();
        }
        in.close();
        fin.delete();
        fout.renameTo(fin);
    }
