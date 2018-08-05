    private void registerInManifest(String jarFileName) {
        try {
            String manifestName = "META-INF/MANIFEST.MF";
            String classPathPrefix = "Class-Path: ";
            File mainJarFile = new File(".." + File.separator + MAIN_JAR);
            ZipFile mainJar = new ZipFile(mainJarFile);
            ZipEntry manifestEntry = mainJar.getEntry(manifestName);
            StringBuilder preBuilder = new StringBuilder(""), postBuilder = new StringBuilder(""), mainBuilder = new StringBuilder("");
            BufferedReader br = new BufferedReader(new InputStreamReader(mainJar.getInputStream(manifestEntry)));
            State state = State.PRE;
            String line = null;
            while ((line = br.readLine()) != null) {
                switch(state) {
                    case PRE:
                        if (line.startsWith(classPathPrefix)) {
                            mainBuilder.append(classPathPrefix + "lib/" + jarFileName + " " + line.substring(classPathPrefix.length()));
                            state = State.MAIN;
                        } else {
                            preBuilder.append(line + "\r\n");
                        }
                        break;
                    case MAIN:
                        if (line.startsWith(" ")) {
                            mainBuilder.append(line.substring(1));
                        } else {
                            mainBuilder.append("\r\n");
                            postBuilder.append(line + "\r\n");
                            state = State.POST;
                        }
                        break;
                    case POST:
                        postBuilder.append(line + "\r\n");
                        break;
                }
            }
            br.close();
            for (int i = 69; i < mainBuilder.length(); i += 70) {
                mainBuilder.insert(i, "\r\n ");
            }
            String newManifest = preBuilder.toString() + mainBuilder.toString() + postBuilder.toString();
            File newJarFile = new File(mainJarFile.getAbsolutePath() + ".new");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(newJarFile));
            int len = 0;
            byte[] buf = new byte[1024];
            Enumeration<? extends ZipEntry> entries = mainJar.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                out.putNextEntry(new ZipEntry(entry.getName()));
                if (entry.getName().equals(manifestName)) {
                    out.write(newManifest.getBytes());
                } else {
                    InputStream in = mainJar.getInputStream(entry);
                    while ((len = in.read(buf)) > -1) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                }
            }
            out.close();
            boolean success = mainJarFile.delete();
            success = success && newJarFile.renameTo(mainJarFile);
            if (!success) {
                throw new IOException("could not complete file operation!");
            }
        } catch (IOException ioe) {
            System.err.println("Error writing Manifest to jar: " + ioe.getMessage());
            ioe.printStackTrace();
            System.exit(1);
        }
    }
