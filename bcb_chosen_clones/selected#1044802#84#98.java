    @SuppressWarnings("unchecked")
    public LinkedInApiClient createLinkedInApiClient(LinkedInAccessToken accessToken) {
        validateAccessToken(accessToken);
        try {
            if (defaultClientImpl == null) {
                Class<? extends LinkedInApiClient> clazz = (Class<? extends LinkedInApiClient>) Class.forName(ApplicationConstants.CLIENT_DEFAULT_IMPL);
                defaultClientImpl = clazz.getConstructor(String.class, String.class);
            }
            final LinkedInApiClient client = defaultClientImpl.newInstance(apiConsumer.getConsumerKey(), apiConsumer.getConsumerSecret());
            client.setAccessToken(accessToken);
            return client;
        } catch (Exception e) {
            throw new LinkedInApiClientException(e);
        }
    }
