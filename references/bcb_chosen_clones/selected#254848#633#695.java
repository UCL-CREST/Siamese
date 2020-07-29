    private String copy(PluginVersionDetail usePluginVersion, File runtimeRepository) {
        try {
            File tmpFile = null;
            try {
                tmpFile = File.createTempFile("tmpPlugin_", ".zip");
                tmpFile.deleteOnExit();
                URL url = new URL(usePluginVersion.getUri());
                String destFilename = new File(url.getFile()).getName();
                File destFile = new File(runtimeRepository, destFilename);
                InputStream in = null;
                FileOutputStream out = null;
                int bytesDownload = 0;
                long startTime = 0;
                long endTime = 0;
                try {
                    URLConnection urlConnection = url.openConnection();
                    bytesDownload = urlConnection.getContentLength();
                    in = urlConnection.getInputStream();
                    out = new FileOutputStream(tmpFile);
                    startTime = System.currentTimeMillis();
                    IOUtils.copy(in, out);
                    endTime = System.currentTimeMillis();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }
                String downloadSpeedInfo = null;
                long downloadSpeed = 0;
                if ((endTime - startTime) > 0) {
                    downloadSpeed = 1000L * bytesDownload / (endTime - startTime);
                }
                if (downloadSpeed == 0) {
                    downloadSpeedInfo = "? B/s";
                } else if (downloadSpeed < 1000) {
                    downloadSpeedInfo = downloadSpeed + " B/s";
                } else if (downloadSpeed < 1000000) {
                    downloadSpeedInfo = downloadSpeed / 1000 + " KB/s";
                } else if (downloadSpeed < 1000000000) {
                    downloadSpeedInfo = downloadSpeed / 1000000 + " MB/s";
                } else {
                    downloadSpeedInfo = downloadSpeed / 1000000000 + " GB/s";
                }
                String tmpFileMessageDigest = getMessageDigest(tmpFile.toURI().toURL()).getValue();
                if (!tmpFileMessageDigest.equals(usePluginVersion.getMessageDigest().getValue())) {
                    throw new RuntimeException("Downloaded file: " + usePluginVersion.getUri() + " does not have required message digest: " + usePluginVersion.getMessageDigest().getValue());
                }
                if (!isNoop()) {
                    FileUtils.copyFile(tmpFile, destFile);
                }
                return bytesDownload + " Bytes " + downloadSpeedInfo;
            } finally {
                if (tmpFile != null) {
                    tmpFile.delete();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not download " + usePluginVersion.getUri() + " to " + runtimeRepository, ex);
        }
    }
