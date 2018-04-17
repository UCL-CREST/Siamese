    public void createZips(Collection<String> filesToZip, Project project, String backupFileName) {
        try {
            for (Destination destination : project.getDestinations()) {
                String backupFilePath = destination.getPath();
                if (!backupFilePath.endsWith(File.separator) && !backupFilePath.endsWith("/")) {
                    backupFilePath = backupFilePath + "/";
                }
                backupFilePath = backupFilePath + backupFileName;
                Destination backupDestination = new Destination(destination);
                backupDestination.setPath(backupFilePath);
                OutputStream outputStream = backupDestination.getFileObject().getContent().getOutputStream();
                byte[] buffer = new byte[18024];
                ZipOutputStream out = new ZipOutputStream(outputStream);
                out.setLevel(Deflater.DEFAULT_COMPRESSION);
                for (String filePath : filesToZip) {
                    FileInputStream in = new FileInputStream(filePath);
                    try {
                        out.putNextEntry(new ZipEntry(filePath));
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    } catch (ZipException ze) {
                        ze.printStackTrace();
                        logger.error(ze);
                    }
                    out.closeEntry();
                    in.close();
                }
                out.close();
                sendEmail(project, EmailTrigger.SUCCESS, project.getName() + " backup successful");
            }
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            sendEmail(project, EmailTrigger.FAILURE, project.getName() + " backup failed\n" + iae.getMessage());
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            sendEmail(project, EmailTrigger.FAILURE, project.getName() + " backup failed\n" + fnfe.getMessage());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            sendEmail(project, EmailTrigger.FAILURE, project.getName() + " backup failed\n" + ioe.getMessage());
        }
    }
