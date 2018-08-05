        private boolean deleteDirectory(File dir) {
            if (dir == null || !dir.exists() || !dir.isDirectory()) return false;
            boolean success = true;
            File[] list = dir.listFiles();
            for (File file : list) {
                if (file.isDirectory()) {
                    if (!deleteDirectory(file)) success = false;
                } else {
                    if (!file.delete()) success = false;
                }
            }
            if (!dir.delete()) success = false;
            return success;
        }
