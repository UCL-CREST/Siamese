    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException {
        try {
            if (vtf == null) {
                LOG.debug("Serializing from database");
                existDocument.stream(out);
            } else {
                LOG.debug("Serializing from buffer");
                InputStream is = vtf.getByteStream();
                IOUtils.copy(is, out);
                out.flush();
                IOUtils.closeQuietly(is);
                vtf.delete();
                vtf = null;
            }
        } catch (PermissionDeniedException e) {
            LOG.debug(e.getMessage());
            throw new NotAuthorizedException(this);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
