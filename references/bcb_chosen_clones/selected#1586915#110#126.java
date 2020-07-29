    public void writeConfiguration(Writer out) throws IOException {
        if (myResource == null) {
            out.append("# Unable to print configuration resource\n");
        } else {
            URL url = myResource.getUrl();
            InputStream in = url.openStream();
            if (in != null) {
                try {
                    IOUtils.copy(in, out);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            } else {
                out.append("# Unable to print configuration resource\n");
            }
        }
    }
