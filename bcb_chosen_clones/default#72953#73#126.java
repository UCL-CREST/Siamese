    public ArrayList<Flame> rotate(RotateDialog rotate, String domTree) {
        ProgressBarDialog pBar = new ProgressBarDialog(new JFrame(), "Calculating rotation");
        try {
            Debugger.appendAction("Starting rotate");
            java.util.Date today = new java.util.Date();
            String flamename = today.getTime() + ".flame";
            String seqname = today.getTime() + ".seq";
            PrintWriter print = new PrintWriter(flamename);
            print.print(domTree);
            print.close();
            new Thread(pBar).start();
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(Settings.flam3Folder + "flam3-genome.exe");
            Map<String, String> env = pb.environment();
            env.put("nframes", Integer.toString(rotate.getNframes()));
            env.put("sequence", flamename);
            env.put("verbose", "1");
            env.put("prefix", seqname);
            Process p = pb.start();
            OutputStream os = p.getOutputStream();
            BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            PrintWriter pw = new PrintWriter(os);
            pw.close();
            os.close();
            er.close();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Flame rotated = new Flame(docBuilder.parse(p.getInputStream()), renderQueue);
            p.waitFor();
            p.destroy();
            String fileName = System.getProperty("user.dir") + "\\" + flamename;
            Utils.delete(new File(fileName));
            pBar.setVisible(false);
            ArrayList<Flame> rawFlames = rotated.split();
            ArrayList<Flame> flames = new ArrayList<Flame>();
            for (int i = 0; i <= rotate.getEndFrame(); i++) {
                flames.add(rawFlames.get(i));
            }
            Debugger.appendLog("Succesfull: rotate");
            Debugger.appendAction("Ending rotate");
            return flames;
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
        Debugger.appendLog("Failed: rotate");
        return null;
    }
