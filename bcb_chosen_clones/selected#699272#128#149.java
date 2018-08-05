    private static void readFileEntry(Zip64File zip64File, FileEntry fileEntry, File destFolder) {
        FileOutputStream fileOut;
        File target = new File(destFolder, fileEntry.getName());
        File targetsParent = target.getParentFile();
        if (targetsParent != null) {
            targetsParent.mkdirs();
        }
        try {
            fileOut = new FileOutputStream(target);
            log.info("[readFileEntry] writing entry: " + fileEntry.getName() + " to file: " + target.getAbsolutePath());
            EntryInputStream entryReader = zip64File.openEntryInputStream(fileEntry.getName());
            IOUtils.copyLarge(entryReader, fileOut);
            entryReader.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ZipException e) {
            log.warning("ATTENTION PLEASE: Some strange, but obviously not serious ZipException occured! Extracted file '" + target.getName() + "' anyway! So don't Panic!" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
