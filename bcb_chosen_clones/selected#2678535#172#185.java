    private void copyOutResource(String dstPath, InputStream in) throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        try {
            dstPath = this.outputDir + dstPath;
            File file = new File(dstPath);
            file.getParentFile().mkdirs();
            out = new FileOutputStream(file);
            IOUtils.copy(in, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
