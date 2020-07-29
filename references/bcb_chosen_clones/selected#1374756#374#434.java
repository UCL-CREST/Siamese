    private void performFileStream(StreamService streamService, final StreamSession session, SubMonitor subMonitor, int numberOfFilesToSend) throws SarosCancellationException {
        OutputStream output = session.getOutputStream(0);
        ZipOutputStream zout = new ZipOutputStream(output);
        int worked = 0;
        int lastWorked = 0;
        int filesSent = 0;
        double increment = 0.0;
        if (numberOfFilesToSend >= 1) {
            increment = (double) 100 / numberOfFilesToSend;
            subMonitor.beginTask("Streaming files...", 100);
        } else {
            subMonitor.worked(100);
        }
        try {
            for (String projectID : this.projectFilesToSend.keySet()) {
                List<IPath> toSend = this.projectFilesToSend.get(projectID);
                zout = new ZipOutputStream(output);
                for (IPath path : toSend) {
                    IFile file = sarosSession.getProject(projectID).getFile(path);
                    String absPath = file.getLocation().toPortableString();
                    byte[] buffer = new byte[streamService.getChunkSize()[0]];
                    InputStream input = new FileInputStream(absPath);
                    ZipEntry ze = new ZipEntry(path.toPortableString());
                    ze.setExtra(projectID.getBytes());
                    zout.putNextEntry(ze);
                    int numRead = 0;
                    while ((numRead = input.read(buffer)) > 0) {
                        zout.write(buffer, 0, numRead);
                    }
                    input.close();
                    zout.flush();
                    zout.closeEntry();
                    worked = (int) Math.round(increment * filesSent);
                    if ((worked - lastWorked) > 0) {
                        subMonitor.worked((worked - lastWorked));
                        lastWorked = worked;
                    }
                    filesSent++;
                    checkCancellation(CancelOption.NOTIFY_PEER);
                }
            }
        } catch (IOException e) {
            error = true;
            log.error("Error while sending file: ", e);
            localCancel("An I/O problem occurred while the project's files were being sent: \"" + e.getMessage() + "\" The invitation was cancelled.", CancelOption.NOTIFY_PEER);
            executeCancellation();
        } catch (SarosCancellationException e) {
            log.debug("Invitation process was cancelled.");
        } catch (Exception e) {
            log.error("Unknown exception: ", e);
        } finally {
            try {
                if (filesSent >= 1) zout.finish();
            } catch (IOException e) {
                log.warn("IOException occurred when finishing the ZipOutputStream.");
            }
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(zout);
        }
        subMonitor.done();
    }
