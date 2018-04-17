    public static boolean copy(final File from, final File to) {
        if (from.isDirectory()) {
            to.mkdirs();
            for (final String name : Arrays.asList(from.list())) {
                if (!copy(from, to, name)) {
                    if (COPY_DEBUG) {
                        System.out.println("Failed to copy " + name + " from " + from + " to " + to);
                    }
                    return false;
                }
            }
        } else {
            try {
                final FileInputStream is = new FileInputStream(from);
                final FileChannel ifc = is.getChannel();
                final FileOutputStream os = makeFile(to);
                if (USE_NIO) {
                    final FileChannel ofc = os.getChannel();
                    ofc.transferFrom(ifc, 0, from.length());
                } else {
                    pipe(is, os, false);
                }
                is.close();
                os.close();
            } catch (final IOException ex) {
                if (COPY_DEBUG) {
                    System.out.println("Failed to copy " + from + " to " + to + ": " + ex);
                }
                return false;
            }
        }
        final long time = from.lastModified();
        setLastModified(to, time);
        final long newtime = to.lastModified();
        if (COPY_DEBUG) {
            if (newtime != time) {
                System.out.println("Failed to set timestamp for file " + to + ": tried " + new Date(time) + ", have " + new Date(newtime));
                to.setLastModified(time);
                final long morenewtime = to.lastModified();
                return false;
            } else {
                System.out.println("Timestamp for " + to + " set successfully.");
            }
        }
        return time == newtime;
    }
