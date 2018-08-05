    public static boolean copy(File from, File to) {
        if (from.isDirectory()) {
            for (String name : Arrays.asList(from.list())) {
                if (!copy(from, to, name)) {
                    LogUtils.info("Failed to copy " + name + " from " + from + " to " + to, null);
                    return false;
                }
            }
        } else {
            try {
                FileInputStream is = new FileInputStream(from);
                FileChannel ifc = is.getChannel();
                FileOutputStream os = makeFile(to);
                if (USE_NIO) {
                    FileChannel ofc = os.getChannel();
                    ofc.transferFrom(ifc, 0, from.length());
                } else {
                    pipe(is, os, false);
                }
                is.close();
                os.close();
            } catch (IOException ex) {
                LogUtils.warning("Failed to copy " + from + " to " + to, ex);
                return false;
            }
        }
        long time = from.lastModified();
        setLastModified(to, time);
        long newtime = to.lastModified();
        if (newtime != time) {
            LogUtils.info("Failed to set timestamp for file " + to + ": tried " + new Date(time) + ", have " + new Date(newtime), null);
            to.setLastModified(time);
            long morenewtime = to.lastModified();
            return false;
        }
        return time == newtime;
    }
