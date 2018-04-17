    public NucZipper(File file, NucleiMgr nucleiMgr) {
        super();
        String nucDir = nucleiMgr.getConfig().iZipNucDir;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zos = new ZipOutputStream(fos);
            Vector nuclei = (Vector) nucleiMgr.getNucleiRecord();
            for (int i = 0; i < nuclei.size(); i++) {
                String ename = nucDir + "t" + EUtils.makePaddedInt(i + 1) + "-nuclei";
                zos.putNextEntry(new ZipEntry(ename));
                String s;
                for (int j = 0; j < ((Vector) nuclei.elementAt(i)).size(); j++) {
                    Nucleus n = (Nucleus) ((Vector) nuclei.elementAt(i)).elementAt(j);
                    s = formatNucleus(j, n);
                    byte[] b = s.getBytes();
                    zos.write(b, 0, b.length);
                }
                zos.closeEntry();
            }
            addParameters(zos, nucleiMgr);
            zos.close();
        } catch (IOException ioe) {
            System.out.println("NucZipper exception: " + ioe);
            new AceTreeHelp("/org/rhwlab/help/messages/PermissionError.html", 200, 200);
        }
    }
