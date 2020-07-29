    public void copy(String fs, String t) throws IOException {
        if (new File(t).isDirectory() && !t.endsWith(File.separator)) t = t + File.separator;
        File f = new File(fs);
        String[] flist = f.list();
        if (flist == null || flist.length < 1) return;
        for (int i = 0; i < flist.length; i++) {
            String p = f.getAbsolutePath();
            if (!p.endsWith("" + File.separatorChar)) p = p + File.separatorChar;
            File nf = new File(p + flist[i]);
            String nt = t;
            boolean isd = false;
            if (nf.isDirectory()) {
                nt = nt + flist[i] + File.separator;
                new File(nt).mkdirs();
                isd = true;
                copy(p + flist[i], nt);
                nt = nt + flist[i];
                File nf2 = new File(nt);
            } else try {
                FileUtil.copy(nf, new File(t + nf.getName()));
            } catch (IOException ed) {
                if (ed.toString().indexOf("Disk quota exceeded") > -1) throw ed;
                System.out.println(nf + " Error:" + ed.toString() + " Copying File to " + t + nf.getName());
            }
        }
    }
