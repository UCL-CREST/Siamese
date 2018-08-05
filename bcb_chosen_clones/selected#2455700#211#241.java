    public static boolean copy(File source_, File destination_, FileFilter filter_) throws IOException {
        boolean copy = false;
        Validate.notNull(source_);
        Validate.notNull(destination_);
        Validate.isTrue(source_.exists());
        if (source_.isFile()) {
            if (!destination_.exists()) {
                copyFile(source_, destination_);
                copy = true;
            }
        } else if (source_.isDirectory()) {
            if (!destination_.exists()) {
                copy = destination_.mkdirs();
                if (copy) {
                    File[] contents = source_.listFiles(filter_);
                    if ((contents != null) && (contents.length > 0)) {
                        for (int i = 0; i < contents.length; i++) {
                            File newSourceFile = contents[i];
                            String sourceName = newSourceFile.getName();
                            File newDestinationFile = new File(destination_, sourceName);
                            copy = copy(newSourceFile, newDestinationFile, filter_);
                            if (!copy) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return copy;
    }
