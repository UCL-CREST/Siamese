    public static boolean filecopy(final File source, final File target) {
        boolean out = false;
        if (source.isDirectory() || !source.exists() || target.isDirectory() || source.equals(target)) return false;
        try {
            target.getParentFile().mkdirs();
            target.createNewFile();
            FileChannel sourceChannel = new FileInputStream(source).getChannel();
            try {
                FileChannel targetChannel = new FileOutputStream(target).getChannel();
                try {
                    targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                    out = true;
                } finally {
                    targetChannel.close();
                }
            } finally {
                sourceChannel.close();
            }
        } catch (IOException e) {
            out = false;
        }
        return out;
    }
