    public static Long copyFile(InputStream in, OutputStream out) throws IOException {
        long millis = System.currentTimeMillis();
        final CRC32 checksum = verify ? new CRC32() : null;
        if (verify) {
            checksum.reset();
        }
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        if (verify) while ((bytesRead = in.read(buffer)) >= 0) {
            checksum.update(buffer, 0, bytesRead);
            out.write(buffer, 0, bytesRead);
        } else while ((bytesRead = in.read(buffer)) >= 0) out.write(buffer, 0, bytesRead);
        out.close();
        in.close();
        if (clock) {
            millis = System.currentTimeMillis() - millis;
        }
        if (verify) {
            return new Long(checksum.getValue());
        } else {
            return null;
        }
    }
