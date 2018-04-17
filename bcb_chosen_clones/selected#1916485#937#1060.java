    private void writeJar() {
        try {
            File outJar = new File(currentProjectDir + DEPLOYDIR + fileSeparator + currentProjectName + ".jar");
            jarSize = (int) outJar.length();
            File tempJar = File.createTempFile("hipergps" + currentProjectName, ".jar");
            tempJar.deleteOnExit();
            File preJar = new File(currentProjectDir + "/res/wtj2me.jar");
            JarInputStream preJarInStream = new JarInputStream(new FileInputStream(preJar));
            Manifest mFest = preJarInStream.getManifest();
            java.util.jar.Attributes atts = mFest.getMainAttributes();
            if (hiperGeoId != null) {
                atts.putValue("hiperGeoId", hiperGeoId);
            }
            jad.updateAttributes(atts);
            JarOutputStream jarOutStream = new JarOutputStream(new FileOutputStream(tempJar), mFest);
            byte[] buffer = new byte[WalkingtoolsInformation.BUFFERSIZE];
            JarEntry jarEntry = null;
            while ((jarEntry = preJarInStream.getNextJarEntry()) != null) {
                if (jarEntry.getName().contains("net/") || jarEntry.getName().contains("org/")) {
                    try {
                        jarOutStream.putNextEntry(jarEntry);
                    } catch (ZipException ze) {
                        continue;
                    }
                    int read;
                    while ((read = preJarInStream.read(buffer)) != -1) {
                        jarOutStream.write(buffer, 0, read);
                    }
                    jarOutStream.closeEntry();
                }
            }
            File[] icons = { new File(currentProjectDir + WalkingtoolsInformation.IMAGEDIR + fileSeparator + "icon_" + WalkingtoolsInformation.MEDIAUUID + ".png"), new File(currentProjectDir + WalkingtoolsInformation.IMAGEDIR + fileSeparator + "loaderIcon_" + WalkingtoolsInformation.MEDIAUUID + ".png"), new File(currentProjectDir + WalkingtoolsInformation.IMAGEDIR + fileSeparator + "mygps_" + WalkingtoolsInformation.MEDIAUUID + ".png") };
            for (int i = 0; i < icons.length; i++) {
                jarEntry = new JarEntry("img/" + icons[i].getName());
                try {
                    jarOutStream.putNextEntry(jarEntry);
                } catch (ZipException ze) {
                    continue;
                }
                FileInputStream in = new FileInputStream(icons[i]);
                while (true) {
                    int read = in.read(buffer, 0, buffer.length);
                    if (read <= 0) {
                        break;
                    }
                    jarOutStream.write(buffer, 0, read);
                }
                in.close();
            }
            for (int i = 0; i < imageFiles.size(); i++) {
                jarEntry = new JarEntry("img/" + imageFiles.get(i).getName());
                try {
                    jarOutStream.putNextEntry(jarEntry);
                } catch (ZipException ze) {
                    continue;
                }
                FileInputStream in = new FileInputStream(imageFiles.get(i));
                while (true) {
                    int read = in.read(buffer, 0, buffer.length);
                    if (read <= 0) {
                        break;
                    }
                    jarOutStream.write(buffer, 0, read);
                }
                in.close();
            }
            for (int i = 0; i < audioFiles.size(); i++) {
                jarEntry = new JarEntry("audio/" + audioFiles.get(i).getName());
                try {
                    jarOutStream.putNextEntry(jarEntry);
                } catch (ZipException ze) {
                    continue;
                }
                FileInputStream in = new FileInputStream(audioFiles.get(i));
                while (true) {
                    int read = in.read(buffer, 0, buffer.length);
                    if (read <= 0) {
                        break;
                    }
                    jarOutStream.write(buffer, 0, read);
                }
                in.close();
            }
            File gpx = new File(currentProjectDir + WalkingtoolsInformation.GPXDIR + "/hipergps.gpx");
            jarEntry = new JarEntry("gpx/" + gpx.getName());
            jarOutStream.putNextEntry(jarEntry);
            FileInputStream in = new FileInputStream(gpx);
            while (true) {
                int read = in.read(buffer, 0, buffer.length);
                if (read <= 0) {
                    break;
                }
                jarOutStream.write(buffer, 0, read);
            }
            in.close();
            jarOutStream.flush();
            jarOutStream.close();
            jarSize = (int) tempJar.length();
            preJarInStream = new JarInputStream(new FileInputStream(tempJar));
            mFest = preJarInStream.getManifest();
            atts = mFest.getMainAttributes();
            atts.putValue("MIDlet-Jar-Size", "" + jarSize + 1);
            jarOutStream = new JarOutputStream(new FileOutputStream(outJar), mFest);
            while ((jarEntry = preJarInStream.getNextJarEntry()) != null) {
                try {
                    jarOutStream.putNextEntry(jarEntry);
                } catch (ZipException ze) {
                    continue;
                }
                int read;
                while ((read = preJarInStream.read(buffer)) != -1) {
                    jarOutStream.write(buffer, 0, read);
                }
                jarOutStream.closeEntry();
            }
            jarOutStream.flush();
            preJarInStream.close();
            jarOutStream.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
