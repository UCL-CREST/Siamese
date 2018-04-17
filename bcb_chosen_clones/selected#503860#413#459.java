    public static void saveFileData(File file, File destination, java.io.File newDataFile) throws Exception {
        String fileName = file.getFileName();
        String assetsPath = FileFactory.getRealAssetsRootPath();
        new java.io.File(assetsPath).mkdir();
        java.io.File workingFile = getAssetIOFile(file);
        DotResourceCache vc = CacheLocator.getVeloctyResourceCache();
        vc.remove(ResourceManager.RESOURCE_TEMPLATE + workingFile.getPath());
        if (destination != null && destination.getInode() > 0) {
            FileInputStream is = new FileInputStream(workingFile);
            FileChannel channelFrom = is.getChannel();
            java.io.File newVersionFile = getAssetIOFile(destination);
            FileChannel channelTo = new FileOutputStream(newVersionFile).getChannel();
            channelFrom.transferTo(0, channelFrom.size(), channelTo);
            channelTo.force(false);
            channelTo.close();
            channelFrom.close();
        }
        if (newDataFile != null) {
            FileChannel writeCurrentChannel = new FileOutputStream(workingFile).getChannel();
            writeCurrentChannel.truncate(0);
            FileChannel fromChannel = new FileInputStream(newDataFile).getChannel();
            fromChannel.transferTo(0, fromChannel.size(), writeCurrentChannel);
            writeCurrentChannel.force(false);
            writeCurrentChannel.close();
            fromChannel.close();
            if (UtilMethods.isImage(fileName)) {
                BufferedImage img = javax.imageio.ImageIO.read(workingFile);
                int height = img.getHeight();
                file.setHeight(height);
                int width = img.getWidth();
                file.setWidth(width);
            }
            String folderPath = workingFile.getParentFile().getAbsolutePath();
            Identifier identifier = IdentifierCache.getIdentifierFromIdentifierCache(file);
            java.io.File directory = new java.io.File(folderPath);
            java.io.File[] files = directory.listFiles((new FileFactory()).new ThumbnailsFileNamesFilter(identifier));
            for (java.io.File iofile : files) {
                try {
                    iofile.delete();
                } catch (SecurityException e) {
                    Logger.error(FileFactory.class, "EditFileAction._saveWorkingFileData(): " + iofile.getName() + " cannot be erased. Please check the file permissions.");
                } catch (Exception e) {
                    Logger.error(FileFactory.class, "EditFileAction._saveWorkingFileData(): " + e.getMessage());
                }
            }
        }
    }
