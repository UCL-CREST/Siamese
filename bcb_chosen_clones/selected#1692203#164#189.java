    @Override
    public Resource createNew(String name, InputStream in, Long length, String contentType) throws IOException {
        File dest = new File(this.realFile, name);
        if (allowedClient) {
            if (".request".equals(name) || ".tokens".equals(name)) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(dest);
                    IOUtils.copy(in, out);
                } finally {
                    IOUtils.closeQuietly(out);
                }
                if (".request".equals(name)) {
                    File request = new File(realFile.getAbsolutePath() + "/" + name);
                    RequestManager.manageRequest(request, null, true);
                    return new OverEncryptedFriendsFile(factory, folderPath + "/.response", allowedClient);
                }
                return new OverEncryptedFriendsFile(factory, folderPath + "/" + name, allowedClient);
            } else {
                return null;
            }
        } else {
            LOGGER.error("User isn't owner of this folder");
            return null;
        }
    }
