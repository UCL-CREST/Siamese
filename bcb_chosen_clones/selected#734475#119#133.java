        public static void deleteDirectory(File dir, boolean recurse) {
            File[] files = dir.listFiles();
            if (recurse) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file, true);
                    }
                }
            }
            for (File file : files) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
