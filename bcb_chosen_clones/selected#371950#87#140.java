    private void zipAndSendFiles(File[] logFiles) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EmailSender emailSender = new EmailSender();
        try {
            File globalZipFile = new File("XeptoTileServer_Statistics_" + sdf.format(new Date()) + ".zip");
            ZipOutputStream globalZos = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(globalZipFile), new Adler32()));
            BufferedOutputStream globalOut = new BufferedOutputStream(globalZos);
            globalZos.setComment("Statistics for all users from the XeptoTileServer.");
            File[] zipFiles = new File[usernames.length];
            ZipOutputStream[] zipOutputStreams = new ZipOutputStream[usernames.length];
            BufferedOutputStream[] bufferedZipStreams = new BufferedOutputStream[usernames.length];
            for (File logFile : logFiles) {
                int i;
                for (i = 0; i < usernames.length; i++) {
                    if (logFile.getName().substring(27, 28 + usernames[i].length()).equalsIgnoreCase(usernames[i] + "_")) break;
                }
                BufferedReader in = new BufferedReader(new FileReader(logFile.getPath()));
                ZipEntry entry = new ZipEntry(logFile.getName());
                globalZos.putNextEntry(entry);
                if (i < usernames.length && emails[i] != null) {
                    if (zipFiles[i] == null) {
                        zipFiles[i] = new File("XeptoTileServer_Statistics_" + usernames[i] + "_" + sdf.format(new Date()) + ".zip");
                        zipOutputStreams[i] = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(zipFiles[i]), new Adler32()));
                        bufferedZipStreams[i] = new BufferedOutputStream(zipOutputStreams[i]);
                        zipOutputStreams[i].setComment("Statistics for username " + usernames[i] + " from the XeptoTileServer.");
                    }
                    zipOutputStreams[i].putNextEntry(entry);
                    int c;
                    while ((c = in.read()) != -1) {
                        globalOut.write(c);
                        bufferedZipStreams[i].write(c);
                    }
                } else {
                    int c;
                    while ((c = in.read()) != -1) globalOut.write(c);
                }
                in.close();
                logFile.delete();
            }
            globalOut.close();
            emailSender.send(java.util.ResourceBundle.getBundle("logsender_conf").getString("e-mail"), globalZipFile);
            globalZipFile.delete();
            for (int i = 0; i < zipFiles.length; i++) {
                if (zipFiles[i] != null) {
                    bufferedZipStreams[i].close();
                    emailSender.send(emails[i], zipFiles[i]);
                    zipFiles[i].delete();
                }
            }
        } catch (IOException ex) {
            System.out.println("Cannot create zip file.");
            ex.printStackTrace();
        }
    }
