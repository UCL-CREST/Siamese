    public void sync() {
        logger.info("start");
        try {
            logger.info("checking working copy...");
            File wcdir = new File(workingCopy);
            String[] filenames = wcdir.list();
            if ((filenames != null) && (filenames.length > 0)) {
                logger.info("updating working copy...");
                createManager();
                SVNUpdateClient updateClient = clientManager.getUpdateClient();
                updateClient.setIgnoreExternals(false);
                long rev = updateClient.doUpdate(wcdir, SVNRevision.HEAD, SVNDepth.INFINITY, true, true);
                logger.info("at revision " + rev);
            } else {
                logger.info("checking out working copy...");
                createManager();
                SVNUpdateClient updateClient = clientManager.getUpdateClient();
                updateClient.setIgnoreExternals(false);
                SVNURL url = SVNURL.parseURIEncoded(repositoryPath);
                long rev = updateClient.doCheckout(url, wcdir, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, true);
                logger.info("at revision " + rev);
            }
            DataFetcher fetcher = new DataFetcher(dataSource.getConnection());
            int resultsFetched = 0;
            Vector<HashMap<String, String>> resultsData = new Vector<HashMap<String, String>>();
            fetcher.open(query);
            ArrayList<File> commitFiles = new ArrayList<File>();
            ArrayList<File> addFiles = new ArrayList<File>();
            prepeareSourceSQL();
            SVNWCClient wcClient = clientManager.getWCClient();
            do {
                resultsData = fetcher.fetch(500);
                resultsFetched = resultsData.size();
                for (HashMap<String, String> m : resultsData) {
                    String name = m.get(nameColumn);
                    String type = m.get(typeColumn);
                    String owner = m.get(ownerColumn);
                    String date = m.get(dateColumn);
                    DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    long datemills = formatter.parse(date).getTime();
                    File wcfile = getWCFile(type, name);
                    boolean mustBeCommited = false;
                    if (!wcfile.exists() || (wcfile.lastModified() < datemills)) {
                        boolean isAdd = !wcfile.exists();
                        String source = getSource(owner, type, name);
                        if (!isAdd) {
                            byte[] s1 = source.getBytes();
                            byte[] s2 = getStoredBytes(wcfile);
                            CRC32 c1 = new CRC32();
                            c1.update(s1);
                            CRC32 c2 = new CRC32();
                            c2.update(s2);
                            long cs1 = c1.getValue();
                            long cs2 = c2.getValue();
                            if (cs1 != cs2) {
                                storeSource(wcfile, source);
                                commitFiles.add(wcfile);
                                mustBeCommited = true;
                                logger.info("updated file \"" + wcfile.getName() + "\" (" + type + ")");
                            } else logger.info("file \"" + wcfile.getName() + "\" (" + type + ") is out of date, but it wasn't changed");
                        } else {
                            storeSource(wcfile, source);
                            addFiles.add(wcfile);
                            commitFiles.add(wcfile);
                            mustBeCommited = true;
                            logger.info("created file \"" + wcfile.getName() + "\" (" + type + ")");
                        }
                        String plist = "";
                        for (String field : m.keySet()) {
                            try {
                                if (field.toLowerCase().startsWith("p$")) {
                                    String propertyName = field.substring(2);
                                    String propertyValue = m.get(field);
                                    SVNPropertyData pdata = wcClient.doGetProperty(wcfile, propertyName, SVNRevision.HEAD, SVNRevision.HEAD);
                                    String storedPropertyValue = SVNPropertyValue.getPropertyAsString(pdata.getValue());
                                    if (!storedPropertyValue.equals(propertyValue)) {
                                        wcClient.doSetProperty(wcfile, propertyName, SVNPropertyValue.create(propertyValue), false, SVNDepth.INFINITY, null, null);
                                        plist += propertyName + "=" + propertyValue + ", ";
                                    }
                                }
                            } catch (SVNException se) {
                                logger.error("property processing error for file \"" + wcfile.getName() + "\" (" + type + "), class: " + se.getClass().getName() + ", message:" + se.getMessage());
                            }
                        }
                        if (!plist.isEmpty()) {
                            plist = plist.substring(0, plist.length() - 2);
                            if (!mustBeCommited) commitFiles.add(wcfile);
                            logger.info("property processing for file \"" + wcfile.getName() + "\" (" + type + ") (" + plist + ")");
                        }
                    }
                }
            } while (resultsFetched >= 500);
            File[] commitFilesArray = new File[commitFiles.size()];
            commitFilesArray = commitFiles.toArray(commitFilesArray);
            File[] addFilesArray = new File[addFiles.size()];
            addFilesArray = addFiles.toArray(addFilesArray);
            logger.info("adding new files...");
            wcClient.doAdd(addFilesArray, false, false, true, SVNDepth.INFINITY, false, false, true);
            logger.info("combining commit packet...");
            SVNCommitClient commitClient = clientManager.getCommitClient();
            SVNCommitPacket commitPacket = commitClient.doCollectCommitItems(commitFilesArray, false, false, SVNDepth.INFINITY, null);
            logger.info("commiting...");
            commitClient.doCommit(commitPacket, false, "dbcode-svn-sync bot revision, sync-object \"" + name + "\"");
            logger.info("commited (" + commitFilesArray.length + " file(s) total, " + addFilesArray.length + " new file(s))");
        } catch (Exception e) {
            logger.fatal("sync exception, class: " + e.getClass().getName() + ", message: " + e.getMessage());
        } finally {
            closeSourceSQL();
        }
        logger.info("end");
    }
