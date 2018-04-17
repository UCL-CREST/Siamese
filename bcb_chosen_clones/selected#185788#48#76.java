    protected boolean processStreams(Transformer transformer, InjectorOptions options, ZipInputStream sourceStream, ZipOutputStream targetStream) throws Exception {
        Assert.assertNotNull("transformer", transformer);
        ZipEntry inEntry = null;
        ZipEntry outEntry = null;
        InputStream entryInputStream;
        addReadmeCommentFile(targetStream);
        while ((inEntry = sourceStream.getNextEntry()) != null) {
            outEntry = new ZipEntry(inEntry.getName());
            entryInputStream = new OpenInputStream(sourceStream);
            targetStream.putNextEntry(outEntry);
            String ext = getNormalizedExtention(inEntry);
            if (isArchiveExtention(ext)) {
                LOG.info("Entering nested archive : " + outEntry.getName());
                ZipInputStream nestedSourceStream = new ZipInputStream(entryInputStream);
                ZipOutputStream nestedTargetStream = new ZipOutputStream(targetStream);
                processStreams(transformer, options, nestedSourceStream, nestedTargetStream);
                nestedTargetStream.finish();
            } else if (isClassExtention(ext)) {
                LOG.debug("injecting " + inEntry.getName());
                byte[] transformedClass = transformer.inject(entryInputStream, inEntry.getName(), options);
                targetStream.write(transformedClass);
            } else {
                LOG.debug("copying " + inEntry.getName());
                IOUtil.copy(entryInputStream, targetStream);
            }
            targetStream.closeEntry();
        }
        return true;
    }
