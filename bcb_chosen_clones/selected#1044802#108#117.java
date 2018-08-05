    public LinkedInApiClient createLinkedInApiClient(Class<? extends LinkedInApiClient> implClass, LinkedInAccessToken accessToken) {
        validateAccessToken(accessToken);
        try {
            final LinkedInApiClient client = implClass.getConstructor(String.class, String.class).newInstance(apiConsumer.getConsumerKey(), apiConsumer.getConsumerSecret());
            client.setAccessToken(accessToken);
            return client;
        } catch (Exception e) {
            throw new LinkedInApiClientException(e);
        }
    }
