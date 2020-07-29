    public static void copyTo(File source, File dest) {
        if (source.isHidden()) ; else if (source.isDirectory()) {
            File temp = new File(dest.getPath() + "/" + source.getName());
            temp.mkdir();
            for (File sel : source.listFiles()) copyTo(sel, temp);
        } else {
            try {
                File tempDest = new File(dest.getPath() + "/" + source.getName());
                tempDest.createNewFile();
                FileChannel sourceCh = new FileInputStream(source).getChannel();
                FileChannel destCh = new FileOutputStream(tempDest).getChannel();
                sourceCh.transferTo(0, sourceCh.size(), destCh);
                sourceCh.close();
                destCh.close();
            } catch (IOException ex) {
                Logger.getLogger(EditorUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
