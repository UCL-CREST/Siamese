    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (commandVal != 0) {
            commandVal = -1;
            while (commandVal == -1) try {
                Thread.sleep(100);
            } catch (InterruptedException d) {
            }
        } else if (command.equals("Flip Horizontal")) {
            cImage.flip(0);
            if (cImage.flipx == 0) cImage.flipx = 1; else cImage.flipx = 0;
        } else if (command.equals("Flip Vertical")) {
            cImage.flip(1);
            if (cImage.flipy == 0) cImage.flipy = 1; else cImage.flipy = 0;
        } else if (command.equals("Red Channel")) cImage.setChannel(0); else if (command.equals("Green Channel")) cImage.setChannel(1); else if (command.equals("Blue Channel")) cImage.setChannel(2); else if (command.equals("All Channels")) cImage.setChannel(4); else if (command.equals("Quit")) System.exit(0); else if (command.equals("About")) {
            myAbout.about_f.pack();
            myAbout.about_d.setVisible(true);
        } else if (command.equals("STOP")) {
            status_t.setText("Stopping...");
            commandVal = -1;
        } else if (command.equals("Send")) {
            mySerial.write(status_t.getText() + "\r");
            status_t.setText("Command Sent");
        } else if (command.equals("Config Servos")) {
            status_t.setText("Servo Configure Called");
            mySerial.write("sp " + pan_range_far.getText() + " " + pan_range_near.getText() + " " + pan_step.getText() + " " + tilt_range_far.getText() + " " + tilt_range_near.getText() + " " + tilt_step.getText());
            if (mySerial.readACK(1) == 1) status_t.setText("Servos Set."); else status_t.setText("Servo Config Failed.");
        } else if (command.equals("Clear")) {
            status_t.setText("");
        } else if (command.equals("RESET")) {
            commandVal = 0;
            as_c.setSelectedIndex(0);
            hr_c.setSelectedIndex(0);
            lm_c.setSelectedIndex(0);
            whiteBalance.setSelectedIndex(0);
            autoGain.setSelectedIndex(0);
            color_c.setSelectedIndex(0);
            bm_c.setSelectedIndex(0);
            hd_c.setSelectedIndex(0);
            ti_c.setSelectedIndex(0);
            pd_c.setSelectedIndex(0);
            dc_c.setSelectedIndex(1);
            mySerial.readACK(0);
            setWindowParams();
            mySerial.write("rs\r");
            mySerial.readACK(0);
            status_t.setText("Camera Reset...");
        } else if (command.equals("Grab Frame")) {
            if (fs_c.getSelectedIndex() == 1) {
                int chan = sf_chan_c.getSelectedIndex();
                if (chan == 0) mySerial.write("sf\r"); else {
                    mySerial.write("sf " + (chan - 1) + "\r");
                }
                commandVal = 5;
            } else commandVal = 6;
        } else if (command.equals("Save Frame")) {
            JFileChooser save_d = new JFileChooser();
            int result = save_d.showSaveDialog(new JFrame("Type the name of the File to save"));
            File saveFile = save_d.getSelectedFile();
            if (result == JFileChooser.APPROVE_OPTION) {
                cImage.writeImage(saveFile + ".jpg");
            }
        } else if (command.equals("clear")) {
            cImage.objnum = 0;
            cImage.my_drive = 0;
            cImage.target_x = 0;
            cImage.target_y = 0;
            CameraImage.objDirt[0] = 0;
            CameraImage.objDirt[1] = 0;
            CameraImage.objDirt[2] = 0;
            cImage.repaint();
        } else if (command.equals("Get Mean")) {
            status_t.setText("Get Mean");
            if (mySerial.readACK(0) == 0) {
                status_t.setText("Get Mean Failed");
                return;
            }
            mySerial.write("gm\r");
            MainWindow.status_t.setText("Get Mean ");
            commandVal = 4;
        } else if (command.equals("read frame")) {
            mySerial.write("rf");
            if (mySerial.readACK(0) == 0) {
                MainWindow.status_t.setText("Read Frame Failed");
                return;
            }
            MainWindow.status_t.setText("New Frame Loaded...");
        } else if (command.equals("Load Frame")) {
            if (mySerial.readACK(0) == 0) {
                status_t.setText("Load Frame Failed");
                return;
            }
            mySerial.write("lf");
            if (frame_diff_linemode == 3) {
                mySerial.write("\r");
                myMotion.trackMotion(hd_c.getSelectedIndex());
            } else if (mySerial.readACK(1) == 0) {
                MainWindow.status_t.setText("Load Frame Failed");
                return;
            }
            MainWindow.status_t.setText("Frame Loaded...");
        } else if (command.equals("Set Window")) {
            status_t.setText("Set Virtual Window Called");
            mySerial.write("vw " + x1.getText() + " " + y1.getText() + " " + x2.getText() + " " + y2.getText());
            if (mySerial.readACK(1) == 1) status_t.setText("Virtual Window Set."); else status_t.setText("Virtual Window Failed.");
        } else if (command.equals("Down Sample")) {
            status_t.setText("Down Sampling Called");
            mySerial.write("ds " + dsx.getText() + " " + dsy.getText());
            if (mySerial.readACK(1) == 1) status_t.setText("Down Sampling Set."); else status_t.setText("Down Sampling Failed.");
        } else if (command.equals("Track Color")) {
            status_t.setText("Track Color Called");
            if (mySerial.readACK(0) == 0) {
                status_t.setText("Tracking Failed");
                return;
            }
            mySerial.write("tc " + rmin_t.getText() + " " + rmax_t.getText() + " " + gmin_t.getText() + " " + gmax_t.getText() + " " + bmin_t.getText() + " " + bmax_t.getText() + "\r");
            MainWindow.status_t.setText("Tracking Color ");
            commandVal = 3;
        } else if (command.equals("Track Window")) {
            status_t.setText("Track Window Called");
            if (mySerial.readACK(0) == 0) {
                status_t.setText("Tracking Failed");
                return;
            }
            mySerial.write("tw\r");
            setWindowParams();
            MainWindow.status_t.setText("Tracking Window ");
            commandVal = 3;
        } else if (command.equals("Update ALL")) {
            UpdateAllCommands();
        } else if (command.equals("Set NF")) {
            status_t.setText("Setting Noise Filter");
            mySerial.write("nf " + nf_t.getText());
            if (mySerial.readACK(1) == 1) status_t.setText("Noise Filter Set."); else status_t.setText("Noise Filter Failed.");
        } else if (command.equals("Frame Diff")) {
            status_t.setText("Frame Diff Called");
            if (mySerial.readACK(0) == 0) {
                status_t.setText("Tracking Failed");
                return;
            }
            mySerial.write("fd " + thresh_t.getText() + "\r");
            MainWindow.status_t.setText("Motion Track " + thresh_t.getText());
            commandVal = 1;
        } else if (command.equals("Get Histogram")) {
            status_t.setText("Get Histogram");
            if (mySerial.readACK(0) == 0) {
                status_t.setText("Histogram Failed");
                return;
            }
            mySerial.write("gh " + channel_c.getSelectedIndex() + "\r");
            MainWindow.status_t.setText("Get Histogram " + channel_c.getSelectedIndex());
            commandVal = 2;
        }
    }
