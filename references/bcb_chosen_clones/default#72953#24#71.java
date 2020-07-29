    public ArrayList<Flame> tween(TweenDialog tween) {
        ProgressBarDialog pBar = new ProgressBarDialog(new JFrame(), "Calculating tween");
        try {
            Debugger.appendAction("Starting tween");
            java.util.Date today = new java.util.Date();
            String seqname = today.getTime() + ".seq";
            PrintWriter pw = new PrintWriter(seqname);
            pw.print(tween.getFlameFile());
            pw.close();
            new Thread(pBar).start();
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(Settings.flam3Folder + "flam3-genome.exe");
            Map<String, String> env = pb.environment();
            env.put("nframes", Integer.toString(tween.getNframes()));
            env.put("sequence", seqname);
            env.put("loops", "0");
            env.put("verbose", "1");
            env.put("prefix", seqname);
            Process p = pb.start();
            OutputStream os = p.getOutputStream();
            BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            pw.close();
            os.close();
            er.close();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Flame tweened = new Flame(docBuilder.parse(p.getInputStream()), renderQueue);
            p.waitFor();
            p.destroy();
            String fileName = System.getProperty("user.dir") + "\\" + seqname;
            Utils.delete(new File(fileName));
            pBar.setVisible(false);
            Debugger.appendLog("Succesfull: tween");
            Debugger.appendAction("Ending tween");
            return tweened.split();
        } catch (SAXException ex) {
            Debugger.storeException(ex);
        } catch (IOException ex) {
            Debugger.storeException(ex);
        } catch (ParserConfigurationException ex) {
            Debugger.storeException(ex);
        } catch (InterruptedException ex) {
            Debugger.storeException(ex);
        }
        pBar.setVisible(false);
        Debugger.appendLog("Failed: tween");
        return null;
    }
