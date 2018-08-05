    public static void copy(String sourceName, String destName) throws IOException {
        File src = new File(sourceName);
        File dest = new File(destName);
        BufferedInputStream source = null;
        BufferedOutputStream destination = null;
        byte[] buffer;
        int bytes_read;
        long byteCount = 0;
        if (!src.exists()) throw new IOException("Source not found: " + src);
        if (!src.canRead()) throw new IOException("Source is unreadable: " + src);
        if (src.isFile()) {
            if (!dest.exists()) {
                File parentdir = parent(dest);
                if (!parentdir.exists()) parentdir.mkdir();
            } else if (dest.isDirectory()) {
                if (src.isDirectory()) dest = new File(dest + File.separator + src); else dest = new File(dest + File.separator + src.getName());
            }
        } else if (src.isDirectory()) {
            if (dest.isFile()) throw new IOException("Cannot copy directory " + src + " to file " + dest);
            if (!dest.exists()) dest.mkdir();
        }
        if ((!dest.canWrite()) && (dest.exists())) throw new IOException("Destination is unwriteable: " + dest);
        if (src.isFile()) {
            try {
                source = new BufferedInputStream(new FileInputStream(src));
                destination = new BufferedOutputStream(new FileOutputStream(dest));
                buffer = new byte[4096];
                byteCount = 0;
                while (true) {
                    bytes_read = source.read(buffer);
                    if (bytes_read == -1) break;
                    destination.write(buffer, 0, bytes_read);
                    byteCount = byteCount + bytes_read;
                }
            } finally {
                if (source != null) source.close();
                if (destination != null) destination.close();
            }
        } else if (src.isDirectory()) {
            String targetfile, target, targetdest;
            String[] files = src.list();
            for (int i = 0; i < files.length; i++) {
                targetfile = files[i];
                target = src + File.separator + targetfile;
                targetdest = dest + File.separator + targetfile;
                if ((new File(target)).isDirectory()) {
                    copy(new File(target).getCanonicalPath(), new File(targetdest).getCanonicalPath());
                } else {
                    try {
                        byteCount = 0;
                        source = new BufferedInputStream(new FileInputStream(target));
                        destination = new BufferedOutputStream(new FileOutputStream(targetdest));
                        buffer = new byte[4096];
                        while (true) {
                            bytes_read = source.read(buffer);
                            if (bytes_read == -1) break;
                            destination.write(buffer, 0, bytes_read);
                            byteCount = byteCount + bytes_read;
                        }
                    } finally {
                        if (source != null) source.close();
                        if (destination != null) destination.close();
                    }
                }
            }
        }
    }
