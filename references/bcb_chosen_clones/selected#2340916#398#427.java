    private void loadFonts() {
        File fontsDir = null;
        try {
            fontsDir = new ClassPathResource("fonts.ini").getFile().getParentFile();
        } catch (IOException e) {
            throw new RuntimeException("Error when trying to read the fonts.ini file", e);
        }
        File[] files = fontsDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".ttf");
            }
        });
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (int i = 0; i < files.length; i++) {
            try {
                try {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, files[i]);
                    ge.registerFont(font);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("There is no file: " + files[i].getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException("Error when reading: " + files[i].getAbsolutePath());
                }
            } catch (FontFormatException e) {
                System.out.println(files[i].getPath() + " is no true type font file");
            }
        }
    }
