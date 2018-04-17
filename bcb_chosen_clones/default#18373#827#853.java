    public boolean zipDir(String inputDirPath, String dir2zip, ZipOutputStream zos) {
        try {
            File zipDir = new File(inputDirPath);
            String dirList[] = zipDir.list();
            byte readBuffer[] = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String fileDirPath = f.getPath();
                    zipDir(fileDirPath, fileDirPath, zos);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                String filePath = f.getPath();
                ZipEntry anEntry = new ZipEntry(filePath);
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) zos.write(readBuffer, 0, bytesIn);
                fis.close();
            }
            return (true);
        } catch (Exception e) {
            errMsgLog += "Problem zipping to zip file '" + dir2zip + "' in zipDir() failed.";
            lastErrMsgLog = errMsgLog;
            return (false);
        }
    }
