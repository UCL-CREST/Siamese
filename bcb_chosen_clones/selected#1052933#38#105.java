    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("FileMonitorJob - executing its JOB at " + new Date() + " by " + context.getTrigger().getName());
        FTPClient client = new FTPClient();
        OutputStream outStream = null;
        Calendar filterCal = Calendar.getInstance();
        filterCal.set(Calendar.DAY_OF_MONTH, filterCal.get(Calendar.DAY_OF_MONTH) - 1);
        Date aDayAgo = filterCal.getTime();
        try {
            Session session = CustomSystemSession.create(r);
            client.connect(ftpHostname);
            client.login(ftpUsername, ftpPassword);
            FTPFile[] users = client.listFiles();
            if (users != null) {
                for (FTPFile user : users) {
                    String userName = user.getName();
                    client.changeWorkingDirectory("/" + userName + "/");
                    Node userNode = null;
                    @SuppressWarnings("deprecation") Query query = session.getWorkspace().getQueryManager().createQuery("/jcr:root/users/element(*, user)[\n" + "  @alias = '" + userName.replaceAll("'", "''") + "'\n" + "]\n" + "order by @lastModified descending", Query.XPATH);
                    NodeIterator results = query.execute().getNodes();
                    if (results.hasNext()) {
                        userNode = results.nextNode();
                    } else if (session.getRootNode().hasNode("users/" + userName)) {
                        userNode = session.getRootNode().getNode("users/" + userName);
                    }
                    FTPFile[] experiments = client.listFiles();
                    if (experiments != null && userNode != null) {
                        for (FTPFile experiment : experiments) {
                            String experimentName = experiment.getName();
                            client.changeWorkingDirectory("/" + userName + "/" + experimentName + "/");
                            FTPFile[] datasets = client.listFiles();
                            if (datasets != null) {
                                for (FTPFile dataset : datasets) {
                                    String datasetName = dataset.getName();
                                    client.changeWorkingDirectory("/" + userName + "/" + experimentName + "/" + datasetName + "/");
                                    Date collectionDate = dataset.getTimestamp().getTime();
                                    if (collectionDate.after(aDayAgo)) {
                                        FTPFile[] images = client.listFiles();
                                        if (images != null) {
                                            for (FTPFile image : images) {
                                                processImage(userName, experimentName, datasetName, collectionDate, image, client, userNode, session);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            session.logout();
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
    }
