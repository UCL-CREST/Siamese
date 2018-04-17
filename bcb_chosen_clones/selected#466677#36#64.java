    static void copyDirectory(File sourceLocation, File targetLocation) {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                if (!targetLocation.mkdir()) {
                    String message = "It was not possible to create directory " + targetLocation.getAbsolutePath() + ". This application cannot continue.";
                    throw new RuntimeException(message);
                }
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {
            try {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException exception) {
                String message = "It was not possible to copy the source directory " + "to the chosen destiny due to the following cause: " + exception.getMessage();
                throw new RuntimeException(message, exception);
            }
        }
    }
