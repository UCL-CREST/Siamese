    public static boolean copyFile(File soureFile, File destFile) {
        boolean copySuccess = false;
        if (soureFile != null && destFile != null && soureFile.exists()) {
            try {
                new File(destFile.getParent()).mkdirs();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destFile));
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(soureFile));
                for (int currentByte = in.read(); currentByte != -1; currentByte = in.read()) out.write(currentByte);
                in.close();
                out.close();
                copySuccess = true;
            } catch (Exception e) {
                copySuccess = false;
            }
        }
        return copySuccess;
    }
