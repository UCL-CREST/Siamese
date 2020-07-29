    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
        try {
            resolveFileAttachment();
        } catch (NoFileByTheIdException e) {
            throw new NotFoundException(e.getLocalizedMessage());
        }
        DefinableEntity owningEntity = fa.getOwner().getEntity();
        InputStream in = getFileModule().readFile(owningEntity.getParentBinder(), owningEntity, fa);
        try {
            if (range != null) {
                if (logger.isDebugEnabled()) logger.debug("sendContent: ranged content: " + toString(fa));
                PartialGetHelper.writeRange(in, range, out);
            } else {
                if (logger.isDebugEnabled()) logger.debug("sendContent: send whole file " + toString(fa));
                IOUtils.copy(in, out);
            }
            out.flush();
        } catch (ReadingException e) {
            throw new IOException(e);
        } catch (WritingException e) {
            throw new IOException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
