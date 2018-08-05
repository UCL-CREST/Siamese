    protected boolean writeFile(Interest outstandingInterest) throws IOException {
        File fileToWrite = ccnNameToFilePath(outstandingInterest.name());
        Log.info("CCNFileProxy: extracted request for file: " + fileToWrite.getAbsolutePath() + " exists? ", fileToWrite.exists());
        if (!fileToWrite.exists()) {
            Log.warning("File {0} does not exist. Ignoring request.", fileToWrite.getAbsoluteFile());
            return false;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileToWrite);
        } catch (FileNotFoundException fnf) {
            Log.warning("Unexpected: file we expected to exist doesn't exist: {0}!", fileToWrite.getAbsolutePath());
            return false;
        }
        CCNTime modificationTime = new CCNTime(fileToWrite.lastModified());
        ContentName versionedName = VersioningProfile.addVersion(new ContentName(_prefix, outstandingInterest.name().postfix(_prefix).components()), modificationTime);
        CCNFileOutputStream ccnout = new CCNFileOutputStream(versionedName, _handle);
        ccnout.addOutstandingInterest(outstandingInterest);
        byte[] buffer = new byte[BUF_SIZE];
        int read = fis.read(buffer);
        while (read >= 0) {
            ccnout.write(buffer, 0, read);
            read = fis.read(buffer);
        }
        fis.close();
        ccnout.close();
        return true;
    }
