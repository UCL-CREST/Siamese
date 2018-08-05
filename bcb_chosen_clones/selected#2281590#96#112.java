    private static void copyFile(File sourceFile, File destFile) throws IOException {
        System.out.println(sourceFile.getAbsolutePath());
        System.out.println(destFile.getAbsolutePath());
        FileChannel source = new FileInputStream(sourceFile).getChannel();
        try {
            FileChannel destination = new FileOutputStream(destFile).getChannel();
            try {
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (destination != null) {
                    destination.close();
                }
            }
        } finally {
            source.close();
        }
    }
