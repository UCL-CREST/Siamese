                    public void handle() {
                        FileChannel srcChannel, destChannel;
                        String destOutFile = logFile + "." + System.currentTimeMillis();
                        String destOutFileCompressed = logFile + "." + System.currentTimeMillis() + ".gz";
                        if (rotateDest != null) {
                            (new File(rotateDest)).mkdirs();
                            if (destOutFile.indexOf("/") != -1) {
                                destOutFile = rotateDest + "/" + destOutFile.substring(destOutFile.lastIndexOf("/") + 1);
                            }
                            if (destOutFileCompressed.indexOf("/") != -1) {
                                destOutFileCompressed = rotateDest + "/" + destOutFileCompressed.substring(destOutFileCompressed.lastIndexOf("/") + 1);
                            }
                        }
                        if (rotateCompress) {
                            try {
                                GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(destOutFileCompressed));
                                FileInputStream in = new FileInputStream(logFile);
                                byte buf[] = new byte[1024];
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                }
                                in.close();
                                out.finish();
                                out.close();
                                buf = null;
                                in = null;
                                out = null;
                                Debug.debug("Rotated log file '" + logFile + "' to '" + destOutFileCompressed + "'");
                            } catch (Exception e) {
                                Debug.debug("Unable to rotate log file '" + logFile + "': " + e);
                            }
                        } else {
                            try {
                                srcChannel = new FileInputStream(logFile).getChannel();
                            } catch (IOException e) {
                                Debug.debug("Unable to read log file '" + logFile + "': " + e.getMessage());
                                return;
                            }
                            try {
                                destChannel = new FileOutputStream(destOutFile).getChannel();
                            } catch (IOException e) {
                                Debug.debug("Unable to rotate log file to '" + destOutFile + "': " + e.getMessage());
                                return;
                            }
                            try {
                                destChannel.transferFrom(srcChannel, 0, srcChannel.size());
                                srcChannel.close();
                                destChannel.close();
                                srcChannel = null;
                                destChannel = null;
                            } catch (IOException e) {
                                Debug.debug("Unable to copy data for file rotation: " + e.getMessage());
                                return;
                            }
                            Debug.debug("Rotated log file '" + logFile + "' to '" + destOutFile + "'");
                        }
                        if (rotateDelete && isFile) {
                            try {
                                ps.close();
                            } catch (Exception e) {
                            }
                            isFile = false;
                            ps = null;
                            (new File(logFile)).delete();
                            reconfigureDebug();
                        }
                        if (rotateDest != null) {
                            long comparisonTime = rotateDays * (60 * 60 * 24 * 1000);
                            long currentTime = System.currentTimeMillis();
                            File fileList[] = (new File(rotateDest)).listFiles();
                            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date date = new java.util.Date(currentTime);
                            String archiveFile = format1.format(date).toString() + ".zip";
                            if (rotateArchive != null) {
                                archiveFile = rotateArchive + "/" + archiveFile;
                                (new File(rotateArchive)).mkdirs();
                            }
                            Archive archive = new Archive(archiveFile);
                            for (int i = 0; i < fileList.length; i++) {
                                String currentFilename = fileList[i].getName();
                                long timeDifference = (currentTime - fileList[i].lastModified());
                                if ((rotateCompress && currentFilename.endsWith(".gz")) || (!rotateCompress && currentFilename.indexOf(logFile + ".") != -1)) {
                                    if (rotateDest != null) {
                                        currentFilename = rotateDest + "/" + currentFilename;
                                    }
                                    if (timeDifference > comparisonTime) {
                                        archive.addFile(fileList[i].getName(), currentFilename);
                                        fileList[i].delete();
                                    }
                                }
                            }
                            archive = null;
                            fileList = null;
                            format1 = null;
                            date = null;
                        }
                    }
