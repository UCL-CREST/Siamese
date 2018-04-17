    public long copyFileWithPaths(String userBaseDir, String sourcePath, String destinPath) throws Exception {
        if (userBaseDir.endsWith(sep)) {
            userBaseDir = userBaseDir.substring(0, userBaseDir.length() - sep.length());
        }
        String file1FullPath = new String();
        if (sourcePath.startsWith(sep)) {
            file1FullPath = new String(userBaseDir + sourcePath);
        } else {
            file1FullPath = new String(userBaseDir + sep + sourcePath);
        }
        String file2FullPath = new String();
        if (destinPath.startsWith(sep)) {
            file2FullPath = new String(userBaseDir + destinPath);
        } else {
            file2FullPath = new String(userBaseDir + sep + destinPath);
        }
        long plussQuotaSize = 0;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        byte dataBuff[] = new byte[bufferSize];
        File fileordir = new File(file1FullPath);
        if (fileordir.exists()) {
            if (fileordir.isFile()) {
                File file2 = new File(file2FullPath);
                if (file2.exists()) {
                    plussQuotaSize -= file2.length();
                    file2.delete();
                }
                FileUtils.getInstance().createDirectory(file2.getParent());
                in = new BufferedInputStream(new FileInputStream(file1FullPath), bufferSize);
                out = new BufferedOutputStream(new FileOutputStream(file2FullPath), bufferSize);
                int readLen;
                while ((readLen = in.read(dataBuff)) > 0) {
                    out.write(dataBuff, 0, readLen);
                    plussQuotaSize += readLen;
                }
                out.flush();
                in.close();
                out.close();
            }
            if (fileordir.isDirectory()) {
                String[] entryList = fileordir.list();
                if (entryList.length > 0) {
                    for (int pos = 0; pos < entryList.length; pos++) {
                        String entryName = entryList[pos];
                        String file1FullPathEntry = new String(file1FullPath.concat(entryList[pos]));
                        String file2FullPathEntry = new String(file2FullPath.concat(entryList[pos]));
                        File file2 = new File(file2FullPathEntry);
                        if (file2.exists()) {
                            plussQuotaSize -= file2.length();
                            file2.delete();
                        }
                        FileUtils.getInstance().createDirectory(file2.getParent());
                        in = new BufferedInputStream(new FileInputStream(file1FullPathEntry), bufferSize);
                        out = new BufferedOutputStream(new FileOutputStream(file2FullPathEntry), bufferSize);
                        int readLen;
                        while ((readLen = in.read(dataBuff)) > 0) {
                            out.write(dataBuff, 0, readLen);
                            plussQuotaSize += readLen;
                        }
                        out.flush();
                        in.close();
                        out.close();
                    }
                }
            }
        } else {
            throw new Exception("Source file or dir not exist ! file1FullPath = (" + file1FullPath + ")");
        }
        return plussQuotaSize;
    }
