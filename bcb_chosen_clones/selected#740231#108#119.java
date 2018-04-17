    @Override
    public void setContentAsStream(InputStream input) throws IOException {
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(htmlFile));
        try {
            IOUtils.copy(input, output);
        } finally {
            output.close();
        }
        if (this.getLastModified() != -1) {
            htmlFile.setLastModified(this.getLastModified());
        }
    }
