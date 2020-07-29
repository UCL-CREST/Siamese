    public void sendHashAllFilesToStream(String baseDirStr, OutputStream httpout, Hashtable newNamesTable) throws Exception {
        if (baseDirStr.endsWith(sep)) {
            baseDirStr = baseDirStr.substring(0, baseDirStr.length() - 1);
        }
        FileUtils.getInstance().createDirectory(baseDirStr);
        if (null == newNamesTable) {
            newNamesTable = new Hashtable();
        }
        BufferedInputStream in = null;
        byte dataBuff[] = new byte[bufferSize];
        File baseDir = new File(baseDirStr);
        if ((baseDir.exists()) && (baseDir.isDirectory())) {
            if (!newNamesTable.isEmpty()) {
                ZipOutputStream outStream = new ZipOutputStream(httpout);
                Enumeration enumFiles = newNamesTable.keys();
                while (enumFiles.hasMoreElements()) {
                    String newName = (String) enumFiles.nextElement();
                    String oldPathName = (String) newNamesTable.get(newName);
                    if ((newName != null) && (!"".equals(newName)) && (oldPathName != null) && (!"".equals(oldPathName))) {
                        String oldPathFileName = baseDirStr + sep + oldPathName;
                        if (oldPathName.startsWith(sep)) {
                            oldPathFileName = baseDirStr + oldPathName;
                        }
                        File f = new File(oldPathFileName);
                        if ((f.exists()) && (f.isFile())) {
                            System.out.println("download:" + f.getAbsolutePath());
                            in = new BufferedInputStream(new FileInputStream(oldPathFileName), bufferSize);
                            ZipEntry entry = new ZipEntry(newName);
                            outStream.putNextEntry(entry);
                            int writeLen;
                            while ((writeLen = in.read(dataBuff)) > 0) {
                                outStream.write(dataBuff, 0, writeLen);
                            }
                            outStream.flush();
                            outStream.closeEntry();
                            in.close();
                        }
                    } else {
                        throw new Exception("newNamesTable parameters wrong (null or empty) !");
                    }
                }
                outStream.finish();
                httpout.flush();
            } else {
                throw new Exception("newNamesTable is empty !");
            }
        } else {
            throw new Exception("Base (baseDirStr) dir not exist !");
        }
    }
