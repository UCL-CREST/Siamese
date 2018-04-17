    private boolean copyFile(File _file1, File _file2) {
        FileInputStream fis;
        FileOutputStream fos;
        try {
            fis = new FileInputStream(_file1);
            fos = new FileOutputStream(_file2);
            FileChannel canalFuente = fis.getChannel();
            canalFuente.transferTo(0, canalFuente.size(), fos.getChannel());
            fis.close();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return false;
    }
