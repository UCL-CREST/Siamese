    private static FileEntry writeEntry(Zip64File zip64File, FileEntry targetPath, File toWrite, boolean compress) {
        InputStream in = null;
        EntryOutputStream out = null;
        processAndCreateFolderEntries(zip64File, parseTargetPath(targetPath.getName(), toWrite), compress);
        try {
            if (!compress) {
                out = zip64File.openEntryOutputStream(targetPath.getName(), FileEntry.iMETHOD_STORED, getFileDate(toWrite));
            } else {
                out = zip64File.openEntryOutputStream(targetPath.getName(), FileEntry.iMETHOD_DEFLATED, getFileDate(toWrite));
            }
            if (!targetPath.isDirectory()) {
                in = new FileInputStream(toWrite);
                IOUtils.copyLarge(in, out);
                in.close();
            }
            out.flush();
            out.close();
            if (targetPath.isDirectory()) {
                log.info("[createZip] Written folder entry to zip: " + targetPath.getName());
            } else {
                log.info("[createZip] Written file entry to zip: " + targetPath.getName());
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (ZipException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return targetPath;
    }
