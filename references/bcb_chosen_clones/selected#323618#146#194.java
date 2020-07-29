    public boolean addFileAffix(String filename) {
        log.debug("增加附件..");
        if (filename.equals("") || filename == null) {
            return false;
        }
        String file[];
        file = filename.split(";");
        log.debug("你有 " + file.length + " 个附件!");
        try {
            for (int i = 0; i < file.length; i++) {
                String appendFileName = file[i];
                File f = new File(appendFileName);
                if (f.length() / 1024 > 1500) {
                    appendFileName = appendFileName + ".zip";
                    new File(appendFileName).createNewFile();
                    File zipFile = new File(appendFileName);
                    BufferedInputStream in = null;
                    ZipOutputStream zipOut = null;
                    try {
                        in = new BufferedInputStream(new FileInputStream(f));
                        zipOut = new ZipOutputStream(new BufferedOutputStream(new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32()), 4096));
                        ZipEntry entry = new ZipEntry(f.getName());
                        zipOut.putNextEntry(entry);
                        byte[] b = new byte[4096];
                        int count = -1;
                        while ((count = in.read(b)) != -1) {
                            zipOut.write(b, 0, count);
                        }
                        zipOut.closeEntry();
                    } catch (Exception ex) {
                        log.error(ex);
                        continue;
                    } finally {
                        if (in != null) in.close();
                        if (zipOut != null) zipOut.close();
                    }
                }
                BodyPart bp = new MimeBodyPart();
                FileDataSource fileds = new FileDataSource(appendFileName);
                bp.setDataHandler(new DataHandler(fileds));
                bp.setFileName(fileds.getName());
                mp.addBodyPart(bp);
            }
            return true;
        } catch (Exception e) {
            log.error("增加附件: " + filename + "--faild!" + e);
            return false;
        }
    }
