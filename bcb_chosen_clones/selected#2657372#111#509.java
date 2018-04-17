    public void launchJob(final String workingDir, final AppConfigType appConfig) throws FaultType {
        logger.info("called for job: " + jobID);
        MessageContext mc = MessageContext.getCurrentContext();
        HttpServletRequest req = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
        String clientDN = (String) req.getAttribute(GSIConstants.GSI_USER_DN);
        if (clientDN != null) {
            logger.info("Client's DN: " + clientDN);
        } else {
            clientDN = "Unknown client";
        }
        String remoteIP = req.getRemoteAddr();
        SOAPService service = mc.getService();
        String serviceName = service.getName();
        if (serviceName == null) {
            serviceName = "Unknown service";
        }
        if (appConfig.isParallel()) {
            if (AppServiceImpl.drmaaInUse) {
                if (AppServiceImpl.drmaaPE == null) {
                    logger.error("drmaa.pe property must be specified in opal.properties " + "for parallel execution using DRMAA");
                    throw new FaultType("drmaa.pe property must be specified in opal.properties " + "for parallel execution using DRMAA");
                }
                if (AppServiceImpl.mpiRun == null) {
                    logger.error("mpi.run property must be specified in opal.properties " + "for parallel execution using DRMAA");
                    throw new FaultType("mpi.run property must be specified in " + "opal.properties for parallel execution " + "using DRMAA");
                }
            } else if (!AppServiceImpl.globusInUse) {
                if (AppServiceImpl.mpiRun == null) {
                    logger.error("mpi.run property must be specified in opal.properties " + "for parallel execution without using Globus");
                    throw new FaultType("mpi.run property must be specified in " + "opal.properties for parallel execution " + "without using Globus");
                }
            }
            if (jobIn.getNumProcs() == null) {
                logger.error("Number of processes unspecified for parallel job");
                throw new FaultType("Number of processes unspecified for parallel job");
            } else if (jobIn.getNumProcs().intValue() > AppServiceImpl.numProcs) {
                logger.error("Processors required - " + jobIn.getNumProcs() + ", available - " + AppServiceImpl.numProcs);
                throw new FaultType("Processors required - " + jobIn.getNumProcs() + ", available - " + AppServiceImpl.numProcs);
            }
        }
        try {
            status.setCode(GramJob.STATUS_PENDING);
            status.setMessage("Launching executable");
            status.setBaseURL(new URI(AppServiceImpl.tomcatURL + jobID));
        } catch (MalformedURIException mue) {
            logger.error("Cannot convert base_url string to URI - " + mue.getMessage());
            throw new FaultType("Cannot convert base_url string to URI - " + mue.getMessage());
        }
        if (!AppServiceImpl.dbInUse) {
            AppServiceImpl.statusTable.put(jobID, status);
        } else {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(AppServiceImpl.dbUrl, AppServiceImpl.dbUser, AppServiceImpl.dbPasswd);
            } catch (SQLException e) {
                logger.error("Cannot connect to database - " + e.getMessage());
                throw new FaultType("Cannot connect to database - " + e.getMessage());
            }
            String time = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.US).format(new Date());
            String sqlStmt = "insert into job_status(job_id, code, message, base_url, " + "client_dn, client_ip, service_name, start_time, last_update) " + "values ('" + jobID + "', " + status.getCode() + ", " + "'" + status.getMessage() + "', " + "'" + status.getBaseURL() + "', " + "'" + clientDN + "', " + "'" + remoteIP + "', " + "'" + serviceName + "', " + "'" + time + "', " + "'" + time + "');";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sqlStmt);
                conn.close();
            } catch (SQLException e) {
                logger.error("Cannot insert job status into database - " + e.getMessage());
                throw new FaultType("Cannot insert job status into database - " + e.getMessage());
            }
        }
        String args = appConfig.getDefaultArgs();
        if (args == null) {
            args = jobIn.getArgList();
        } else {
            String userArgs = jobIn.getArgList();
            if (userArgs != null) args += " " + userArgs;
        }
        if (args != null) {
            args = args.trim();
        }
        logger.debug("Argument list: " + args);
        if (AppServiceImpl.drmaaInUse) {
            String cmd = null;
            String[] argsArray = null;
            if (appConfig.isParallel()) {
                cmd = "/bin/sh";
                String newArgs = AppServiceImpl.mpiRun + " -machinefile $TMPDIR/machines" + " -np " + jobIn.getNumProcs() + " " + appConfig.getBinaryLocation();
                if (args != null) {
                    args = newArgs + " " + args;
                } else {
                    args = newArgs;
                }
                logger.debug("CMD: " + args);
                argsArray = new String[] { "-c", args };
            } else {
                cmd = appConfig.getBinaryLocation();
                if (args == null) args = "";
                logger.debug("CMD: " + cmd + " " + args);
                argsArray = args.split(" ");
            }
            try {
                logger.debug("Working directory: " + workingDir);
                JobTemplate jt = session.createJobTemplate();
                if (appConfig.isParallel()) jt.setNativeSpecification("-pe " + AppServiceImpl.drmaaPE + " " + jobIn.getNumProcs());
                jt.setRemoteCommand(cmd);
                jt.setArgs(argsArray);
                jt.setJobName(jobID);
                jt.setWorkingDirectory(workingDir);
                jt.setErrorPath(":" + workingDir + "/stderr.txt");
                jt.setOutputPath(":" + workingDir + "/stdout.txt");
                drmaaJobID = session.runJob(jt);
                logger.info("DRMAA job has been submitted with id " + drmaaJobID);
                session.deleteJobTemplate(jt);
            } catch (Exception ex) {
                logger.error(ex);
                status.setCode(GramJob.STATUS_FAILED);
                status.setMessage("Error while running executable via DRMAA - " + ex.getMessage());
                if (AppServiceImpl.dbInUse) {
                    try {
                        updateStatusInDatabase(jobID, status);
                    } catch (SQLException e) {
                        logger.error(e);
                        throw new FaultType("Cannot update status into database - " + e.getMessage());
                    }
                }
                return;
            }
            status.setCode(GramJob.STATUS_ACTIVE);
            status.setMessage("Execution in progress");
            if (AppServiceImpl.dbInUse) {
                try {
                    updateStatusInDatabase(jobID, status);
                } catch (SQLException e) {
                    logger.error(e);
                    throw new FaultType("Cannot update status into database - " + e.getMessage());
                }
            }
        } else if (AppServiceImpl.globusInUse) {
            String rsl = null;
            if (appConfig.isParallel()) {
                rsl = "&(directory=" + workingDir + ")" + "(executable=" + appConfig.getBinaryLocation() + ")" + "(count=" + jobIn.getNumProcs() + ")" + "(jobtype=mpi)" + "(stdout=stdout.txt)" + "(stderr=stderr.txt)";
            } else {
                rsl = "&(directory=" + workingDir + ")" + "(executable=" + appConfig.getBinaryLocation() + ")" + "(stdout=stdout.txt)" + "(stderr=stderr.txt)";
            }
            if (args != null) {
                args = "\"" + args + "\"";
                args = args.replaceAll("[\\s]+", "\" \"");
                rsl += "(arguments=" + args + ")";
            }
            logger.debug("RSL: " + rsl);
            try {
                job = new GramJob(rsl);
                GlobusCredential globusCred = new GlobusCredential(AppServiceImpl.serviceCertPath, AppServiceImpl.serviceKeyPath);
                GSSCredential gssCred = new GlobusGSSCredentialImpl(globusCred, GSSCredential.INITIATE_AND_ACCEPT);
                job.setCredentials(gssCred);
                job.addListener(this);
                job.request(AppServiceImpl.gatekeeperContact);
            } catch (Exception ge) {
                logger.error(ge);
                status.setCode(GramJob.STATUS_FAILED);
                status.setMessage("Error while running executable via Globus - " + ge.getMessage());
                if (AppServiceImpl.dbInUse) {
                    try {
                        updateStatusInDatabase(jobID, status);
                    } catch (SQLException e) {
                        logger.error(e);
                        throw new FaultType("Cannot update status into database - " + e.getMessage());
                    }
                }
                return;
            }
        } else {
            String cmd = null;
            if (appConfig.isParallel()) {
                cmd = new String(AppServiceImpl.mpiRun + " " + "-np " + jobIn.getNumProcs() + " " + appConfig.getBinaryLocation());
            } else {
                cmd = new String(appConfig.getBinaryLocation());
            }
            if (args != null) {
                cmd += " " + args;
            }
            logger.debug("CMD: " + cmd);
            try {
                logger.debug("Working directory: " + workingDir);
                proc = Runtime.getRuntime().exec(cmd, null, new File(workingDir));
                stdoutThread = writeStdOut(proc, workingDir);
                stderrThread = writeStdErr(proc, workingDir);
            } catch (IOException ioe) {
                logger.error(ioe);
                status.setCode(GramJob.STATUS_FAILED);
                status.setMessage("Error while running executable via fork - " + ioe.getMessage());
                if (AppServiceImpl.dbInUse) {
                    try {
                        updateStatusInDatabase(jobID, status);
                    } catch (SQLException e) {
                        logger.error(e);
                        throw new FaultType("Cannot update status into database - " + e.getMessage());
                    }
                }
                return;
            }
            status.setCode(GramJob.STATUS_ACTIVE);
            status.setMessage("Execution in progress");
            if (AppServiceImpl.dbInUse) {
                try {
                    updateStatusInDatabase(jobID, status);
                } catch (SQLException e) {
                    logger.error(e);
                    throw new FaultType("Cannot update status into database - " + e.getMessage());
                }
            }
        }
        new Thread() {

            public void run() {
                try {
                    waitForCompletion();
                } catch (FaultType f) {
                    logger.error(f);
                    synchronized (status) {
                        status.notifyAll();
                    }
                    return;
                }
                if (AppServiceImpl.drmaaInUse || !AppServiceImpl.globusInUse) {
                    done = true;
                    status.setCode(GramJob.STATUS_STAGE_OUT);
                    status.setMessage("Writing output metadata");
                    if (AppServiceImpl.dbInUse) {
                        try {
                            updateStatusInDatabase(jobID, status);
                        } catch (SQLException e) {
                            status.setCode(GramJob.STATUS_FAILED);
                            status.setMessage("Cannot update status database after finish - " + e.getMessage());
                            logger.error(e);
                            synchronized (status) {
                                status.notifyAll();
                            }
                            return;
                        }
                    }
                }
                try {
                    if (!AppServiceImpl.drmaaInUse && !AppServiceImpl.globusInUse) {
                        try {
                            logger.debug("Waiting for all outputs to be written out");
                            stdoutThread.join();
                            stderrThread.join();
                            logger.debug("All outputs successfully written out");
                        } catch (InterruptedException ignore) {
                        }
                    }
                    File stdOutFile = new File(workingDir + File.separator + "stdout.txt");
                    if (!stdOutFile.exists()) {
                        throw new IOException("Standard output missing for execution");
                    }
                    File stdErrFile = new File(workingDir + File.separator + "stderr.txt");
                    if (!stdErrFile.exists()) {
                        throw new IOException("Standard error missing for execution");
                    }
                    if (AppServiceImpl.archiveData) {
                        logger.debug("Archiving output files");
                        File f = new File(workingDir);
                        File[] outputFiles = f.listFiles();
                        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(workingDir + File.separator + jobID + ".zip"));
                        byte[] buf = new byte[1024];
                        try {
                            for (int i = 0; i < outputFiles.length; i++) {
                                FileInputStream in = new FileInputStream(outputFiles[i]);
                                out.putNextEntry(new ZipEntry(outputFiles[i].getName()));
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                }
                                out.closeEntry();
                                in.close();
                            }
                            out.close();
                        } catch (IOException e) {
                            logger.error(e);
                            logger.error("Error not fatal - moving on");
                        }
                    }
                    File f = new File(workingDir);
                    File[] outputFiles = f.listFiles();
                    OutputFileType[] outputFileObj = new OutputFileType[outputFiles.length - 2];
                    int j = 0;
                    for (int i = 0; i < outputFiles.length; i++) {
                        if (outputFiles[i].getName().equals("stdout.txt")) {
                            outputs.setStdOut(new URI(AppServiceImpl.tomcatURL + jobID + "/stdout.txt"));
                        } else if (outputFiles[i].getName().equals("stderr.txt")) {
                            outputs.setStdErr(new URI(AppServiceImpl.tomcatURL + jobID + "/stderr.txt"));
                        } else {
                            OutputFileType next = new OutputFileType();
                            next.setName(outputFiles[i].getName());
                            next.setUrl(new URI(AppServiceImpl.tomcatURL + jobID + "/" + outputFiles[i].getName()));
                            outputFileObj[j++] = next;
                        }
                    }
                    outputs.setOutputFile(outputFileObj);
                } catch (IOException e) {
                    status.setCode(GramJob.STATUS_FAILED);
                    status.setMessage("Cannot retrieve outputs after finish - " + e.getMessage());
                    logger.error(e);
                    if (AppServiceImpl.dbInUse) {
                        try {
                            updateStatusInDatabase(jobID, status);
                        } catch (SQLException se) {
                            logger.error(se);
                        }
                    }
                    synchronized (status) {
                        status.notifyAll();
                    }
                    return;
                }
                if (!AppServiceImpl.dbInUse) {
                    AppServiceImpl.outputTable.put(jobID, outputs);
                } else {
                    Connection conn = null;
                    try {
                        conn = DriverManager.getConnection(AppServiceImpl.dbUrl, AppServiceImpl.dbUser, AppServiceImpl.dbPasswd);
                    } catch (SQLException e) {
                        status.setCode(GramJob.STATUS_FAILED);
                        status.setMessage("Cannot connect to database after finish - " + e.getMessage());
                        logger.error(e);
                        synchronized (status) {
                            status.notifyAll();
                        }
                        return;
                    }
                    String sqlStmt = "insert into job_output(job_id, std_out, std_err) " + "values ('" + jobID + "', " + "'" + outputs.getStdOut().toString() + "', " + "'" + outputs.getStdErr().toString() + "');";
                    Statement stmt = null;
                    try {
                        stmt = conn.createStatement();
                        stmt.executeUpdate(sqlStmt);
                    } catch (SQLException e) {
                        status.setCode(GramJob.STATUS_FAILED);
                        status.setMessage("Cannot update job output database after finish - " + e.getMessage());
                        logger.error(e);
                        try {
                            updateStatusInDatabase(jobID, status);
                        } catch (SQLException se) {
                            logger.error(se);
                        }
                        synchronized (status) {
                            status.notifyAll();
                        }
                        return;
                    }
                    OutputFileType[] outputFile = outputs.getOutputFile();
                    for (int i = 0; i < outputFile.length; i++) {
                        sqlStmt = "insert into output_file(job_id, name, url) " + "values ('" + jobID + "', " + "'" + outputFile[i].getName() + "', " + "'" + outputFile[i].getUrl().toString() + "');";
                        try {
                            stmt = conn.createStatement();
                            stmt.executeUpdate(sqlStmt);
                        } catch (SQLException e) {
                            status.setCode(GramJob.STATUS_FAILED);
                            status.setMessage("Cannot update output_file DB after finish - " + e.getMessage());
                            logger.error(e);
                            try {
                                updateStatusInDatabase(jobID, status);
                            } catch (SQLException se) {
                                logger.error(se);
                            }
                            synchronized (status) {
                                status.notifyAll();
                            }
                            return;
                        }
                    }
                }
                if (terminatedOK()) {
                    status.setCode(GramJob.STATUS_DONE);
                    status.setMessage("Execution complete - " + "check outputs to verify successful execution");
                } else {
                    status.setCode(GramJob.STATUS_FAILED);
                    status.setMessage("Execution failed");
                }
                if (AppServiceImpl.dbInUse) {
                    try {
                        updateStatusInDatabase(jobID, status);
                    } catch (SQLException e) {
                        status.setCode(GramJob.STATUS_FAILED);
                        status.setMessage("Cannot update status database after finish - " + e.getMessage());
                        logger.error(e);
                        synchronized (status) {
                            status.notifyAll();
                        }
                        return;
                    }
                }
                AppServiceImpl.jobTable.remove(jobID);
                synchronized (status) {
                    status.notifyAll();
                }
                logger.info("Execution complete for job: " + jobID);
            }
        }.start();
    }
