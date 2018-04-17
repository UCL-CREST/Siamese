    private void writeFile(File file, String fileName) {
        try {
            FileInputStream fin = new FileInputStream(file);
            FileOutputStream fout = new FileOutputStream(dirTableModel.getDirectory().getAbsolutePath() + File.separator + fileName);
            int val;
            while ((val = fin.read()) != -1) fout.write(val);
            fin.close();
            fout.close();
            dirTableModel.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
