    public static final boolean checkForUpdate(final String currentVersion, final String updateURL, boolean noLock) throws Exception {
        try {
            final String parentFDTConfDirName = System.getProperty("user.home") + File.separator + ".fdt";
            final String fdtUpdateConfFileName = "update.properties";
            final File confFile = createOrGetRWFile(parentFDTConfDirName, fdtUpdateConfFileName);
            if (confFile != null) {
                long lastCheck = 0;
                Properties updateProperties = new Properties();
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(confFile);
                    updateProperties.load(fis);
                    final String lastCheckProp = (String) updateProperties.get("LastCheck");
                    lastCheck = 0;
                    if (lastCheckProp != null) {
                        try {
                            lastCheck = Long.parseLong(lastCheckProp);
                        } catch (Throwable t) {
                            if (logger.isLoggable(Level.FINE)) {
                                logger.log(Level.FINE, "Got exception parsing LastCheck param", t);
                            }
                            lastCheck = 0;
                        }
                    }
                } catch (Throwable t) {
                    logger.log(Level.WARNING, "Cannot load update properties file: " + confFile, t);
                } finally {
                    closeIgnoringExceptions(fos);
                    closeIgnoringExceptions(fis);
                }
                final long now = System.currentTimeMillis();
                boolean bHaveUpdates = false;
                checkAndSetInstanceID(updateProperties);
                if (lastCheck + FDT.UPDATE_PERIOD < now) {
                    lastCheck = now;
                    try {
                        logger.log("\n\nChecking for remote updates ... This may be disabled using -noupdates flag.");
                        bHaveUpdates = updateFDT(currentVersion, updateURL, false, noLock);
                        if (bHaveUpdates) {
                            logger.log("FDT may be updated using: java -jar fdt.jar -update");
                        } else {
                            if (logger.isLoggable(Level.FINE)) {
                                logger.log(Level.FINE, "No updates available");
                            }
                        }
                    } catch (Throwable t) {
                        if (logger.isLoggable(Level.FINE)) {
                            logger.log(Level.WARNING, "Got exception", t);
                        }
                    }
                    updateProperties.put("LastCheck", "" + now);
                    try {
                        fos = new FileOutputStream(confFile);
                        updateProperties.store(fos, null);
                    } catch (Throwable t1) {
                        logger.log(Level.WARNING, "Cannot store update properties file", t1);
                    } finally {
                        closeIgnoringExceptions(fos);
                    }
                    return bHaveUpdates;
                }
            } else {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, " [ checkForUpdate ] Cannot read or write the update conf file: " + parentFDTConfDirName + File.separator + fdtUpdateConfFileName);
                }
                return false;
            }
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Got exception checking for updates", t);
        }
        return false;
    }
