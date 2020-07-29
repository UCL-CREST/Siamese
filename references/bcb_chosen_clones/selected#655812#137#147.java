    public static void exportGestureSet(List<GestureSet> sets, File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(exportGestureSetsAsStream(sets), outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Could not export Gesture Sets. Export File not found.", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not export Gesture Sets.", e);
        }
    }
