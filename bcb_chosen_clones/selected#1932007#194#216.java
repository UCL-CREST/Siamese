    private FileInputStream getPackageStream(String archivePath) throws IOException, PackageManagerException {
        final int lastSlashInName = filename.lastIndexOf("/");
        final String newFileName = filename.substring(lastSlashInName);
        File packageFile = new File((new StringBuilder()).append(archivePath).append(newFileName).toString());
        if (null != packageFile) return new FileInputStream(packageFile);
        if (null != packageURL) {
            final InputStream urlStream = new ConnectToServer(null).getInputStream(packageURL);
            packageFile = new File((new StringBuilder()).append(getName()).append(".deb").toString());
            final OutputStream fileStream = new FileOutputStream(packageFile);
            final byte buffer[] = new byte[10240];
            for (int read = 0; (read = urlStream.read(buffer)) > 0; ) fileStream.write(buffer, 0, read);
            urlStream.close();
            fileStream.close();
            return new FileInputStream(packageFile);
        } else {
            final String errorMessage = PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("package.getPackageStream.packageURLIsNull", "No entry found for package.getPackageStream.packageURLIsNull");
            if (pm != null) {
                pm.addWarning(errorMessage);
                logger.error(errorMessage);
            } else logger.error(errorMessage);
            throw new FileNotFoundException();
        }
    }
