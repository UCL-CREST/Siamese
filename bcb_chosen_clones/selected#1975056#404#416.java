        protected static boolean purge(File file) {
            File[] dir;
            int i;
            if (file.isDirectory()) {
                dir = file.listFiles();
                for (i = 0; i < dir.length; i++) {
                    if (!purge(dir[i])) {
                        log2_.warning("Failed deleting '" + dir[i] + "'");
                    }
                }
            }
            return file.delete();
        }
