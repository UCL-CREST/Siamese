    public static Fetcher create(final URL url) throws Exception {
        final Class<? extends Fetcher> resourceClass = Fetcher.fetcherTypes.get(url.getProtocol().toLowerCase());
        if (resourceClass == null) return new NullFetcher(url);
        return resourceClass.getConstructor(URL.class).newInstance(url);
    }
