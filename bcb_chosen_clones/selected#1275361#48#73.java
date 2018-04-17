    protected File getFile(NameCategory category) throws IOException {
        File home = new File(System.getProperty("user.dir"));
        String fileName = String.format("%s.txt", category);
        File file = new File(home, fileName);
        if (file.exists()) {
            return file;
        } else {
            URL url = LocalNameGenerator.class.getResource("/" + fileName);
            if (url == null) {
                throw new IllegalStateException(String.format("Cannot find resource at %s", fileName));
            } else {
                InputStream in = url.openStream();
                try {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    try {
                        IOUtils.copy(in, out);
                    } finally {
                        out.close();
                    }
                } finally {
                    in.close();
                }
                return file;
            }
        }
    }
