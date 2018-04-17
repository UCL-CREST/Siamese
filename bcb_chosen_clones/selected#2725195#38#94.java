    protected void unZip() throws PersistenceException {
        boolean newZip = false;
        try {
            if (null == backup) {
                mode = (String) context.get(Context.MODE);
                if (null == mode) mode = Context.MODE_NAME_RESTORE;
                backupDirectory = (File) context.get(Context.BACKUP_DIRECTORY);
                logger.debug("Got backup directory {" + backupDirectory.getAbsolutePath() + "}");
                if (!backupDirectory.exists() && mode.equals(Context.MODE_NAME_BACKUP)) {
                    newZip = true;
                    backupDirectory.mkdirs();
                } else if (!backupDirectory.exists()) {
                    throw new PersistenceException("Backup directory {" + backupDirectory.getAbsolutePath() + "} does not exist.");
                }
                backup = new File(backupDirectory + "/" + getBackupName() + ".zip");
                logger.debug("Got zip file {" + backup.getAbsolutePath() + "}");
            }
            File _explodedDirectory = File.createTempFile("exploded-" + backup.getName() + "-", ".zip");
            _explodedDirectory.mkdirs();
            _explodedDirectory.delete();
            backupDirectory = new File(_explodedDirectory.getParentFile(), _explodedDirectory.getName());
            backupDirectory.mkdirs();
            logger.debug("Created exploded directory {" + backupDirectory.getAbsolutePath() + "}");
            if (!backup.exists() && mode.equals(Context.MODE_NAME_BACKUP)) {
                newZip = true;
                backup.createNewFile();
            } else if (!backup.exists()) {
                throw new PersistenceException("Backup file {" + backup.getAbsolutePath() + "} does not exist.");
            }
            if (newZip) return;
            ZipFile zip = new ZipFile(backup);
            Enumeration zipFileEntries = zip.entries();
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                logger.debug("Inflating: " + entry);
                File destFile = new File(backupDirectory, currentEntry);
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                if (!entry.isDirectory()) {
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = zip.getInputStream(entry);
                        out = new FileOutputStream(destFile);
                        IOUtils.copy(in, out);
                    } finally {
                        if (null != out) out.close();
                        if (null != in) in.close();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Unable to unzip {" + backup + "}", e);
            throw new PersistenceException(e);
        }
    }
