    public void copy(final File source, final File dest) throws IOException {
        final FileInputStream in = new FileInputStream(source);
        try {
            final FileOutputStream out = new FileOutputStream(dest);
            try {
                final FileChannel inChannel = in.getChannel();
                final FileChannel outChannel = out.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
