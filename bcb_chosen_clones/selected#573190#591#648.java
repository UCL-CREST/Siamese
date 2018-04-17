    private static void extractSOUNDFILES() throws Exception {
        ArrayList<File> soundFiles = new ArrayList<File>();
        File soundsFolder = new File("/var/lib/asterisk/sounds/");
        if (soundsFolder.exists()) {
            File soundFiles1[] = soundsFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (NullStatus.isNull(name)) {
                        return false;
                    }
                    if (name.endsWith("-sounds.jar")) {
                        return true;
                    }
                    return false;
                }
            });
            for (int i = 0; i < soundFiles1.length; i++) {
                soundFiles.add(soundFiles1[i]);
            }
        }
        soundsFolder = new File("/opt/jboss/server/default/deploy/Tobe.war/WEB-INF/lib/");
        if (soundsFolder.exists()) {
            File soundFiles1[] = soundsFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (NullStatus.isNull(name)) {
                        return false;
                    }
                    if (name.endsWith("-sounds.jar")) {
                        return true;
                    }
                    return false;
                }
            });
            for (int i = 0; i < soundFiles1.length; i++) {
                soundFiles.add(soundFiles1[i]);
            }
        }
        for (int i = 0; i < soundFiles.size(); i++) {
            ZipFile zipFile = new ZipFile(soundFiles.get(i));
            InputStream inputStream = null;
            Enumeration<? extends ZipEntry> oEnum = zipFile.entries();
            while (oEnum.hasMoreElements()) {
                ZipEntry zipEntry = oEnum.nextElement();
                File file = new File("/var/lib/asterisk/sounds/", zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    inputStream = zipFile.getInputStream(zipEntry);
                    FileOutputStream fos = new FileOutputStream(file);
                    IOUtils.copy(inputStream, fos);
                    fos.close();
                }
            }
        }
    }
