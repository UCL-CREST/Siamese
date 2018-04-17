    public static void copyTransformationResources(File sourceLocation, File targetLocation) throws IOException, FileNotFoundException {
        try {
            if (sourceLocation.isDirectory()) {
                if (!targetLocation.exists()) {
                    targetLocation.mkdir();
                }
                String[] children = sourceLocation.list(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        return !name.contains(".svn");
                    }
                });
                for (int i = 0; i < children.length; i++) {
                    copyTransformationResources(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
                }
            } else {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
