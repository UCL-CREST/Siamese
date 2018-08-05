    private void syncFiles(File src, File dst) throws IOException {
        callback.incStepProgressIndicatorPosition();
        if ((src == null) || (dst == null)) {
            if (src == null) logger.warn("syncFiles: Unexpected 'src' null parameter"); else logger.warn("syncFiles: Unexpected 'dst' null parameter");
            return;
        }
        if (!src.exists()) {
            File f = new File(src.getAbsolutePath());
            if (f.exists() == false) {
                logger.warn("syncFiles: Unexpected missing file: " + src.getAbsolutePath());
                return;
            } else {
                logger.debug("syncFiles: Incorrect caching of exists()=false status for file: " + src.getAbsolutePath());
            }
        }
        if (src.getAbsolutePath().equalsIgnoreCase(dst.getAbsolutePath())) {
            copyToSelf++;
            if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("syncFiles: attempting to copy file to itself: " + src.getAbsolutePath());
            return;
        }
        CachedFile cf_src = CachedFileManager.INSTANCE.inCache(src);
        CachedFile cf_dst = CachedFileManager.INSTANCE.inCache(dst);
        if (cf_src == null) {
            cf_src = new CachedFile(src.getPath());
            if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("syncFiles: Source not in cache: " + src.getPath());
        }
        if (cf_dst == null) {
            cf_dst = CachedFileManager.INSTANCE.addCachedFile(dst);
            if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("syncFiles: Target not in cache: " + src.getPath());
            cf_dst.setTarget(true);
        }
        if (cf_src.isDirectory()) {
            if (logger.isTraceEnabled()) logger.trace("Directory " + cf_src.getName() + " Processing Started");
            callback.showMessage(src.getParentFile().getName() + File.separator + cf_src.getName());
            if (!cf_dst.exists()) {
                if (logger.isTraceEnabled()) logger.trace("Directory " + cf_dst.getName() + " Create missing target");
                if (syncLog) syncLogFile.printf("CREATED: %s\n", cf_dst.getName());
                dst.mkdirs();
            }
            if (!cf_dst.isDirectory()) {
                logger.warn("Directory " + cf_src.getName() + " Unexpected file with name expected for directory");
                return;
            }
            File sourceFiles[] = src.listFiles();
            List<File> targetNotInSourceFiles = new LinkedList<File>(Arrays.asList(dst.listFiles()));
            for (int i = 0; i < sourceFiles.length; i++) {
                File sourceFile = sourceFiles[i];
                String fileName = sourceFile.getName();
                File destFile = new File(dst, fileName);
                if (destFile.exists()) {
                    if ((cf_src.getName().endsWith(".xml")) && (currentProfile.getGenerateOpds() == true)) {
                        if (logger.isTraceEnabled()) logger.trace("No OPDS catalog so delete " + src.getName());
                    } else {
                        targetNotInSourceFiles.remove(destFile);
                        if (CachedFileManager.INSTANCE.inCache(destFile) == null) destFile = CachedFileManager.INSTANCE.addCachedFile(destFile);
                    }
                } else {
                    if (logger.isTraceEnabled()) logger.trace("Directory " + src.getName() + " Unexpected missing target");
                    CachedFileManager.INSTANCE.removeCachedFile(destFile);
                }
                syncFiles(sourceFile, destFile);
            }
            for (File file : targetNotInSourceFiles) {
                Helper.delete(file);
                if (syncLog) syncLogFile.printf("DELETED: %s\n", file.getAbsolutePath());
                if (CachedFileManager.INSTANCE.inCache(file) != null) {
                    CachedFileManager.INSTANCE.removeCachedFile(file);
                }
            }
            if (logger.isTraceEnabled()) logger.trace("Directory " + src.getName() + " Processing completed");
        } else {
            boolean copyflag;
            if (!currentProfile.getGenerateOpds()) {
                if (cf_src.getName().endsWith(".xml")) {
                    if (cf_dst.exists()) {
                        if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("File " + cf_dst.getName() + ": Deleted as XML file and no OPDS catalog required");
                    } else {
                        if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": Ignored as XML file and no OPDS catalog required");
                    }
                    CachedFileManager.INSTANCE.removeCachedFile(cf_src);
                    CachedFileManager.INSTANCE.removeCachedFile(cf_dst);
                    return;
                }
            }
            if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": Checking to see if should be copied");
            if (!cf_dst.exists()) {
                if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": Copy as target is missing");
                copyExistHits++;
                copyflag = true;
                if (syncLog) syncLogFile.printf("COPIED (New file): %s\n", cf_dst.getName());
            } else {
                if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": .. exists on target");
                if (cf_src.length() != cf_dst.length()) {
                    if (logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": Copy as size changed");
                    copyLengthHits++;
                    copyflag = true;
                    if (syncLog) syncLogFile.printf("COPIED (length changed): %s\n", cf_src.getName());
                } else {
                    if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": .. size same on source and target");
                    if (cf_src.lastModified() <= cf_dst.lastModified()) {
                        if (logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": Skip Copy as source is not newer");
                        copyDateMisses++;
                        copyflag = false;
                        if (syncLog) syncLogFile.printf("NOT COPIED (Source not newer): %s\n", cf_dst.getName());
                    } else {
                        if (syncFilesDetail && logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": .. source is newer");
                        if (!checkCRC) {
                            if (logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": Copy as CRC check not active");
                            if (cf_dst.isCrc()) if (logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + "CRC entry invalidated");
                            cf_dst.clearCrc();
                            copyCrcUnchecked++;
                            copyflag = true;
                            if (syncLog) syncLogFile.printf("COPIED (CRC check not active): %s\n", cf_src.getName());
                        } else {
                            if (cf_src.getCrc() != cf_dst.getCrc()) {
                                if (logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": Copy as CRC's different");
                                copyCrcHits++;
                                copyflag = true;
                                if (syncLog) syncLogFile.printf("COPIED (CRC changed): %s\n", cf_src.getName());
                            } else {
                                if (logger.isTraceEnabled()) logger.trace("File " + cf_src.getName() + ": Skip copy as CRC's match");
                                copyCrcMisses++;
                                copyflag = false;
                                if (syncLog) syncLogFile.printf("NOT COPIED (CRC same): %s\n", cf_src.getName());
                            }
                        }
                    }
                }
            }
            if (copyflag) {
                callback.showMessage(src.getParentFile().getName() + File.separator + src.getName());
                if (syncFilesDetail && logger.isDebugEnabled()) logger.debug("Copying file " + cf_src.getName());
                Helper.copy(cf_src, cf_dst);
                cf_dst.setCrc(cf_src.getCrc());
            }
        }
    }
