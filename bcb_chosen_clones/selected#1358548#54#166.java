    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        keysString = req.getParameter("resultKeys");
        if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
            archiveHelper.setMode(req.getParameter("mode"));
        }
        SecurityAdvisor secAdvisor = null;
        try {
            secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
            if (secAdvisor != null) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment;filename=microarray.zip");
                response.setHeader("Cache-Control", "max-age=0, must-revalidate");
                long time1 = System.currentTimeMillis();
                Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
                DictionaryHelper dh = DictionaryHelper.getInstance(sess);
                baseDir = dh.getAnalysisReadDirectory(req.getServerName());
                archiveHelper.setTempDir(dh.getPropertyDictionary(PropertyDictionary.TEMP_DIRECTORY));
                Map fileNameMap = new HashMap();
                long compressedFileSizeTotal = getFileNamesToDownload(baseDir, keysString, fileNameMap);
                ZipOutputStream zipOut = null;
                TarArchiveOutputStream tarOut = null;
                if (archiveHelper.isZipMode()) {
                    zipOut = new ZipOutputStream(response.getOutputStream());
                } else {
                    tarOut = new TarArchiveOutputStream(response.getOutputStream());
                }
                int totalArchiveSize = 0;
                for (Iterator i = fileNameMap.keySet().iterator(); i.hasNext(); ) {
                    String analysisNumber = (String) i.next();
                    Analysis analysis = null;
                    List analysisList = sess.createQuery("SELECT a from Analysis a where a.number = '" + analysisNumber + "'").list();
                    if (analysisList.size() == 1) {
                        analysis = (Analysis) analysisList.get(0);
                    }
                    if (analysis == null) {
                        log.error("Unable to find request " + analysisNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
                        continue;
                    }
                    if (!secAdvisor.canRead(analysis)) {
                        log.error("Insufficient permissions to read analysis " + analysisNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
                        continue;
                    }
                    List fileNames = (List) fileNameMap.get(analysisNumber);
                    for (Iterator i1 = fileNames.iterator(); i1.hasNext(); ) {
                        String filename = (String) i1.next();
                        String zipEntryName = "bioinformatics-analysis-" + filename.substring(baseDir.length());
                        archiveHelper.setArchiveEntryName(zipEntryName);
                        TransferLog xferLog = new TransferLog();
                        xferLog.setFileName(filename.substring(baseDir.length() + 5));
                        xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
                        xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
                        xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
                        xferLog.setPerformCompression("Y");
                        xferLog.setIdAnalysis(analysis.getIdAnalysis());
                        xferLog.setIdLab(analysis.getIdLab());
                        InputStream in = archiveHelper.getInputStreamToArchive(filename, zipEntryName);
                        ZipEntry zipEntry = null;
                        if (archiveHelper.isZipMode()) {
                            zipEntry = new ZipEntry(archiveHelper.getArchiveEntryName());
                            zipOut.putNextEntry(zipEntry);
                        } else {
                            TarArchiveEntry entry = new TarArchiveEntry(archiveHelper.getArchiveEntryName());
                            entry.setSize(archiveHelper.getArchiveFileSize());
                            tarOut.putArchiveEntry(entry);
                        }
                        OutputStream out = null;
                        if (archiveHelper.isZipMode()) {
                            out = zipOut;
                        } else {
                            out = tarOut;
                        }
                        int size = archiveHelper.transferBytes(in, out);
                        totalArchiveSize += size;
                        xferLog.setFileSize(new BigDecimal(size));
                        xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
                        sess.save(xferLog);
                        if (archiveHelper.isZipMode()) {
                            zipOut.closeEntry();
                            totalArchiveSize += zipEntry.getCompressedSize();
                        } else {
                            tarOut.closeArchiveEntry();
                            totalArchiveSize += archiveHelper.getArchiveFileSize();
                        }
                        archiveHelper.removeTemporaryFile();
                    }
                    sess.flush();
                }
                response.setContentLength(totalArchiveSize);
                if (archiveHelper.isZipMode()) {
                    zipOut.finish();
                    zipOut.flush();
                } else {
                    tarOut.close();
                    tarOut.flush();
                }
            } else {
                response.setStatus(999);
                System.out.println("DownloadAnalyisFolderServlet: You must have a SecurityAdvisor in order to run this command.");
            }
        } catch (Exception e) {
            response.setStatus(999);
            System.out.println("DownloadAnalyisFolderServlet: An exception occurred " + e.toString());
            e.printStackTrace();
        } finally {
            if (secAdvisor != null) {
                try {
                    secAdvisor.closeHibernateSession();
                } catch (Exception e) {
                }
            }
            archiveHelper.removeTemporaryFile();
        }
    }
