    private File copyFile(File file, String newName, File folder) {
        File newFile = null;
        if (!file.exists()) {
            System.out.println("File " + file + " does not exist");
            return null;
        }
        if (file.isFile()) {
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                newFile = new File(folder, newName);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                out = new BufferedOutputStream(new FileOutputStream(newFile));
                int read;
                byte[] buffer = new byte[8192];
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                updateTreeUI();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (file.isDirectory()) {
            newFile = new File(folder, newName);
            if (!newFile.exists()) {
                newFile.mkdir();
            }
            for (File f : file.listFiles()) {
                copyFile(f, f.getName(), newFile);
            }
        }
        return newFile;
    }
