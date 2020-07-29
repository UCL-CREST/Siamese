    public static void delete(File dir) {
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.exists()) {
                    if (file.isDirectory()) {
                        LOG.log(Level.FINEST, "Deleting dir-tree {0}", file);
                        delete(file);
                    } else {
                        LOG.log(Level.FINEST, "Deleting file {0}", file);
                        file.delete();
                    }
                }
            }
            if (dir.exists()) {
                LOG.log(Level.FINEST, "Deleting dir {0}", dir);
                dir.delete();
            }
        } else {
            LOG.log(Level.FINEST, "No file {0} to delete", dir);
        }
    }
