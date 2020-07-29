        @Override
        public void execute() {
            if (includes != null && includes.trim().isEmpty()) return;
            DirectoryScanner s = getDirectoryScanner(srcDir);
            String[] files = s.getIncludedFiles();
            files = filter(srcDir, destDir, files);
            if (files.length == 0) return;
            System.out.println("Generating " + files.length + " stub files to " + destDir);
            List<String> classNames = new ArrayList<String>();
            for (String file : files) {
                classNames.add(file.replaceAll(".java$", "").replace('/', '.'));
            }
            if (!fork) {
                GenStubs m = new GenStubs();
                boolean ok = m.run(srcDir.getPath(), destDir, classNames);
                if (!ok) throw new BuildException("genstubs failed");
            } else {
                List<String> cmd = new ArrayList<String>();
                String java_home = System.getProperty("java.home");
                cmd.add(new File(new File(java_home, "bin"), "java").getPath());
                if (classpath != null) cmd.add("-Xbootclasspath/p:" + classpath);
                cmd.add(GenStubs.class.getName());
                cmd.add("-sourcepath");
                cmd.add(srcDir.getPath());
                cmd.add("-s");
                cmd.add(destDir.getPath());
                cmd.addAll(classNames);
                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectErrorStream(true);
                try {
                    Process p = pb.start();
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    try {
                        String line;
                        while ((line = in.readLine()) != null) System.out.println(line);
                    } finally {
                        in.close();
                    }
                    int rc = p.waitFor();
                    if (rc != 0) throw new BuildException("genstubs failed");
                } catch (IOException e) {
                    throw new BuildException("genstubs failed", e);
                } catch (InterruptedException e) {
                    throw new BuildException("genstubs failed", e);
                }
            }
        }
