    public static void copyDirectory(File sourceLocation, File targetLocation) {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {
            try {
                copyFile(sourceLocation, targetLocation);
            } catch (IOException e) {
                throw new GeneratorException("Error: Copying the directory/file: " + sourceLocation.getAbsolutePath(), e);
            }
        }
    }
