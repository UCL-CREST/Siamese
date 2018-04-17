    public boolean addMeFile(File f) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(directory + f.getName())));
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
            byte[] read = new byte[128];
            int len = 128;
            while ((len = in.read(read)) > 0) out.write(read, 0, len);
            out.flush();
            out.close();
            in.close();
            if (!PatchManager.mute) System.out.println("added : " + directory + f.getName());
        } catch (IOException e) {
            System.out.println("copy directory : " + e);
            return false;
        }
        return true;
    }
