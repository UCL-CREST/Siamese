    protected void writeSkeletonInstaller() throws IOException {
        sendMsg("Copying the skeleton installer", PackagerListener.MSG_VERBOSE);
        InputStream is = MultiVolumePackager.class.getResourceAsStream("/" + SKELETON_SUBPATH);
        if (is == null) {
            File skeleton = new File(Compiler.IZPACK_HOME, SKELETON_SUBPATH);
            is = new FileInputStream(skeleton);
        }
        ZipInputStream inJarStream = new ZipInputStream(is);
        List<String> excludes = new ArrayList<String>();
        excludes.add("META-INF.MANIFEST.MF");
        copyZipWithoutExcludes(inJarStream, primaryJarStream, excludes);
        is = MultiVolumePackager.class.getResourceAsStream("/" + SKELETON_SUBPATH);
        if (is == null) {
            File skeleton = new File(Compiler.IZPACK_HOME, SKELETON_SUBPATH);
            is = new FileInputStream(skeleton);
        }
        inJarStream = new ZipInputStream(is);
        boolean found = false;
        ZipEntry ze = null;
        String modifiedmanifest = null;
        while (((ze = inJarStream.getNextEntry()) != null) && !found) {
            if ("META-INF/MANIFEST.MF".equals(ze.getName())) {
                long size = ze.getSize();
                byte[] buffer = new byte[4096];
                int readbytes = 0;
                int totalreadbytes = 0;
                StringBuffer manifest = new StringBuffer();
                while (((readbytes = inJarStream.read(buffer)) > 0) && (totalreadbytes < size)) {
                    totalreadbytes += readbytes;
                    String tmp = new String(buffer, 0, readbytes, "utf-8");
                    manifest.append(tmp);
                }
                StringReader stringreader = new StringReader(manifest.toString());
                BufferedReader reader = new BufferedReader(stringreader);
                String line = null;
                StringBuffer modified = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Main-Class:")) {
                        line = "Main-Class: com.izforge.izpack.installer.MultiVolumeInstaller";
                    }
                    modified.append(line);
                    modified.append("\r\n");
                }
                reader.close();
                modifiedmanifest = modified.toString();
                break;
            }
        }
        primaryJarStream.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        primaryJarStream.write(modifiedmanifest.getBytes());
        primaryJarStream.closeEntry();
    }
