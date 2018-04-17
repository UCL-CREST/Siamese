    private void copy(File source, File destination) {
        if (!destination.exists()) {
            destination.mkdir();
        }
        File files[] = source.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    copy(files[i], new File(destination, files[i].getName()));
                } else {
                    try {
                        FileChannel srcChannel = new FileInputStream(files[i]).getChannel();
                        FileChannel dstChannel = new FileOutputStream(new File(destination, files[i].getName())).getChannel();
                        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                        srcChannel.close();
                        dstChannel.close();
                    } catch (IOException ioe) {
                        log.error("Could not write to " + destination.getAbsolutePath(), ioe);
                    }
                }
            }
        }
    }
