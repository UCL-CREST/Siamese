    private boolean copyToFile(String folder, String fileName) throws StorageException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(folder + "/" + fileName);
        if (in == null) {
            return false;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(path, fileName));
            IOUtils.copy(in, out);
            in.close();
            out.flush();
        } catch (Exception e) {
            throw new StorageException(e);
        } finally {
            if (in != null) {
                IOUtils.closeQuietly(in);
            }
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
        }
        return true;
    }
