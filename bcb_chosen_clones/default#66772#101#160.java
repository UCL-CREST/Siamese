    public boolean onStart() {
        log("Starting up, this may take a minute...");
        gui = new ApeAtollGUI();
        gui.setVisible(true);
        while (waitGUI) {
            sleep(100);
        }
        URLConnection url = null;
        BufferedReader in = null;
        BufferedWriter out = null;
        if (checkUpdates) {
            try {
                url = new URL("http://www.voltrex.be/rsbot/VoltrexApeAtollVERSION.txt").openConnection();
                in = new BufferedReader(new InputStreamReader(url.getInputStream()));
                if (Double.parseDouble(in.readLine()) > properties.version()) {
                    if (JOptionPane.showConfirmDialog(null, "Update found. Do you want to update?") == 0) {
                        JOptionPane.showMessageDialog(null, "Please choose 'VoltrexApeAtoll.java' in your scripts/sources folder.");
                        JFileChooser fc = new JFileChooser();
                        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            url = new URL("http://www.voltrex.be/rsbot/VoltrexApeAtoll.java").openConnection();
                            in = new BufferedReader(new InputStreamReader(url.getInputStream()));
                            out = new BufferedWriter(new FileWriter(fc.getSelectedFile().getPath()));
                            String inp;
                            while ((inp = in.readLine()) != null) {
                                out.write(inp);
                                out.newLine();
                                out.flush();
                            }
                            log("Script successfully downloaded. Please recompile.");
                            return false;
                        } else log("Update canceled");
                    } else log("Update canceled");
                } else log("You have the latest version.");
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                log("Problem getting version. Please report this bug!");
            }
        }
        try {
            BKG = ImageIO.read(new URL("http://i54.tinypic.com/2egcfaw.jpg"));
        } catch (final java.io.IOException e) {
            e.printStackTrace();
        }
        try {
            final URL cursorURL = new URL("http://imgur.com/i7nMG.png");
            final URL cursor80URL = new URL("http://imgur.com/8k9op.png");
            normal = ImageIO.read(cursorURL);
            clicked = ImageIO.read(cursor80URL);
        } catch (MalformedURLException e) {
            log.info("Unable to buffer cursor.");
        } catch (IOException e) {
            log.info("Unable to open cursor image.");
        }
        scriptStartTime = System.currentTimeMillis();
        mouse.setSpeed(MouseSpeed);
        camera.setPitch(true);
        log("You are using Voltrex Ape Atoll agility course.");
        return true;
    }
