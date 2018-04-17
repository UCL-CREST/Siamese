    private boolean deleteFiles(File fileOrDirectory, boolean deleteDir) {
        boolean deleted = true;
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory()) {
                File[] files = fileOrDirectory.listFiles();
                for (File file : files) {
                    deleted = deleteFiles(file, true);
                    if (!deleted) return false;
                }
                if (deleteDir) {
                    deleted = fileOrDirectory.delete();
                    if (!deleted) {
                        Object[] options = { i18n.getString("OK") };
                        Object[] objects = { i18n.getString("UPDATE_COULD_NOT_DELETE") + " " + fileOrDirectory.getAbsoluteFile() + ". " + i18n.getString("UPDATE_ABORT") };
                        JOptionPane.showOptionDialog(null, objects, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                        return false;
                    }
                }
            } else {
                if (fileOrDirectory.getName().equals("xnavigator.jar") || fileOrDirectory.getName().equals("resources.jar")) {
                } else {
                    deleted = fileOrDirectory.delete();
                    if (!deleted) {
                        Object[] options = { i18n.getString("OK") };
                        Object[] objects = { i18n.getString("UPDATE_COULD_NOT_DELETE") + " " + fileOrDirectory.getAbsoluteFile() + ". " + i18n.getString("UPDATE_FILE_IN_USE") + " " + i18n.getString("UPDATE_ABORT") };
                        JOptionPane.showOptionDialog(null, objects, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                        return false;
                    }
                }
            }
        }
        return deleted;
    }
