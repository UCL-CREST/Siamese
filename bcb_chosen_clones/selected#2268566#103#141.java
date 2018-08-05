    public String loadFileContent(final String _resourceURI) {
        final Lock readLock = this.fileLock.readLock();
        final Lock writeLock = this.fileLock.writeLock();
        boolean hasReadLock = false;
        boolean hasWriteLock = false;
        try {
            readLock.lock();
            hasReadLock = true;
            if (!this.cachedResources.containsKey(_resourceURI)) {
                readLock.unlock();
                hasReadLock = false;
                writeLock.lock();
                hasWriteLock = true;
                if (!this.cachedResources.containsKey(_resourceURI)) {
                    final InputStream resourceAsStream = this.getClass().getResourceAsStream(_resourceURI);
                    final StringWriter writer = new StringWriter();
                    try {
                        IOUtils.copy(resourceAsStream, writer);
                    } catch (final IOException ex) {
                        throw new IllegalStateException("Resource not read-able", ex);
                    }
                    final String loadedResource = writer.toString();
                    this.cachedResources.put(_resourceURI, loadedResource);
                }
                writeLock.unlock();
                hasWriteLock = false;
                readLock.lock();
                hasReadLock = true;
            }
            return this.cachedResources.get(_resourceURI);
        } finally {
            if (hasReadLock) {
                readLock.unlock();
            }
            if (hasWriteLock) {
                writeLock.unlock();
            }
        }
    }
