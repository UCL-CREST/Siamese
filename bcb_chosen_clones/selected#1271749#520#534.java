    public static void copyDirectory(final File sourceDir, final File targetDir) throws IOException {
        if (sourceDir.isDirectory()) {
            if (!targetDir.exists()) {
                if (!targetDir.mkdir()) {
                    throw new IOException("Directory " + sourceDir.getAbsolutePath() + " could not be created.");
                }
            }
            final String[] children = sourceDir.list();
            for (final String element : children) {
                copyDirectory(new File(sourceDir, element), new File(targetDir, element));
            }
        } else {
            copyFile(sourceDir, targetDir);
        }
    }
