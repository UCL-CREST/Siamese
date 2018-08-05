    public void run() {
        Debugger.appendAction("Opened RenderLoggingDialog");
        Process p = null;
        try {
            setVisible(true);
            boolean read = false;
            Thread.sleep(10);
            Settings.recursiveValidate();
            if (!renderFolder.exists()) if (!renderFolder.mkdir()) {
                throw new IOException("Could not create render folder");
            }
            appendLog("---Start rendering---");
            appendLog(" Image type: " + Settings.renderImageType);
            appendLog(" Size: " + Settings.renderWidth + "*" + Settings.renderHeight);
            appendLog(" Quality: " + Settings.renderQuality);
            appendLog(" Motion blur samples: " + Settings.renderMotionBlurSamples);
            appendLog(" Density estimation: " + Settings.renderDensityEstimationRadius + " radius, " + Settings.renderDensityEstimationMinimum + " minimum, " + Settings.renderDensityEstimationCurve + " curve");
            appendLog("[" + new java.util.Date().toString() + "] Templating...");
            setAction("Templating");
            java.util.Date today = new java.util.Date();
            String seqname = today.getTime() + ".seq";
            String outname = today.getTime() + "-out.seq";
            String templateName = today.getTime() + ".templ";
            PrintWriter pw = new PrintWriter(seqname);
            pw.print(sequence);
            pw.close();
            pw = new PrintWriter(templateName);
            pw.print(template);
            pw.close();
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(Settings.flam3Folder + "flam3-genome.exe");
            Map<String, String> env = pb.environment();
            env.put("template", templateName);
            env.put("clone_all", seqname);
            p = pb.start();
            OutputStream os = p.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            os.close();
            PrintWriter tfo = new PrintWriter(new FileOutputStream(outname));
            read = true;
            while (read) {
                String line = in.readLine();
                tfo.println(line);
                if (line.contains("</clone_all>")) {
                    read = false;
                }
            }
            in.close();
            tfo.close();
            read = er.ready();
            while (read) {
                appendLog(er.readLine());
                read = er.ready();
            }
            er.close();
            p.destroy();
            Utils.delete(new File(seqname));
            Utils.delete(new File(templateName));
            appendLog("[" + new java.util.Date().toString() + "] Templating finished");
            appendLog("[" + new java.util.Date().toString() + "] Rendering...");
            setAction("Rendering");
            pb = new ProcessBuilder();
            pb.command(Settings.flam3Folder + "flam3-animate.exe");
            env = pb.environment();
            env.put("in", outname);
            env.put("prefix", prefix);
            env.put("format", format);
            env.put("transparency", transparency);
            env.put("verbose", "1");
            p = pb.start();
            os = p.getOutputStream();
            er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            os.close();
            read = true;
            while (read) {
                String line = er.readLine();
                if (line == null) {
                    read = false;
                } else {
                    if (line.contains("time = ")) {
                        String rawFrames = line.substring(line.indexOf("time = ") + 7);
                        String[] times = rawFrames.split("/");
                        int frameNr = Integer.parseInt(times[0]) + 1;
                        int frameCount = Integer.parseInt(times[1]) + 1;
                        setFrame(frameNr + " / " + frameCount);
                    } else if (line.contains("chaos: ")) {
                        setProgress(line.substring(line.indexOf("chaos: ") + 7));
                    } else if (line.contains("density estimation")) {
                    } else if (line.contains("filtering")) {
                    } else if (line.length() == 0) {
                    } else appendLog(line);
                }
            }
            er.close();
            p.destroy();
            Utils.delete(new File(outname));
            appendLog("[" + new java.util.Date().toString() + "] Rendering finished");
            setAction("Completed");
            setFrame("N/A");
            setProgress("N/A");
            JOptionPane.showMessageDialog(new JFrame(), "Rendering completed.", "Rendering completed", JOptionPane.INFORMATION_MESSAGE);
            Debugger.appendLog("Succesfull: RenderLoggingDialog Thread");
        } catch (InterruptedException ex) {
            setAction("Error");
            setFrame("N/A");
            setProgress("N/A");
            appendLog(ex.getStackTrace().toString());
            Debugger.appendLog("Failed: RenderLoggingDialog Thread");
            Debugger.storeException(ex);
        } catch (FileNotFoundException ex) {
            setAction("Error");
            setFrame("N/A");
            setProgress("N/A");
            appendLog(ex.getStackTrace().toString());
            Debugger.appendLog("Failed: RenderLoggingDialog Thread");
            Debugger.storeException(ex);
        } catch (IOException ex) {
            setAction("Error");
            setFrame("N/A");
            setProgress("N/A");
            appendLog(ex.getStackTrace().toString());
            Debugger.appendLog("Failed: RenderLoggingDialog Thread");
            Debugger.storeException(ex);
        }
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        if (p != null) {
            p.destroy();
        }
    }
