    private void processData(InputStream raw) {
        String fileName = remoteName;
        if (localName != null) {
            fileName = localName;
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName), true);
            IOUtils.copy(raw, fos);
            LOG.info("ok");
        } catch (IOException e) {
            LOG.error("error writing file", e);
        }
    }
