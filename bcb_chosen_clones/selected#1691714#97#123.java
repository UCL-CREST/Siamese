    public static boolean copy(File fromFile, File toFile) throws IOException {
        if (fromFile == null || toFile == null) {
            return false;
        }
        if (!fromFile.exists()) {
            return false;
        }
        if ((fromFile.isDirectory() && toFile.isFile()) || (fromFile.isFile() && toFile.isDirectory())) {
            return false;
        }
        create(toFile);
        if (fromFile.isDirectory()) {
            File subFiles[] = fromFile.listFiles();
            for (File sub : subFiles) {
                String name = sub.getName();
                File goalFile = new File(toFile, name);
                if (sub.isFile()) {
                    copyFile(sub, goalFile);
                } else {
                    copy(sub, goalFile);
                }
            }
        } else {
            copyFile(fromFile, toFile);
        }
        return true;
    }
