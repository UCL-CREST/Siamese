    protected void initGame() {
        try {
            for (File fonte : files) {
                String absolutePath = outputDir.getAbsolutePath();
                String separator = System.getProperty("file.separator");
                String name = fonte.getName();
                String destName = name.substring(0, name.length() - 3);
                File destino = new File(absolutePath + separator + destName + "jme");
                FileInputStream reader = new FileInputStream(fonte);
                OutputStream writer = new FileOutputStream(destino);
                conversor.setProperty("mtllib", fonte.toURL());
                conversor.convert(reader, writer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.finish();
    }
