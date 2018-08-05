    @Override
    public void save(String arxivId, InputStream inputStream, String encoding) {
        String filename = StringUtil.arxivid2filename(arxivId, "tex");
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(String.format("%s/%s", LATEX_DOCUMENT_DIR, filename)), encoding);
            IOUtils.copy(inputStream, writer, encoding);
            writer.flush();
            writer.close();
            inputStream.close();
        } catch (IOException e) {
            logger.error("Failed to save the Latex source with id='{}'", arxivId, e);
            throw new RuntimeException(e);
        }
    }
