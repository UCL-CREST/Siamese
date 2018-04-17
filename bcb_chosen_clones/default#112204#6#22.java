    public static boolean kill(File prey, FileFilter filter) throws IOException {
        if (prey.isFile() && filter.accept(prey)) {
            return prey.delete();
        } else if (prey.isDirectory()) {
            File[] children = prey.listFiles();
            if (children != null) for (File subPrey : children) {
                if (!kill(subPrey, filter)) {
                    java.util.logging.Logger.global.info("Killing spree stops on " + subPrey.getCanonicalPath());
                    return false;
                }
            }
            if (filter.accept(prey)) {
                return prey.delete();
            }
        }
        return true;
    }
