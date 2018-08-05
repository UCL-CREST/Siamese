    private static boolean moveFiles(String sourceDir, String targetDir) {
        boolean isFinished = false;
        boolean fileMoved = false;
        File stagingDir = new File(sourceDir);
        if (!stagingDir.exists()) {
            System.out.println(getTimeStamp() + "ERROR - source directory does not exist.");
            return true;
        }
        if (stagingDir.listFiles() == null) {
            System.out.println(getTimeStamp() + "ERROR - Empty file list. Possible permission error on source directory " + sourceDir);
            return true;
        }
        File[] fileList = stagingDir.listFiles();
        for (int x = 0; x < fileList.length; x++) {
            File f = fileList[x];
            if (f.getName().startsWith(".")) {
                continue;
            }
            String targetFileName = targetDir + File.separator + f.getName();
            String operation = "move";
            boolean success = f.renameTo(new File(targetFileName));
            if (success) {
                fileMoved = true;
            } else {
                operation = "mv";
                try {
                    Process process = Runtime.getRuntime().exec(new String[] { "mv", f.getCanonicalPath(), targetFileName });
                    process.waitFor();
                    process.destroy();
                    if (!new File(targetFileName).exists()) {
                        success = false;
                    } else {
                        success = true;
                        fileMoved = true;
                    }
                } catch (Exception e) {
                    success = false;
                }
                if (!success) {
                    operation = "copy";
                    FileChannel in = null;
                    FileChannel out = null;
                    try {
                        in = new FileInputStream(f).getChannel();
                        File outFile = new File(targetFileName);
                        out = new FileOutputStream(outFile).getChannel();
                        in.transferTo(0, in.size(), out);
                        in.close();
                        in = null;
                        out.close();
                        out = null;
                        f.delete();
                        success = true;
                    } catch (Exception e) {
                        success = false;
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e) {
                            }
                        }
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
            if (success) {
                System.out.println(getTimeStamp() + operation + " " + f.getAbsolutePath() + " to " + targetDir);
                fileMoved = true;
            } else {
                System.out.println(getTimeStamp() + "ERROR - " + operation + " " + f.getName() + " to " + targetFileName + " failed.");
                isFinished = true;
            }
        }
        if (fileMoved && !isFinished) {
            try {
                currentLastActivity = System.currentTimeMillis();
                updateLastActivity(currentLastActivity);
            } catch (NumberFormatException e) {
                System.out.println(getTimeStamp() + "ERROR: NumberFormatException when trying to update lastActivity.");
                isFinished = true;
            } catch (IOException e) {
                System.out.println(getTimeStamp() + "ERROR: IOException when trying to update lastActivity. " + e.toString());
                isFinished = true;
            }
        }
        return isFinished;
    }
