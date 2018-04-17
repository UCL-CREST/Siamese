    public void format(File source, File target) {
        if (!source.exists()) {
            throw new IllegalArgumentException("Source '" + source + " doesn't exist");
        }
        if (!source.isFile()) {
            throw new IllegalArgumentException("Source '" + source + " is not a file");
        }
        target.mkdirs();
        String fileExtension = source.getName().substring(source.getName().lastIndexOf(".") + 1);
        String _target = source.getName().replace(fileExtension, "html");
        target = new File(target.getPath() + "/" + _target);
        try {
            Reader reader = new FileReader(source);
            Writer writer = new FileWriter(target);
            this.format(reader, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
