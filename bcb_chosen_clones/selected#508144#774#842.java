    private void compress(File target, Set<File> files) throws CacheOperationException, ConfigurationException {
        ZipOutputStream zipOutput = null;
        try {
            zipOutput = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)));
            for (File file : files) {
                BufferedInputStream fileInput = null;
                File cachePathName = new File(cacheFolder, file.getPath());
                try {
                    if (!cachePathName.exists()) {
                        throw new CacheOperationException("Expected to add file ''{0}'' to export archive ''{1}'' (Account : {2}) but it " + "has gone missing (cause unknown). This can indicate implementation or deployment " + "error. Aborting export operation as a safety precaution.", cachePathName.getPath(), target.getAbsolutePath(), account.getOid());
                    }
                    fileInput = new BufferedInputStream(new FileInputStream(cachePathName));
                    ZipEntry entry = new ZipEntry(file.getPath());
                    entry.setSize(cachePathName.length());
                    entry.setTime(cachePathName.lastModified());
                    zipOutput.putNextEntry(entry);
                    cacheLog.debug("Added new export zip entry ''{0}''.", file.getPath());
                    int count, total = 0;
                    int buffer = 2048;
                    byte[] data = new byte[buffer];
                    while ((count = fileInput.read(data, 0, buffer)) != -1) {
                        zipOutput.write(data, 0, count);
                        total += count;
                    }
                    zipOutput.flush();
                    if (total != cachePathName.length()) {
                        throw new CacheOperationException("Only wrote {0} out of {1} bytes when archiving file ''{2}'' (Account : {3}). " + "This could have occured either due implementation error or file I/O error. " + "Aborting archive operation to prevent a potentially corrupt export archive to " + "be created.", total, cachePathName.length(), cachePathName.getPath(), account.getOid());
                    } else {
                        cacheLog.debug("Wrote {0} out of {1} bytes to zip entry ''{2}''", total, cachePathName.length(), file.getPath());
                    }
                } catch (SecurityException e) {
                    throw new ConfigurationException("Security manager has denied r/w access when attempting to read file ''{0}'' and " + "write it to archive ''{1}'' (Account : {2}) : {3}", e, cachePathName.getPath(), target, account.getOid(), e.getMessage());
                } catch (IllegalArgumentException e) {
                    throw new CacheOperationException("Error creating ZIP archive for account ID = {0} : {1}", e, account.getOid(), e.getMessage());
                } catch (FileNotFoundException e) {
                    throw new CacheOperationException("Attempted to include file ''{0}'' in export archive but it has gone missing " + "(Account : {1}). Possible implementation error in local file cache. Aborting  " + "export operation as a precaution ({2})", e, cachePathName.getPath(), account.getOid(), e.getMessage());
                } catch (ZipException e) {
                    throw new CacheOperationException("Error writing export archive for account ID = {0} : {1}", e, account.getOid(), e.getMessage());
                } catch (IOException e) {
                    throw new CacheOperationException("I/O error while creating export archive for account ID = {0}. " + "Operation aborted ({1})", e, account.getOid(), e.getMessage());
                } finally {
                    if (zipOutput != null) {
                        try {
                            zipOutput.closeEntry();
                        } catch (Throwable t) {
                            cacheLog.warn("Unable to close zip entry for file ''{0}'' in export archive ''{1}'' " + "(Account : {2}) : {3}.", t, file.getPath(), target.getAbsolutePath(), account.getOid(), t.getMessage());
                        }
                    }
                    if (fileInput != null) {
                        try {
                            fileInput.close();
                        } catch (Throwable t) {
                            cacheLog.warn("Failed to close input stream from file ''{0}'' being added " + "to export archive (Account : {1}) : {2}", t, cachePathName.getPath(), account.getOid(), t.getMessage());
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new CacheOperationException("Unable to create target export archive ''{0}'' for account {1) : {2}", e, target, account.getOid(), e.getMessage());
        } finally {
            try {
                if (zipOutput != null) {
                    zipOutput.close();
                }
            } catch (Throwable t) {
                cacheLog.warn("Failed to close the stream to export archive ''{0}'' : {1}.", t, target, t.getMessage());
            }
        }
    }
