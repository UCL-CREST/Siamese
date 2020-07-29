    public void transport(File file) throws TransportException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    transport(file);
                }
            } else if (file.isFile()) {
                try {
                    FileChannel inChannel = new FileInputStream(file).getChannel();
                    FileChannel outChannel = new FileOutputStream(getOption("destination")).getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                } catch (IOException e) {
                    log.error("File transfer failed", e);
                }
            }
        }
    }
