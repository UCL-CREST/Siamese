    public void exportFile() {
        String expfolder = PropertyHandler.getInstance().getProperty(PropertyHandler.KINDLE_EXPORT_FOLDER_KEY);
        File out = new File(expfolder + File.separator + previewInfo.getTitle() + ".prc");
        File f = new File(absPath);
        try {
            FileOutputStream fout = new FileOutputStream(out);
            FileInputStream fin = new FileInputStream(f);
            int read = 0;
            byte[] buffer = new byte[1024 * 1024];
            while ((read = fin.read(buffer)) > 0) {
                fout.write(buffer, 0, read);
            }
            fin.close();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
