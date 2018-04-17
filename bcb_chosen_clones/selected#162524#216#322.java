    private void writePacks(File primaryfile) throws Exception {
        final int num = packsList.size();
        sendMsg("Writing " + num + " Pack" + (num > 1 ? "s" : "") + " into installer");
        Debug.trace("Writing " + num + " Pack" + (num > 1 ? "s" : "") + " into installer");
        Map storedFiles = new HashMap();
        String classname = this.getClass().getName();
        String volumesize = this.getVariables().getProperty(classname + ".volumesize");
        String extraspace = this.getVariables().getProperty(classname + ".firstvolumefreespace");
        long volumesizel = FileSpanningOutputStream.DEFAULT_VOLUME_SIZE;
        long extraspacel = FileSpanningOutputStream.DEFAULT_ADDITIONAL_FIRST_VOLUME_FREE_SPACE_SIZE;
        if (volumesize != null) {
            volumesizel = Long.parseLong(volumesize);
        }
        if (extraspace != null) {
            extraspacel = Long.parseLong(extraspace);
        }
        Debug.trace("Volumesize: " + volumesizel);
        Debug.trace("Extra space on first volume: " + extraspacel);
        FileSpanningOutputStream fout = new FileSpanningOutputStream(primaryfile.getParent() + File.separator + primaryfile.getName() + ".pak", volumesizel);
        fout.setFirstvolumefreespacesize(extraspacel);
        int packNumber = 0;
        for (PackInfo aPacksList : packsList) {
            PackInfo packInfo = aPacksList;
            Pack pack = packInfo.getPack();
            pack.nbytes = 0;
            sendMsg("Writing Pack " + packNumber + ": " + pack.name, PackagerListener.MSG_VERBOSE);
            Debug.trace("Writing Pack " + packNumber + ": " + pack.name);
            ZipEntry entry = new ZipEntry("packs/pack" + packNumber);
            primaryJarStream.putNextEntry(entry);
            ObjectOutputStream objOut = new ObjectOutputStream(primaryJarStream);
            objOut.writeInt(packInfo.getPackFiles().size());
            Iterator iter = packInfo.getPackFiles().iterator();
            for (Object o : packInfo.getPackFiles()) {
                boolean addFile = !pack.loose;
                PackFile packfile = (PackFile) o;
                XPackFile pf = new XPackFile(packfile);
                File file = packInfo.getFile(packfile);
                Debug.trace("Next file: " + file.getAbsolutePath());
                Object[] info = (Object[]) storedFiles.get(file);
                if (info != null && !packJarsSeparate) {
                    Debug.trace("File already included in other pack");
                    pf.setPreviousPackFileRef((String) info[0], (Long) info[1]);
                    addFile = false;
                }
                if (addFile && !pf.isDirectory()) {
                    long pos = fout.getFilepointer();
                    pf.setArchivefileposition(pos);
                    int volumecountbeforewrite = fout.getVolumeCount();
                    FileInputStream inStream = new FileInputStream(file);
                    long bytesWritten = copyStream(inStream, fout);
                    fout.flush();
                    long posafterwrite = fout.getFilepointer();
                    Debug.trace("File (" + pf.sourcePath + ") " + pos + " <-> " + posafterwrite);
                    if (fout.getFilepointer() != (pos + bytesWritten)) {
                        Debug.trace("file: " + file.getName());
                        Debug.trace("(Filepos/BytesWritten/ExpectedNewFilePos/NewFilePointer) (" + pos + "/" + bytesWritten + "/" + (pos + bytesWritten) + "/" + fout.getFilepointer() + ")");
                        Debug.trace("Volumecount (before/after) (" + volumecountbeforewrite + "/" + fout.getVolumeCount() + ")");
                        throw new IOException("Error new filepointer is illegal");
                    }
                    if (bytesWritten != pf.length()) {
                        throw new IOException("File size mismatch when reading " + file);
                    }
                    inStream.close();
                }
                objOut.writeObject(pf);
                objOut.flush();
                pack.nbytes += pf.length();
            }
            objOut.writeInt(packInfo.getParsables().size());
            iter = packInfo.getParsables().iterator();
            while (iter.hasNext()) {
                objOut.writeObject(iter.next());
            }
            objOut.writeInt(packInfo.getExecutables().size());
            iter = packInfo.getExecutables().iterator();
            while (iter.hasNext()) {
                objOut.writeObject(iter.next());
            }
            objOut.writeInt(packInfo.getUpdateChecks().size());
            iter = packInfo.getUpdateChecks().iterator();
            while (iter.hasNext()) {
                objOut.writeObject(iter.next());
            }
            objOut.flush();
            packNumber++;
        }
        int volumes = fout.getVolumeCount();
        Debug.trace("Written " + volumes + " volumes");
        String volumename = primaryfile.getName() + ".pak";
        fout.flush();
        fout.close();
        primaryJarStream.putNextEntry(new ZipEntry("volumes.info"));
        ObjectOutputStream out = new ObjectOutputStream(primaryJarStream);
        out.writeInt(volumes);
        out.writeUTF(volumename);
        out.flush();
        primaryJarStream.closeEntry();
        primaryJarStream.putNextEntry(new ZipEntry("packs.info"));
        out = new ObjectOutputStream(primaryJarStream);
        out.writeInt(packsList.size());
        for (PackInfo aPacksList : packsList) {
            PackInfo pack = aPacksList;
            out.writeObject(pack.getPack());
        }
        out.flush();
        primaryJarStream.closeEntry();
    }
