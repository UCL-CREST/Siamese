    @Override
    public void writeTo(final TrackRepresentation t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws WebApplicationException {
        if (mediaType.isCompatible(MediaType.APPLICATION_OCTET_STREAM_TYPE)) {
            InputStream is = null;
            try {
                httpHeaders.add("Content-Type", "audio/mp3");
                IOUtils.copy(is = t.getInputStream(mediaType), entityStream);
            } catch (final IOException e) {
                LOG.warn("IOException : maybe remote client has disconnected");
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
    }
