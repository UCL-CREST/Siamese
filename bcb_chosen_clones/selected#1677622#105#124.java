    @Override
    public void downloadByUUID(final UUID uuid, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (!exportsInProgress.containsKey(uuid)) {
            throw new IllegalStateException("No download with UUID: " + uuid);
        }
        final File compressedFile = exportsInProgress.get(uuid).file;
        logger.debug("File size: " + compressedFile.length());
        OutputStream output = null;
        InputStream fileInputStream = null;
        try {
            output = response.getOutputStream();
            prepareResponse(request, response, compressedFile);
            fileInputStream = new FileInputStream(compressedFile);
            IOUtils.copy(fileInputStream, output);
            output.flush();
        } finally {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(output);
        }
    }
