        private void zipLog(InfoRolling info) {
            boolean zipped = false;
            File[] logFiles = info.getFiles();
            try {
                GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
                gc.roll(Calendar.DAY_OF_MONTH, info.getBackRolling());
                final String prefixFileName = logFileName.substring(0, logFileName.indexOf("."));
                final String date = sdf.format(gc.getTime());
                String tarName = new StringBuffer(prefixFileName).append(date).append(".tar").toString();
                String gzipFileName = new StringBuffer(tarName).append(".zip").toString();
                String tarPath = new StringBuffer(logDir).append(File.separator).append(tarName).toString();
                TarArchive ta = new TarArchive(new FileOutputStream(tarPath));
                for (int i = 0; i < logFiles.length; i++) {
                    File file = logFiles[i];
                    TarEntry te = new TarEntry(file);
                    ta.writeEntry(te, true);
                }
                ta.closeArchive();
                ZipEntry zipEntry = new ZipEntry(tarName);
                zipEntry.setMethod(ZipEntry.DEFLATED);
                ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(new StringBuffer(logDir).append(File.separator).append(gzipFileName).toString()));
                zout.putNextEntry(zipEntry);
                InputStream in = new FileInputStream(tarPath);
                byte[] buffer = new byte[2048];
                int ch = 0;
                while ((ch = in.read(buffer)) >= 0) {
                    zout.write(buffer, 0, ch);
                }
                zout.flush();
                zout.close();
                in.close();
                logFiles = new File(getFile().substring(0, getFile().lastIndexOf(File.separator))).listFiles(new FileFilter() {

                    public boolean accept(File file) {
                        return file.getName().endsWith(".tar");
                    }
                });
                for (int i = 0; i < logFiles.length; i++) {
                    File file = logFiles[i];
                    System.out.println("cancello : " + file.getAbsolutePath() + " : " + file.delete());
                }
                logFiles = new File(getFile().substring(0, getFile().lastIndexOf(File.separator))).listFiles(new FileFilter() {

                    public boolean accept(File file) {
                        return file.getName().indexOf(prefixFileName + ".log" + date) != -1 && !file.getName().endsWith(".zip");
                    }
                });
                for (int i = 0; i < logFiles.length; i++) {
                    File file = logFiles[i];
                    System.out.println("cancello : " + file.getAbsolutePath() + " : " + file.delete());
                }
                zipped = true;
            } catch (FileNotFoundException ex) {
                LogLog.error("Filenotfound: " + ex.getMessage(), ex);
            } catch (IOException ex) {
                LogLog.error("IOException: " + ex.getMessage(), ex);
            } finally {
                if (zipped) {
                    for (int i = 0; i < logFiles.length; i++) {
                        File file = logFiles[i];
                        file.delete();
                    }
                }
            }
        }
