    @Override
    protected void executeTask(ErrorHandler err) throws Exception {
        File targetDirectory = Files.resolve(getBaseDirectory(), getTargetDirectory());
        PatternFileSelector existingFileSelector = new PatternFileSelector(targetDirectory, Collections.singleton("**/*"), null, null);
        Set<File> obsoleteFileSet = new HashSet<File>(existingFileSelector.getRelativeFileSet(err));
        long timestamp = getArchive().lastModified();
        Map<File, File> fileMap = getFileMap();
        ZipFile zipFile = null;
        try {
            if (err.isEnabled(Severity.DEBUG)) {
                err.debug("Processing zip file '%s'.", getArchive());
            }
            zipFile = new ZipFile(getArchive());
            for (Enumeration<? extends ZipEntry> i = zipFile.entries(); i.hasMoreElements(); ) {
                ZipEntry zipEntry = i.nextElement();
                File entryFile = new File(zipEntry.getName());
                if (!zipEntry.isDirectory() && fileMap.containsKey(entryFile)) {
                    File target = fileMap.get(entryFile);
                    File normalizedTarget = Files.child(targetDirectory, target);
                    if (normalizedTarget == null) {
                        err.warn(new DefaultProblem(null, Severity.WARNING, null, "Skipping illegal target '%s' for entry '%s'.", target, zipEntry.getName()));
                    } else {
                        try {
                            File file = new File(targetDirectory, normalizedTarget.getPath());
                            extractFile(err, zipFile, zipEntry, file);
                            obsoleteFileSet.remove(normalizedTarget);
                        } catch (IOException e) {
                            err.error(e);
                        }
                    }
                }
            }
        } catch (ZipException e) {
            err.error(e);
        } catch (IOException e) {
            err.error(e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    err.error(e);
                }
            }
        }
        List<File> obsoleteFileList = new ArrayList<File>(obsoleteFileSet);
        Collections.sort(obsoleteFileList, Objects.reverseComparator());
        for (File file : obsoleteFileList) {
            File f = new File(targetDirectory, file.getPath());
            if (f.isDirectory() && f.listFiles().length > 0) {
                f.setLastModified(timestamp);
            } else {
                if (err.isEnabled(Severity.DEBUG)) {
                    err.debug(String.format("Removing '%s'.", file));
                }
                f.delete();
            }
        }
    }
