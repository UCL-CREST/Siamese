    private Fetcher createFetcher(PropertyCollection model) {
        Fetcher newFetcher = null;
        try {
            newFetcher = (Fetcher) fetcherClass.getConstructor(null).newInstance(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        newFetcher.configure(getConfiguration());
        newFetcher.setModel(model);
        modelFetcherMap.put(model, newFetcher);
        return newFetcher;
    }
