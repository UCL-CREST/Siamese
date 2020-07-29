    private void captureDialog(AWTEvent trigger) {
        String pass = "Pass F1 Keystroke for Replay";
        String sAS = "Save Application Window";
        String cF = "Copy File";
        String rP = "Run process";
        String comm = "Add a Comment";
        String cmdstring;
        String savedfilename;
        int i;
        int rc;
        int x;
        int y;
        int w;
        int h;
        Robot robbie;
        BufferedImage newBufferedImage;
        Rectangle rect;
        String[] bgrp = new String[5];
        bgrp[0] = pass;
        bgrp[1] = sAS;
        bgrp[2] = cF;
        bgrp[3] = rP;
        bgrp[4] = comm;
        cmdstring = (String) JOptionPane.showInputDialog(this, "Choose option required", "PGUI Capture Program Function Selection", JOptionPane.PLAIN_MESSAGE, null, bgrp, bgrp[0]);
        if (cmdstring.equals(pass)) {
            sendPGUI(trigger.paramString());
        } else if (cmdstring.equals(sAS)) {
            try {
                robbie = new Robot();
                Point p = upThere.getLocation();
                Dimension d = upThere.getSize();
                x = p.x;
                y = p.y;
                w = (int) d.width;
                h = (int) d.height;
                rect = new Rectangle(x, y, w, h);
                newBufferedImage = robbie.createScreenCapture(rect);
                savedfilename = writeJPEG(newBufferedImage);
                sendPGUI("SC " + x + " " + y + " " + w + " " + h + " " + savedfilename);
            } catch (Exception e) {
                String s = new String("System cannot save image; exception " + e);
                log.println(s);
                JOptionPane.showMessageDialog(null, s, "SevereError", JOptionPane.WARNING_MESSAGE);
            }
        } else if (cmdstring.equals(comm)) {
            cmdstring = (String) JOptionPane.showInputDialog(this, "Comment Text", "Add a Comment at current position in script", JOptionPane.PLAIN_MESSAGE, null, null, "");
            if (cmdstring.length() != 0) {
                sendPGUI("* " + cmdstring);
            }
        } else if (cmdstring.equals(cF)) {
            cmdstring = getFileName("File to be Saved");
            savedfilename = getFileName("Name to be saved as");
            rc = JOptionPane.showConfirmDialog(null, "Save " + cmdstring + " as file " + savedfilename, "Please Confirm File Copy Function", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (rc == JOptionPane.YES_OPTION) {
                try {
                    BufferedReader bfr;
                    BufferedWriter bfw;
                    bfr = new BufferedReader(new FileReader(cmdstring));
                    bfw = new BufferedWriter(new FileWriter(savedfilename));
                    char[] cbuffer = new char[8192];
                    i = bfr.read(cbuffer, 0, 8192);
                    while (i != -1) {
                        bfw.write(cbuffer, 0, i);
                        i = bfr.read(cbuffer, 0, 8192);
                    }
                    bfr.close();
                    bfw.close();
                    sendPGUI("FC " + cmdstring + " " + savedfilename);
                } catch (Exception e) {
                    String s = new String("System cannot copy " + cmdstring + " to " + savedfilename + "; exception " + e);
                    log.println(s);
                    JOptionPane.showMessageDialog(null, s, "SevereError", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (cmdstring.equals(rP)) {
            cmdstring = (String) JOptionPane.showInputDialog(null, "Enter Process Invocation ", "PGUI Capture Program - Run Process ", JOptionPane.PLAIN_MESSAGE, null, null, "");
            try {
                Runtime thisRT = Runtime.getRuntime();
                thisRT.exec(cmdstring);
                sendPGUI("PI " + cmdstring);
            } catch (Exception e) {
                String s = new String("System cannot run application " + cmdstring + "; exception " + e);
                log.println(s);
                JOptionPane.showMessageDialog(null, s, "SevereError", JOptionPane.WARNING_MESSAGE);
            }
        }
        return;
    }
