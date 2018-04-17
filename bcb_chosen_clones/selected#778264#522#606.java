    private void test() {
        String postureString = postureListTextField.getText().trim();
        if (postureString.length() == 0) {
            return;
        }
        String synthPath = synthPathTextField.getText().trim();
        if (synthPath.length() == 0) {
            Logger.getInstance().warn("Empty synth. path option.");
            return;
        }
        String synthOption = synthOptionTextField.getText().trim();
        String tempDir = tempDirTextField.getText().trim();
        if (tempDir.length() == 0) {
            Logger.getInstance().warn("Empty temporary directory option.");
            return;
        }
        String audioPlayerPath = audioPlayerTextField.getText().trim();
        if (audioPlayerPath.length() == 0) {
            Logger.getInstance().warn("Empty audio player path option.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        try {
            final String prefix = tempDir + '/';
            PrintWriter out = new PrintWriter(prefix + ArticEditor.SYNTH_INPUT_FILE, "US-ASCII");
            String[] sa = postureString.split("\\s+");
            out.println("# 0");
            for (String item : sa) {
                Matcher m = testPattern.matcher(item);
                if (m.matches()) {
                    out.print(m.group(1));
                    out.print(' ');
                    out.println(m.group(2));
                } else {
                    out.print(item);
                    out.println(" 0");
                }
            }
            out.println("# 0");
            if (out.checkError()) {
                throw new Exception("Error while writing the synth input file.");
            }
            out.close();
            ModelFacade.save(new File(prefix + ArticEditor.SYNTH_CONFIG_DIR + '/' + ArticEditor.SYNTH_XML_FILE));
            List<String> argList = new ArrayList<String>();
            argList.add(synthPath);
            if (synthOption.length() > 0) {
                argList.add(synthOption);
            }
            argList.add(prefix + ArticEditor.SYNTH_CONFIG_DIR);
            argList.add(prefix + ArticEditor.SYNTH_INPUT_FILE);
            argList.add(prefix + ArticEditor.SYNTH_OUTPUT_FILE);
            ProcessBuilder pb = new ProcessBuilder(argList);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append('\n');
            }
            int exitValue = p.waitFor();
            sb.append("Synth exit value: " + exitValue);
            sb.append('\n');
            if (exitValue != 0) {
                Logger.getInstance().error(sb.toString());
                return;
            }
            pb = new ProcessBuilder(audioPlayerPath, prefix + ArticEditor.SYNTH_OUTPUT_FILE);
            pb.redirectErrorStream(true);
            p = pb.start();
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line).append('\n');
            }
            exitValue = p.waitFor();
            sb.append("Audio player exit value: " + exitValue);
            Logger.getInstance().info(sb.toString());
            testGraphPanel.updateData(prefix + ArticEditor.SYNTH_PARAM_OUTPUT_FILE);
        } catch (Exception e) {
            if (sb.length() > 0) {
                Logger.getInstance().error(sb.toString());
            }
            Logger.getInstance().error(e);
        }
    }
