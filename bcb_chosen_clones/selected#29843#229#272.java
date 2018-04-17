    public List<SuspectFileProcessingStatus> retrieve() throws Exception {
        BufferedOutputStream bos = null;
        try {
            String listFilePath = GeneralUtils.generateAbsolutePath(getDownloadDirectoryPath(), getListName(), "/");
            listFilePath = listFilePath.concat(".xml");
            if (!new File(getDownloadDirectoryPath()).exists()) {
                FileUtils.forceMkdir(new File(getDownloadDirectoryPath()));
            }
            FileOutputStream listFileOutputStream = new FileOutputStream(listFilePath);
            bos = new BufferedOutputStream(listFileOutputStream);
            InputStream is = null;
            if (getUseProxy()) {
                is = URLUtils.getResponse(getUrl(), getUserName(), getPassword(), URLUtils.HTTP_GET_METHOD, getProxyHost(), getProxyPort());
                IOUtils.copyLarge(is, bos);
            } else {
                URLUtils.getResponse(getUrl(), getUserName(), getPassword(), bos, null);
            }
            bos.flush();
            bos.close();
            File listFile = new File(listFilePath);
            if (!listFile.exists()) {
                throw new IllegalStateException("The list file did not get created");
            }
            if (isLoggingInfo()) {
                logInfo("Downloaded list file : " + listFile);
            }
            List<SuspectFileProcessingStatus> sfpsList = new ArrayList<SuspectFileProcessingStatus>();
            String loadType = GeneralConstants.LOAD_TYPE_FULL;
            String feedType = GeneralConstants.EMPTY_TOKEN;
            String listName = getListName();
            String errorCode = "";
            String description = "";
            SuspectFileProcessingStatus sfps = getSuspectsLoaderService().storeFileIntoListIncomingDir(listFile, loadType, feedType, listName, errorCode, description);
            sfpsList.add(sfps);
            if (isLoggingInfo()) {
                logInfo("Retrieved list file with SuspectFileProcessingStatus: " + sfps);
            }
            return sfpsList;
        } finally {
            if (null != bos) {
                bos.close();
            }
        }
    }
