    protected boolean deleteRecursiveFileDir(File dir) {
        if (dir == null) {
            return true;
        }
        boolean retour = true;
        if (!dir.exists()) {
            return true;
        }
        File[] list = dir.listFiles();
        if ((list == null) || (list.length == 0)) {
            list = null;
            return dir.delete();
        }
        int len = list.length;
        for (int i = 0; i < len; i++) {
            if (list[i].isDirectory()) {
                if (!deleteRecursiveFileDir(list[i])) {
                    retour = false;
                }
            } else {
                retour = false;
                list = null;
                return retour;
            }
        }
        list = null;
        if (retour) {
            retour = dir.delete();
        }
        return retour;
    }
