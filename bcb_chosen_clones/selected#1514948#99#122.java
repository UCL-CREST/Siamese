    private void copy(final File file) throws IOException {
        String targetFilename = targetFilename(file);
        FileInputStream fis = new FileInputStream(file);
        try {
            FileChannel source = fis.getChannel();
            try {
                FileOutputStream fos = new FileOutputStream(targetFilename);
                try {
                    FileChannel target = fos.getChannel();
                    try {
                        target.transferFrom(source, 0, source.size());
                    } finally {
                        target.close();
                    }
                } finally {
                    fos.close();
                }
            } finally {
                source.close();
            }
        } finally {
            fis.close();
        }
    }
