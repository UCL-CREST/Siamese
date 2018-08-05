    public File addFile(File file, String suffix) throws IOException {
        if (file.exists() && file.isFile()) {
            File nf = File.createTempFile(prefix, "." + suffix, workdir);
            nf.delete();
            if (!file.renameTo(nf)) {
                IOUtils.copy(file, nf);
            }
            synchronized (fileList) {
                fileList.add(nf);
            }
            if (log.isDebugEnabled()) {
                log.debug("Add file [" + file.getPath() + "] -> [" + nf.getPath() + "]");
            }
            return nf;
        }
        return file;
    }
