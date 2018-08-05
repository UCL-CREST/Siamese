    public static void copy(File src, File dst) throws IOException {
        FileInputStream fIn = null;
        FileOutputStream fOut = null;
        FileChannel sIn = null;
        FileChannel sOut = null;
        try {
            fIn = new FileInputStream(src);
            try {
                fOut = new FileOutputStream(dst);
                try {
                    sIn = fIn.getChannel();
                    sOut = fOut.getChannel();
                    sOut.transferFrom(sIn, 0, sIn.size());
                } finally {
                    if (sIn != null) {
                        sIn.close();
                    }
                    if (sOut != null) {
                        sOut.close();
                    }
                }
            } finally {
                if (fOut != null) {
                    fOut.close();
                }
            }
        } finally {
            if (fIn != null) {
                fIn.close();
            }
        }
    }
