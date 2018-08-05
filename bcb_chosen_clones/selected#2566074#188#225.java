    protected final boolean deleteRecursiveDir(String path) {
        if (path == null) {
            return true;
        }
        boolean retour = true;
        File directory = new File(path);
        if (!directory.exists()) {
            directory = null;
            return true;
        }
        if (!directory.isDirectory()) {
            directory = null;
            return false;
        }
        File[] list = directory.listFiles();
        if ((list == null) || (list.length == 0)) {
            list = null;
            retour = directory.delete();
            directory = null;
            return retour;
        }
        int len = list.length;
        for (int i = 0; i < len; i++) {
            if (list[i].isDirectory()) {
                if (!deleteRecursiveFileDir(list[i])) {
                    retour = false;
                }
            } else {
                retour = false;
            }
        }
        list = null;
        if (retour) {
            retour = directory.delete();
        }
        directory = null;
        return retour;
    }
