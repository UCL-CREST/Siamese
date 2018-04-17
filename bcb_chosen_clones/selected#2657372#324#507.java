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
