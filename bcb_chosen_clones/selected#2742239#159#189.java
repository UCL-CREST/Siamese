    private RemoteObject createRemoteObject(final VideoEntry videoEntry, final RemoteContainer container) throws RemoteException {
        MessageDigest instance;
        try {
            instance = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RemoteException(StatusCreator.newStatus("Error creating MD5", e));
        }
        StringWriter sw = new StringWriter();
        YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
        if (mediaGroup != null) {
            if (mediaGroup.getDescription() != null) {
                sw.append(mediaGroup.getDescription().getPlainTextContent());
            }
            List<MediaCategory> keywordsGroup = mediaGroup.getCategories();
            StringBuilder sb = new StringBuilder();
            if (keywordsGroup != null) {
                for (MediaCategory mediaCategory : keywordsGroup) {
                    sb.append(mediaCategory.getContent());
                }
            }
        }
        instance.update(sw.toString().getBytes());
        RemoteObject remoteVideo = InfomngmntFactory.eINSTANCE.createRemoteObject();
        remoteVideo.setHash(asHex(instance.digest()));
        remoteVideo.setId(SiteInspector.getId(videoEntry.getHtmlLink().getHref()));
        remoteVideo.setName(videoEntry.getTitle().getPlainText());
        remoteVideo.setRepositoryTypeObjectId(KEY_VIDEO);
        remoteVideo.setWrappedObject(videoEntry);
        setInternalUrl(remoteVideo, container);
        return remoteVideo;
    }
