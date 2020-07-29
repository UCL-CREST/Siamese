    public void sendDirAllFilesToStream(String FilesDirStr, OutputStream httpout, Hashtable newNamesTable) throws Exception {
        if (!FilesDirStr.endsWith(sep)) {
            FilesDirStr += sep;
        }
        if (null == newNamesTable) {
            newNamesTable = new Hashtable();
        }
        FileUtils.getInstance().createDirectory(FilesDirStr);
        BufferedInputStream in = null;
        byte dataBuff[] = new byte[bufferSize];
        File filesDir = new File(FilesDirStr + ".");
        if ((filesDir.exists()) && (filesDir.isDirectory())) {
            String fileList[] = filesDir.list();
            if (fileList.length > 0) {
                ZipOutputStream outStream = new ZipOutputStream(httpout);
                for (int pos = 0; pos < fileList.length; pos++) {
                    if (!new File(FilesDirStr + fileList[pos]).isDirectory()) {
                        String oldName = fileList[pos];
                        String oldFileName = FilesDirStr + oldName;
                        String newName = oldName;
                        if (newNamesTable.containsKey(oldName)) {
                            newName = (String) newNamesTable.get(oldName);
                            if ((newName == null) && ("".equals(newName))) {
                                newName = oldName;
                            }
                        }
                        in = new BufferedInputStream(new FileInputStream(oldFileName), bufferSize);
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
                }
                outStream.finish();
                httpout.flush();
            } else {
                throw new Exception("FilesDirStr is empty !");
            }
        }
    }
