    public synchronized boolean writeFile(String filename, InputStream stream) throws IOException {
        if (fileSent) return false;
        BufferedInputStream origin = new BufferedInputStream(stream, BUFFER);
        try {
            byte data[] = new byte[BUFFER];
            ZipEntry entry = new ZipEntry(filename);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            fileWritten = true;
            interrupt();
            return true;
        } catch (IOException e) {
            LOG.error("Error writing to zip file '" + zipFile + "'", e);
            interrupt();
            throw e;
        } finally {
            CloseUtils.safeClose(origin);
        }
    }
