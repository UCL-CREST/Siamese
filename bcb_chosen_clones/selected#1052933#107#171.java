    public static String harvestForUser(Node userNode, String alias, Boolean all) {
        FTPClient client = new FTPClient();
        OutputStream outStream = null;
        Calendar filterCal = Calendar.getInstance();
        filterCal.set(Calendar.DAY_OF_MONTH, filterCal.get(Calendar.DAY_OF_MONTH) - 1);
        Date aDayAgo = filterCal.getTime();
        String outputRecord = "";
        try {
            Session session = CustomSystemSession.create(r);
            client.connect(ftpHostname);
            client.login(ftpUsername, ftpPassword);
            FTPFile[] users = client.listFiles();
            if (users != null) {
                for (FTPFile user : users) {
                    String userName = user.getName();
                    if (alias.equals(userName)) {
                        outputRecord += "Found account " + userName + ".\n";
                        client.changeWorkingDirectory("/" + userName + "/");
                        FTPFile[] experiments = client.listFiles();
                        if (experiments != null && userNode != null) {
                            for (FTPFile experiment : experiments) {
                                String experimentName = experiment.getName();
                                outputRecord += "Exploring " + userName + "/" + experimentName + ".\n";
                                client.changeWorkingDirectory("/" + userName + "/" + experimentName + "/");
                                FTPFile[] datasets = client.listFiles();
                                if (datasets != null) {
                                    for (FTPFile dataset : datasets) {
                                        String datasetName = dataset.getName();
                                        outputRecord += "Exploring " + userName + "/" + experimentName + "/" + datasetName + ".\n";
                                        client.changeWorkingDirectory("/" + userName + "/" + experimentName + "/" + datasetName + "/");
                                        Date collectionDate = dataset.getTimestamp().getTime();
                                        if (collectionDate.after(aDayAgo) || all) {
                                            FTPFile[] images = client.listFiles();
                                            if (images != null) {
                                                for (FTPFile image : images) {
                                                    outputRecord += processImage(userName, experimentName, datasetName, collectionDate, image, client, userNode, session);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            client.logout();
        } catch (IOException ioe) {
            log.info("Error communicating with FTP server.");
            log.error("Error communicating with FTP server.", ioe);
            ioe.printStackTrace();
        } catch (RepositoryException ioe) {
            log.info("Error communicating with repository.");
            log.error("Error communicating with repository.", ioe);
            ioe.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outStream);
            try {
                client.disconnect();
            } catch (IOException e) {
                log.error("Problem disconnecting from FTP server", e);
            }
        }
        return outputRecord;
    }
