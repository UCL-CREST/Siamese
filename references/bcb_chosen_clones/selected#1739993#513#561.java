    public void copyHashAllFilesToDirectory(String baseDirStr, Hashtable newNamesTable, String destDirStr) throws Exception {
        if (baseDirStr.endsWith(sep)) {
            baseDirStr = baseDirStr.substring(0, baseDirStr.length() - 1);
        }
        if (destDirStr.endsWith(sep)) {
            destDirStr = destDirStr.substring(0, destDirStr.length() - 1);
        }
        FileUtils.getInstance().createDirectory(baseDirStr);
        if (null == newNamesTable) {
            newNamesTable = new Hashtable();
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        byte dataBuff[] = new byte[bufferSize];
        File baseDir = new File(baseDirStr);
        baseDir.mkdirs();
        if ((baseDir.exists()) && (baseDir.isDirectory())) {
            if (!newNamesTable.isEmpty()) {
                Enumeration enumFiles = newNamesTable.keys();
                while (enumFiles.hasMoreElements()) {
                    String newName = (String) enumFiles.nextElement();
                    String oldPathName = (String) newNamesTable.get(newName);
                    if ((newName != null) && (!"".equals(newName)) && (oldPathName != null) && (!"".equals(oldPathName))) {
                        String newPathFileName = destDirStr + sep + newName;
                        String oldPathFileName = baseDirStr + sep + oldPathName;
                        if (oldPathName.startsWith(sep)) {
                            oldPathFileName = baseDirStr + oldPathName;
                        }
                        File f = new File(oldPathFileName);
                        if ((f.exists()) && (f.isFile())) {
                            in = new BufferedInputStream(new FileInputStream(oldPathFileName), bufferSize);
                            out = new BufferedOutputStream(new FileOutputStream(newPathFileName), bufferSize);
                            int readLen;
                            while ((readLen = in.read(dataBuff)) > 0) {
                                out.write(dataBuff, 0, readLen);
                            }
                            out.flush();
                            in.close();
                            out.close();
                        } else {
                        }
                    }
                }
            } else {
            }
        } else {
            throw new Exception("Base (baseDirStr) dir not exist !");
        }
    }
