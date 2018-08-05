    public DataRecord addRecord(InputStream input) throws DataStoreException {
        File temporary = null;
        try {
            temporary = newTemporaryFile();
            DataIdentifier tempId = new DataIdentifier(temporary.getName());
            usesIdentifier(tempId);
            long length = 0;
            MessageDigest digest = MessageDigest.getInstance(DIGEST);
            OutputStream output = new DigestOutputStream(new FileOutputStream(temporary), digest);
            try {
                length = IOUtils.copyLarge(input, output);
            } finally {
                output.close();
            }
            DataIdentifier identifier = new DataIdentifier(digest.digest());
            File file;
            synchronized (this) {
                usesIdentifier(identifier);
                file = getFile(identifier);
                if (!file.exists()) {
                    File parent = file.getParentFile();
                    parent.mkdirs();
                    if (temporary.renameTo(file)) {
                        temporary = null;
                    } else {
                        throw new IOException("Can not rename " + temporary.getAbsolutePath() + " to " + file.getAbsolutePath() + " (media read only?)");
                    }
                } else {
                    long now = System.currentTimeMillis();
                    if (getLastModified(file) < now + ACCESS_TIME_RESOLUTION) {
                        setLastModified(file, now + ACCESS_TIME_RESOLUTION);
                    }
                }
                if (file.length() != length) {
                    if (!file.isFile()) {
                        throw new IOException("Not a file: " + file);
                    }
                    throw new IOException(DIGEST + " collision: " + file);
                }
            }
            inUse.remove(tempId);
            return new FileDataRecord(identifier, file);
        } catch (NoSuchAlgorithmException e) {
            throw new DataStoreException(DIGEST + " not available", e);
        } catch (IOException e) {
            throw new DataStoreException("Could not add record", e);
        } finally {
            if (temporary != null) {
                temporary.delete();
            }
        }
    }
