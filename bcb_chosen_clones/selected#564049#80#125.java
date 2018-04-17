    @Override
    public DownloadingItem download(Playlist playlist, String title, File folder, StopDownloadCondition condition, String uuid) throws IOException, StoreStateException {
        boolean firstIteration = true;
        Iterator<PlaylistEntry> entries = playlist.getEntries().iterator();
        DownloadingItem prevItem = null;
        File[] previousDownloadedFiles = new File[0];
        while (entries.hasNext()) {
            PlaylistEntry entry = entries.next();
            DownloadingItem item = null;
            LOGGER.info("Downloading from '" + entry.getTitle() + "'");
            InputStream is = RESTHelper.inputStream(entry.getUrl());
            boolean stopped = false;
            File nfile = null;
            try {
                nfile = createFileStream(folder, entry);
                item = new DownloadingItem(nfile, uuid.toString(), title, entry, new Date(), getPID(), condition);
                if (previousDownloadedFiles.length > 0) {
                    item.setPreviousFiles(previousDownloadedFiles);
                }
                addItem(item);
                if (prevItem != null) deletePrevItem(prevItem);
                prevItem = item;
                stopped = IOUtils.copyStreams(is, new FileOutputStream(nfile), condition);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                radioScheduler.fireException(e);
                if (!condition.isStopped()) {
                    File[] nfiles = new File[previousDownloadedFiles.length + 1];
                    System.arraycopy(previousDownloadedFiles, 0, nfiles, 0, previousDownloadedFiles.length);
                    nfiles[nfiles.length - 1] = item.getFile();
                    previousDownloadedFiles = nfiles;
                    if ((!entries.hasNext()) && (firstIteration)) {
                        firstIteration = false;
                        entries = playlist.getEntries().iterator();
                    }
                    continue;
                }
            }
            if (stopped) {
                item.setState(ProcessStates.STOPPED);
                this.radioScheduler.fireStopDownloading(item);
                return item;
            }
        }
        return null;
    }
