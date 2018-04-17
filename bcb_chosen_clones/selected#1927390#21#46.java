    public static void copy(File sourceFile, File destinationFile) {
        try {
            if (((sourceFile == null) && (destinationFile == null)) || ((sourceFile == null) || (destinationFile == null))) {
                System.out.println("sourceFile & destinationFile is null");
                System.exit(-1);
            }
            if (sourceFile.isDirectory()) {
                File[] tmp = sourceFile.listFiles();
                File f;
                for (int i = 0; i < tmp.length; i++) {
                    f = new File(destinationFile.getAbsolutePath() + File.separator + tmp[i].getName());
                    f.getParentFile().mkdirs();
                    copy(tmp[i], f);
                }
            } else {
                System.out.println("\nCopy from: " + sourceFile + "\n\n     to: " + destinationFile);
                FileChannel source = new FileInputStream(sourceFile).getChannel();
                FileChannel destination = new FileOutputStream(destinationFile).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
