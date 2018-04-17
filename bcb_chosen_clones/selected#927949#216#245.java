    private void copy(File from, File to) {
        if (from.isDirectory()) {
            File[] files = from.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    File newTo = new File(to.getPath() + File.separator + files[i].getName());
                    newTo.mkdirs();
                    copy(files[i], newTo);
                } else {
                    copy(files[i], to);
                }
            }
        } else {
            try {
                to = new File(to.getPath() + File.separator + from.getName());
                to.createNewFile();
                FileChannel src = new FileInputStream(from).getChannel();
                FileChannel dest = new FileOutputStream(to).getChannel();
                dest.transferFrom(src, 0, src.size());
                dest.close();
                src.close();
            } catch (FileNotFoundException e) {
                errorLog(e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                errorLog(e.toString());
                e.printStackTrace();
            }
        }
    }
