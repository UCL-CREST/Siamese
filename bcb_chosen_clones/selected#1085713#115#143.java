    private static boolean copyFiles(final File from, final File to, final Pattern regexp) throws IOException {
        if (from.isDirectory()) {
            if (!to.exists()) {
                to.mkdirs();
            } else {
                assert (to.isDirectory());
            }
            final File[] fromChildren = from.listFiles();
            boolean copiedOneChild = false;
            if (fromChildren != null) {
                for (File nextFrom : fromChildren) {
                    final File nextTo = new File(to, nextFrom.getName());
                    if (copyFiles(nextFrom, nextTo, regexp)) {
                        copiedOneChild = true;
                    }
                }
            }
            if ((regexp != null) && !copiedOneChild) {
                to.delete();
            }
            return copiedOneChild;
        } else {
            if ((regexp == null) || regexp.matcher(from.getName()).matches()) {
                copyFile(from, to);
                return true;
            }
            return false;
        }
    }
