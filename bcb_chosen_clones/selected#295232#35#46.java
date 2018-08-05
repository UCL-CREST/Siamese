    private static AtomContainer askForMovieSettings() throws IOException, QTException {
        final InputStream inputStream = QuickTimeFormatGenerator.class.getResourceAsStream(REFERENCE_MOVIE_RESOURCE);
        final ByteArrayOutputStream byteArray = new ByteArrayOutputStream(1024 * 100);
        IOUtils.copy(inputStream, byteArray);
        final byte[] movieBytes = byteArray.toByteArray();
        final QTHandle qtHandle = new QTHandle(movieBytes);
        final DataRef dataRef = new DataRef(qtHandle, StdQTConstants.kDataRefFileExtensionTag, ".mov");
        final Movie movie = Movie.fromDataRef(dataRef, StdQTConstants.newMovieActive | StdQTConstants4.newMovieAsyncOK);
        final MovieExporter exporter = new MovieExporter(StdQTConstants.kQTFileTypeMovie);
        exporter.doUserDialog(movie, null, 0, movie.getDuration());
        return exporter.getExportSettingsFromAtomContainer();
    }
