    protected File downloadFile(String href) {
        Map<String, File> currentDownloadDirMap = downloadedFiles.get(downloadDir);
        if (currentDownloadDirMap != null) {
            File downloadedFile = currentDownloadDirMap.get(href);
            if (downloadedFile != null) {
                return downloadedFile;
            }
        } else {
            downloadedFiles.put(downloadDir, new HashMap<String, File>());
            currentDownloadDirMap = downloadedFiles.get(downloadDir);
        }
        URL url;
        File result;
        try {
            FilesystemUtils.forceMkdirIfNotExists(downloadDir);
            url = generateUrl(href);
            result = createUniqueFile(downloadDir, href);
        } catch (IOException e) {
            LOG.warn("Failed to create file for download", e);
            return null;
        }
        currentDownloadDirMap.put(href, result);
        LOG.info("Downloading " + url);
        try {
            IOUtils.copy(url.openStream(), new FileOutputStream(result));
        } catch (IOException e) {
            LOG.warn("Failed to download file " + url);
        }
        return result;
    }
