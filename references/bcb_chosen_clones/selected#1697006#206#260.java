    private void updatePluginIncrementally(ManagedExtension extension, InputStream diffJarIn, String fromVersion, String newVersion) throws IOException {
        ByteArrayOutputStream diffJarBuffer = new ByteArrayOutputStream();
        Tools.copyStreamSynchronously(diffJarIn, diffJarBuffer, true);
        LogService.getRoot().fine("Downloaded incremental zip.");
        InMemoryZipFile diffJar = new InMemoryZipFile(diffJarBuffer.toByteArray());
        Set<String> toDelete = new HashSet<String>();
        byte[] updateEntry = diffJar.getContents("META-INF/UPDATE");
        if (updateEntry == null) {
            throw new IOException("META-INFO/UPDATE entry missing");
        }
        BufferedReader updateReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(updateEntry), "UTF-8"));
        String line;
        while ((line = updateReader.readLine()) != null) {
            String[] split = line.split(" ", 2);
            if (split.length != 2) {
                throw new IOException("Illegal entry in update script: " + line);
            }
            if ("DELETE".equals(split[0])) {
                toDelete.add(split[1].trim());
            } else {
                throw new IOException("Illegal entry in update script: " + line);
            }
        }
        LogService.getRoot().fine("Extracted update script, " + toDelete.size() + " items to delete.");
        Set<String> allNames = new HashSet<String>();
        allNames.addAll(diffJar.entryNames());
        JarFile fromJar = extension.findArchive(fromVersion);
        Enumeration<? extends ZipEntry> e = fromJar.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = e.nextElement();
            allNames.add(entry.getName());
        }
        LogService.getRoot().info("Extracted entry names, " + allNames.size() + " entries in total.");
        File newFile = extension.getDestinationFile(newVersion);
        ZipOutputStream newJar = new ZipOutputStream(new FileOutputStream(newFile));
        ZipFile oldArchive = extension.findArchive();
        for (String name : allNames) {
            if (toDelete.contains(name)) {
                LogService.getRoot().finest("DELETE " + name);
                continue;
            }
            newJar.putNextEntry(new ZipEntry(name));
            if (diffJar.containsEntry(name)) {
                newJar.write(diffJar.getContents(name));
                LogService.getRoot().finest("UPDATE " + name);
            } else {
                ZipEntry oldEntry = oldArchive.getEntry(name);
                Tools.copyStreamSynchronously(oldArchive.getInputStream(oldEntry), newJar, false);
                LogService.getRoot().finest("STORE " + name);
            }
            newJar.closeEntry();
        }
        newJar.finish();
        newJar.close();
    }
