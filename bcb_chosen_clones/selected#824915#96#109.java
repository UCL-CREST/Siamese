    private byte[] read(File file) throws BuildException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[4096];
            int l;
            while ((l = fis.read(buf)) > -1) {
                bos.write(buf, 0, l);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new BuildException("IO error while reading XQuery source: " + file.getAbsolutePath() + ": " + e.getMessage(), e);
        }
    }
