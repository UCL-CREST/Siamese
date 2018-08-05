    public void writeOutput(String directory) throws IOException {
        File f = new File(directory);
        int i = 0;
        if (f.isDirectory()) {
            for (AppInventorScreen screen : screens.values()) {
                File screenFile = new File(getScreenFilePath(f.getAbsolutePath(), screen));
                screenFile.getParentFile().mkdirs();
                screenFile.createNewFile();
                FileWriter out = new FileWriter(screenFile);
                String initial = files.get(i).toString();
                Map<String, String> types = screen.getTypes();
                String[] lines = initial.split("\n");
                for (String key : types.keySet()) {
                    if (!key.trim().equals(screen.getName().trim())) {
                        String value = types.get(key);
                        boolean varFound = false;
                        boolean importFound = false;
                        for (String line : lines) {
                            if (line.matches("^\\s*(public|private)\\s+" + value + "\\s+" + key + "\\s*=.*;$")) varFound = true;
                            if (line.matches("^\\s*(public|private)\\s+" + value + "\\s+" + key + "\\s*;$")) varFound = true;
                            if (line.matches("^\\s*import\\s+.*" + value + "\\s*;$")) importFound = true;
                        }
                        if (!varFound) initial = initial.replaceFirst("(?s)(?<=\\{\n)", "\tprivate " + value + " " + key + ";\n");
                        if (!importFound) initial = initial.replaceFirst("(?=import)", "import com.google.devtools.simple.runtime.components.android." + value + ";\n");
                    }
                }
                out.write(initial);
                out.close();
                i++;
            }
            File manifestFile = new File(getManifestFilePath(f.getAbsolutePath(), manifest));
            manifestFile.getParentFile().mkdirs();
            manifestFile.createNewFile();
            FileWriter out = new FileWriter(manifestFile);
            out.write(manifest.toString());
            out.close();
            File projectFile = new File(getProjectFilePath(f.getAbsolutePath(), project));
            projectFile.getParentFile().mkdirs();
            projectFile.createNewFile();
            out = new FileWriter(projectFile);
            out.write(project.toString());
            out.close();
            String[] copyResourceFilenames = { "proguard.cfg", "project.properties", "libSimpleAndroidRuntime.jar", "\\.classpath", "res/drawable/icon.png", "\\.settings/org.eclipse.jdt.core.prefs" };
            for (String copyResourceFilename : copyResourceFilenames) {
                InputStream is = getClass().getResourceAsStream("/resources/" + copyResourceFilename.replace("\\.", ""));
                File outputFile = new File(f.getAbsoluteFile() + File.separator + copyResourceFilename.replace("\\.", "."));
                outputFile.getParentFile().mkdirs();
                OutputStream os = new FileOutputStream(outputFile);
                byte[] buf = new byte[1024];
                int readBytes;
                if (is == null) System.out.println("/resources/" + copyResourceFilename.replace("\\.", ""));
                if (os == null) System.out.println(f.getAbsolutePath() + File.separator + copyResourceFilename.replace("\\.", "."));
                while ((readBytes = is.read(buf)) > 0) {
                    os.write(buf, 0, readBytes);
                }
            }
            for (String assetName : assets) {
                InputStream is = new FileInputStream(new File(assetsDir.getAbsolutePath() + File.separator + assetName));
                File outputFile = new File(f.getAbsoluteFile() + File.separator + assetName);
                outputFile.getParentFile().mkdirs();
                OutputStream os = new FileOutputStream(outputFile);
                byte[] buf = new byte[1024];
                int readBytes;
                while ((readBytes = is.read(buf)) > 0) {
                    os.write(buf, 0, readBytes);
                }
            }
            File assetsOutput = new File(getAssetsFilePath(f.getAbsolutePath()));
            new File(assetsDir.getAbsoluteFile() + File.separator + "assets").renameTo(assetsOutput);
        }
    }
