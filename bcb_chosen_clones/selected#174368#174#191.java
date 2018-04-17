    private File copyFile(File source, File destiny) {
        try {
            FileInputStream fileinputstream = new FileInputStream(source);
            FileOutputStream fileoutputstream = new FileOutputStream(destiny);
            byte abyte0[] = new byte[4096];
            int i;
            while ((i = fileinputstream.read(abyte0)) != -1) fileoutputstream.write(abyte0, 0, i);
            fileinputstream.close();
            fileoutputstream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return destiny;
    }
