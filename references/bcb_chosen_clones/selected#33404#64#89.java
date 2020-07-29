    public void write(SpantusBundle bundle, File zipFile) {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            int id = 0;
            for (Entry<String, MarkerSet> markerSetEntry : bundle.getHolder()) {
                for (Marker markerEntry : markerSetEntry.getValue()) {
                    String idStr = markerSetEntry.getKey() + "-" + id++;
                    out.putNextEntry(new ZipEntry(idStr + "-" + SIGNAL_SEGMENT_MARKER));
                    SignalSegment segment = new SignalSegment();
                    segment.setId(idStr);
                    segment.setMarker(markerEntry);
                    Map<String, IValues> features = extractorInputReaderService.findAllVectorValuesForMarker(bundle.getReader(), markerEntry);
                    segment.putAll(features);
                    signalSegmentDao.write(segment, out);
                }
            }
            out.putNextEntry(new ZipEntry(BUNDLE_FILE_SAMPLE));
            readerDao.write(bundle.getReader(), out);
            out.putNextEntry(new ZipEntry(BUNDLE_FILE_MARKER));
            markerDao.write(bundle.getHolder(), out);
            out.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
