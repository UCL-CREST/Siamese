        public boolean deleteRecursive(File path) {
            if (!path.exists()) {
                return false;
            }
            boolean ret = true;
            if (path.isDirectory()) {
                for (File f : path.listFiles()) {
                    ret = ret && deleteRecursive(f);
                }
            }
            return ret && path.delete();
        }
