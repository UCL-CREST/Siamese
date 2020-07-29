    public static void copyFile(File from, File to) throws IOException {
        assert (from != null);
        assert (to != null);
        if (!to.exists()) {
            File parentDir = to.getParentFile();
            if (!parentDir.exists()) parentDir.mkdirs();
            to.createNewFile();
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(from);
            try {
                out = new FileOutputStream(to);
                FileChannel ic = in.getChannel();
                try {
                    FileChannel oc = out.getChannel();
                    try {
                        oc.transferFrom(ic, 0, from.length());
                    } finally {
                        if (oc != null) {
                            oc.close();
                        }
                    }
                } finally {
                    if (ic != null) {
                        ic.close();
                    }
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
