    private void write(File src, File dst, byte id3v1Tag[], byte id3v2HeadTag[], byte id3v2TailTag[]) throws IOException {
        if (src == null || !src.exists()) throw new IOException(Debug.getDebug("missing src", src));
        if (!src.getName().toLowerCase().endsWith(".mp3")) throw new IOException(Debug.getDebug("src not mp3", src));
        if (dst == null) throw new IOException(Debug.getDebug("missing dst", dst));
        if (dst.exists()) {
            dst.delete();
            if (dst.exists()) throw new IOException(Debug.getDebug("could not delete dst", dst));
        }
        boolean hasId3v1 = new MyID3v1().hasID3v1(src);
        long id3v1Length = hasId3v1 ? ID3_V1_TAG_LENGTH : 0;
        long id3v2HeadLength = new MyID3v2().findID3v2HeadLength(src);
        long id3v2TailLength = new MyID3v2().findID3v2TailLength(src, hasId3v1);
        OutputStream os = null;
        InputStream is = null;
        try {
            dst.getParentFile().mkdirs();
            os = new FileOutputStream(dst);
            os = new BufferedOutputStream(os);
            if (!skipId3v2Head && !skipId3v2 && id3v2HeadTag != null) os.write(id3v2HeadTag);
            is = new FileInputStream(src);
            is = new BufferedInputStream(is);
            is.skip(id3v2HeadLength);
            long total_to_read = src.length();
            total_to_read -= id3v1Length;
            total_to_read -= id3v2HeadLength;
            total_to_read -= id3v2TailLength;
            byte buffer[] = new byte[1024];
            long total_read = 0;
            while (total_read < total_to_read) {
                int remainder = (int) (total_to_read - total_read);
                int readSize = Math.min(buffer.length, remainder);
                int read = is.read(buffer, 0, readSize);
                if (read <= 0) throw new IOException("unexpected EOF");
                os.write(buffer, 0, read);
                total_read += read;
            }
            if (!skipId3v2Tail && !skipId3v2 && id3v2TailTag != null) os.write(id3v2TailTag);
            if (!skipId3v1 && id3v1Tag != null) os.write(id3v1Tag);
        } finally {
            try {
                if (is != null) is.close();
            } catch (Throwable e) {
                Debug.debug(e);
            }
            try {
                if (os != null) os.close();
            } catch (Throwable e) {
                Debug.debug(e);
            }
        }
    }
