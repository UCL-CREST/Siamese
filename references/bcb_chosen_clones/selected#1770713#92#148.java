    public static File jar(File in, String outArc, File tempDir, PatchConfigXML conf) {
        FileOutputStream arcFile = null;
        JarOutputStream jout = null;
        DirectoryScanner ds = null;
        ds = new DirectoryScanner();
        ds.setCaseSensitive(true);
        ds.setBasedir(in);
        ds.scan();
        ds.setCaseSensitive(true);
        String[] names = ds.getIncludedFiles();
        ArrayList exName = new ArrayList();
        if (names == null || names.length < 1) return null;
        File tempArc = new File(tempDir, outArc.substring(0, outArc.length()));
        try {
            Manifest mf = null;
            List v = new ArrayList();
            for (int i = 0; i < names.length; i++) {
                if (names[i].toUpperCase().indexOf("MANIFEST.MF") > -1) {
                    FileInputStream fis = new FileInputStream(in.getAbsolutePath() + "/" + names[i].replace('\\', '/'));
                    mf = new Manifest(fis);
                } else v.add(names[i]);
            }
            String[] toJar = new String[v.size()];
            v.toArray(toJar);
            tempArc.createNewFile();
            arcFile = new FileOutputStream(tempArc);
            if (mf == null) jout = new JarOutputStream(arcFile); else jout = new JarOutputStream(arcFile, mf);
            byte[] buffer = new byte[1024];
            for (int i = 0; i < toJar.length; i++) {
                if (conf != null) {
                    if (!conf.allowFileAction(toJar[i], PatchConfigXML.OP_CREATE)) {
                        exName.add(toJar[i]);
                        continue;
                    }
                }
                String currentPath = in.getAbsolutePath() + "/" + toJar[i];
                String entryName = toJar[i].replace('\\', '/');
                JarEntry currentEntry = new JarEntry(entryName);
                jout.putNextEntry(currentEntry);
                FileInputStream fis = new FileInputStream(currentPath);
                int len;
                while ((len = fis.read(buffer)) >= 0) jout.write(buffer, 0, len);
                fis.close();
                jout.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                jout.close();
                arcFile.close();
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }
        return tempArc;
    }
