    public boolean download(String url) {
        HttpGet httpGet = new HttpGet(url);
        String filename = FileUtils.replaceNonAlphanumericCharacters(url);
        String completePath = directory + File.separatorChar + filename;
        int retriesLeft = MAX_RETRIES;
        while (retriesLeft > 0) {
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    logger.info("Downloading file from " + url + " -> " + completePath);
                    IOUtils.copy(resEntity.getContent(), new FileOutputStream(completePath));
                    logger.info("File " + filename + " was downloaded successfully.");
                    setFileSize(new File(completePath).length());
                    setFilename(filename);
                    return true;
                } else {
                    logger.warn("Trouble downloading file from " + url + ". Status was: " + response.getStatusLine());
                }
            } catch (ClientProtocolException e) {
                logger.error("Protocol error. This is probably serious, and there's no need " + "to continue trying to download this file.", e);
                return false;
            } catch (IOException e) {
                logger.warn("IO trouble: " + e.getMessage() + ". Retries left: " + retriesLeft);
            }
            retriesLeft--;
        }
        return false;
    }
