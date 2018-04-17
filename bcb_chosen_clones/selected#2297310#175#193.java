    public boolean deleteFileRecursively(File f) {
        boolean b = false;
        try {
            if (!f.exists()) return true;
            this.printlnLog(this.getClass().getName() + ".deleteFileRecursively()", "dir:" + f.getName());
            if (f.isDirectory()) {
                File[] children = f.listFiles();
                for (int i = 0; i < children.length; i++) {
                    this.deleteFileRecursively(children[i]);
                }
            }
            b = f.delete();
            if (b) this.printlnLog(this.getClass().getName() + ".deleteFileRecursively()", "deleting " + f.getName() + " success."); else this.printlnLog(this.getClass().getName() + ".deleteFileRecursively()", "deleting " + f.getName() + " not success.");
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return b;
    }
