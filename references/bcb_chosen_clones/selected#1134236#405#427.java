    protected static void doCopy(String orig, String dest, boolean block, FeedBack feedBack) {
        if (logger.isDebugEnabled()) logger.debug("copy(" + orig + "," + dest + ")");
        File origFile = new File(orig);
        if (feedBack != null) feedBack.nextStep(orig);
        if (!origFile.exists()) throw new RuntimeException("copy: origin " + orig + " does not exists");
        if (origFile.isDirectory()) {
            File destFile = new File(dest);
            destFile.mkdirs();
            if (!destFile.isDirectory()) throw new RuntimeException("can't create :" + destFile + " . while copying from " + orig + " to " + dest);
            String[] fileList = origFile.list();
            for (String entry : fileList) doCopy(orig + File.separator + entry, dest + File.separator + entry, block, feedBack);
        } else {
            try {
                FileInputStream in = new FileInputStream(orig);
                FileOutputStream out = new FileOutputStream(dest);
                copyStream(in, out, 0);
                in.close();
                if (block) closeAndWaitFileOutputStream(out);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
