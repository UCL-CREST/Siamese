    public static void copyFile(File source, File target) throws Exception {
        if (source.isDirectory()) {
            if (!target.isDirectory()) {
                target.mkdirs();
            }
            String[] children = source.list();
            for (int i = 0; i < children.length; i++) {
                copyFile(new File(source, children[i]), new File(target, children[i]));
            }
        } else {
            FileChannel inChannel = new FileInputStream(source).getChannel();
            FileChannel outChannel = new FileOutputStream(target).getChannel();
            try {
                int maxCount = (64 * 1024 * 1024) - (32 * 1024);
                long size = inChannel.size();
                long position = 0;
                while (position < size) {
                    position += inChannel.transferTo(position, maxCount, outChannel);
                }
            } catch (IOException e) {
                errorLog("{Malgn.copyFile} " + e.getMessage());
                throw e;
            } finally {
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
            }
        }
    }
