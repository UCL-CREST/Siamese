    public static boolean copyFile(final File inFile, final File outFile) {
        try {
            FileChannel ic = new FileInputStream(inFile).getChannel();
            FileChannel oc = new FileOutputStream(outFile).getChannel();
            ic.transferTo(0, ic.size(), oc);
            ic.close();
            oc.close();
            return true;
        } catch (IOException e) {
            SystemUtils.LOG.log(Level.INFO, "SystemUtils.copyFile() Exception while copy file " + inFile.getAbsolutePath() + " to " + outFile.getAbsolutePath(), e);
            return false;
        }
    }
