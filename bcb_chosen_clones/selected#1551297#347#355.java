    private static String retrieveVersion(InputStream is) throws RepositoryException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            IOUtils.copy(is, buffer);
        } catch (IOException e) {
            throw new RepositoryException(exceptionLocalizer.format("device-repository-file-missing", DeviceRepositoryConstants.VERSION_FILENAME), e);
        }
        return buffer.toString().trim();
    }
