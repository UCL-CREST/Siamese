    private ArrayList<PlaylistSearchAlgorithm> initializeSearchAlgorithms(PlaylistCriteria criteria, ArrayList<SongTrack> musicTitles, SongTrackEntityManager stdbm2, double targetCost) {
        ArrayList<PlaylistSearchAlgorithm> algorithms = new ArrayList<PlaylistSearchAlgorithm>();
        try {
            for (Class<?> a : searchAlgorithmsToRun) {
                Constructor<?> algorithmConstructor = a.getConstructor(PlaylistCriteria.class, ArrayList.class, SongTrackEntityManager.class, Double.class);
                algorithms.add((PlaylistSearchAlgorithm) algorithmConstructor.newInstance(criteria, musicTitles, stdbm, targetCost));
            }
        } catch (Exception exception) {
            reportError(exception, true);
        }
        return algorithms;
    }
