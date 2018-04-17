    protected void writePacks() throws Exception {
        final int num = packsList.size();
        sendMsg("Writing " + num + " Pack" + (num > 1 ? "s" : "") + " into installer");
        Map<File, Object[]> storedFiles = new HashMap<File, Object[]>();
        Map<Integer, File> pack200Map = new HashMap<Integer, File>();
        int pack200Counter = 0;
        primaryJarStream.setEncoding("utf-8");
        int packNumber = 0;
        Iterator<PackInfo> packIter = packsList.iterator();
        IXMLElement root = new XMLElementImpl("packs");
        while (packIter.hasNext()) {
            PackInfo packInfo = packIter.next();
            Pack pack = packInfo.getPack();
            pack.nbytes = 0;
            if ((pack.id == null) || (pack.id.length() == 0)) {
                pack.id = pack.name;
            }
            com.izforge.izpack.util.JarOutputStream packStream = primaryJarStream;
            if (packJarsSeparate) {
                String name = baseFile.getName() + ".pack-" + pack.id + ".jar";
                packStream = getJarOutputStream(name);
            }
            OutputStream comprStream = packStream;
            sendMsg("Writing Pack " + packNumber + ": " + pack.name, PackagerListener.MSG_VERBOSE);
            org.apache.tools.zip.ZipEntry entry = new org.apache.tools.zip.ZipEntry("packs/pack-" + pack.id);
            if (!compressor.useStandardCompression()) {
                entry.setMethod(ZipEntry.STORED);
                entry.setComment(compressor.getCompressionFormatSymbols()[0]);
                packStream.putNextEntry(entry);
                packStream.flush();
                comprStream = compressor.getOutputStream(packStream);
            } else {
                int level = compressor.getCompressionLevel();
                if (level >= 0 && level < 10) {
                    packStream.setLevel(level);
                }
                packStream.putNextEntry(entry);
                packStream.flush();
            }
            ByteCountingOutputStream dos = new ByteCountingOutputStream(comprStream);
            ObjectOutputStream objOut = new ObjectOutputStream(dos);
            objOut.writeInt(packInfo.getPackFiles().size());
            Iterator iter = packInfo.getPackFiles().iterator();
            while (iter.hasNext()) {
                boolean addFile = !pack.loose;
                boolean pack200 = false;
                PackFile pf = (PackFile) iter.next();
                File file = packInfo.getFile(pf);
                if (file.getName().toLowerCase().endsWith(".jar") && info.isPack200Compression() && isNotSignedJar(file)) {
                    pf.setPack200Jar(true);
                    pack200 = true;
                }
                Object[] info = storedFiles.get(file);
                if (info != null && !packJarsSeparate) {
                    pf.setPreviousPackFileRef((String) info[0], (Long) info[1]);
                    addFile = false;
                }
                objOut.writeObject(pf);
                if (addFile && !pf.isDirectory()) {
                    long pos = dos.getByteCount();
                    if (pack200) {
                        pack200Map.put(pack200Counter, file);
                        objOut.writeInt(pack200Counter);
                        pack200Counter = pack200Counter + 1;
                    } else {
                        FileInputStream inStream = new FileInputStream(file);
                        long bytesWritten = PackagerHelper.copyStream(inStream, objOut);
                        inStream.close();
                        if (bytesWritten != pf.length()) {
                            throw new IOException("File size mismatch when reading " + file);
                        }
                    }
                    storedFiles.put(file, new Object[] { pack.id, pos });
                }
                pack.nbytes += pf.size();
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
            if (!compressor.useStandardCompression()) {
                comprStream.close();
            }
            packStream.closeEntry();
            if (packJarsSeparate) {
                packStream.closeAlways();
            }
            IXMLElement child = new XMLElementImpl("pack", root);
            child.setAttribute("nbytes", Long.toString(pack.nbytes));
            child.setAttribute("name", pack.name);
            if (pack.id != null) {
                child.setAttribute("id", pack.id);
            }
            root.addChild(child);
            packNumber++;
        }
        primaryJarStream.putNextEntry(new org.apache.tools.zip.ZipEntry("packs.info"));
        ObjectOutputStream out = new ObjectOutputStream(primaryJarStream);
        out.writeInt(packsList.size());
        Iterator<PackInfo> i = packsList.iterator();
        while (i.hasNext()) {
            PackInfo pack = i.next();
            out.writeObject(pack.getPack());
        }
        out.flush();
        primaryJarStream.closeEntry();
        Pack200.Packer packer = createAgressivePack200Packer();
        for (Integer key : pack200Map.keySet()) {
            File file = pack200Map.get(key);
            primaryJarStream.putNextEntry(new org.apache.tools.zip.ZipEntry("packs/pack200-" + key));
            JarFile jar = new JarFile(file);
            packer.pack(jar, primaryJarStream);
            jar.close();
            primaryJarStream.closeEntry();
        }
    }
