    private void real_run() {
        try {
            String cmd_str = "java -Xmx" + memSize + "m -Dfile.encoding=utf8 -jar " + tercomJarFileName + " -r " + refFileName + " -h " + hypFileName + " -o ter -n " + outFileNamePrefix;
            cmd_str += " -b " + beamWidth;
            cmd_str += " -d " + maxShiftDist;
            if (caseSensitive) {
                cmd_str += " -s";
            }
            if (!withPunctuation) {
                cmd_str += " -P";
            }
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd_str);
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), 0);
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), 0);
            errorGobbler.start();
            outputGobbler.start();
            int exitValue = p.waitFor();
            File fd;
            fd = new File(hypFileName);
            if (fd.exists()) fd.delete();
            fd = new File(refFileName);
            if (fd.exists()) fd.delete();
        } catch (IOException e) {
            System.err.println("IOException in TER.runTercom(...): " + e.getMessage());
            System.exit(99902);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException in TER.runTercom(...): " + e.getMessage());
            System.exit(99903);
        }
        blocker.release();
    }
