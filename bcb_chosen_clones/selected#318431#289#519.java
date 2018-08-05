    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == scanForImages) {
            JFileChooser chooser = (JFileChooser) objects.getObject(JFileChooser.class);
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File dir = chooser.getSelectedFile();
            if (!dir.isDirectory()) {
                dir = dir.getParentFile();
            }
            resyncImageSelection(dir);
        } else if (src == createPC) {
            try {
                File floppyImage = floppyDisk.getSelectedFile();
                File hardImage = hardDisk.getSelectedFile();
                File cdImage = cdrom.getSelectedFile();
                DriveSet.BootType bootType;
                if (floppyDisk.isBootDevice()) {
                    if (!floppyImage.exists()) {
                        alert("Floppy Image: " + floppyImage + " does not exist", "Boot", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    bootType = DriveSet.BootType.FLOPPY;
                } else if (hardDisk.isBootDevice()) {
                    if (!hardImage.exists()) {
                        alert("Hard disk Image: " + hardImage + " does not exist", "Boot", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    bootType = DriveSet.BootType.HARD_DRIVE;
                } else {
                    if (!cdImage.exists()) {
                        alert("CD Image: " + cdImage + " does not exist", "Boot", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    bootType = DriveSet.BootType.CDROM;
                }
                String[] args;
                int argc = 0;
                if (floppyImage != null) {
                    argc += 2;
                }
                if (hardImage != null) {
                    argc += 2;
                }
                if (cdImage != null) {
                    argc += 2;
                }
                if (argc > 2) {
                    argc += 2;
                }
                args = new String[argc];
                int pos = 0;
                if (floppyImage != null) {
                    args[pos++] = "-fda";
                    args[pos++] = floppyImage.getAbsolutePath();
                }
                if (hardImage != null) {
                    args[pos++] = "-hda";
                    args[pos++] = hardImage.getAbsolutePath();
                }
                if (cdImage != null) {
                    args[pos++] = "-cdrom";
                    args[pos++] = cdImage.getAbsolutePath();
                }
                if (pos <= (argc - 2)) {
                    args[pos++] = "-boot";
                    if (bootType == DriveSet.BootType.HARD_DRIVE) {
                        args[pos++] = "hda";
                    } else if (bootType == DriveSet.BootType.CDROM) {
                        args[pos++] = "cdrom";
                    } else {
                        args[pos++] = "fda";
                    }
                }
                instance.createPC(args);
                resyncImageSelection(new File(System.getProperty("user.dir")));
            } catch (Exception e) {
                alert("Failed to create PC: " + e, "Boot", JOptionPane.ERROR_MESSAGE);
            }
        } else if (src == loadSnapshot) {
            runMenu.stop();
            JFileChooser fc = new JFileChooser();
            try {
                BufferedReader in = new BufferedReader(new FileReader("prefs.txt"));
                String path = in.readLine();
                in.close();
                if (path != null) {
                    File f = new File(path);
                    if (f.isDirectory()) {
                        fc.setCurrentDirectory(f);
                    }
                }
            } catch (Exception e) {
            }
            int returnVal = fc.showDialog(this, "Load JPC Snapshot");
            File file = fc.getSelectedFile();
            try {
                if (file != null) {
                    BufferedWriter out = new BufferedWriter(new FileWriter("prefs.txt"));
                    out.write(file.getPath());
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (returnVal == 0) {
                try {
                    System.out.println("Loading a snapshot of JPC");
                    ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
                    zin.getNextEntry();
                    ((PC) objects.getObject(PC.class)).loadState(zin);
                    zin.closeEntry();
                    ((PCMonitorFrame) objects.getObject(PCMonitorFrame.class)).resizeDisplay();
                    zin.getNextEntry();
                    ((PCMonitorFrame) objects.getObject(PCMonitorFrame.class)).loadMonitorState(zin);
                    zin.closeEntry();
                    zin.close();
                    System.out.println("done");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (src == saveSnapshot) {
            runMenu.stop();
            JFileChooser fc = new JFileChooser();
            try {
                BufferedReader in = new BufferedReader(new FileReader("prefs.txt"));
                String path = in.readLine();
                in.close();
                if (path != null) {
                    File f = new File(path);
                    if (f.isDirectory()) {
                        fc.setCurrentDirectory(f);
                    }
                }
            } catch (Exception e) {
            }
            int returnVal = fc.showDialog(this, "Save JPC Snapshot");
            File file = fc.getSelectedFile();
            try {
                if (file != null) {
                    BufferedWriter out = new BufferedWriter(new FileWriter("prefs.txt"));
                    out.write(file.getPath());
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (returnVal == 0) {
                try {
                    ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(file));
                    zip.putNextEntry(new ZipEntry("pc"));
                    ((PC) objects.getObject(PC.class)).saveState(zip);
                    zip.closeEntry();
                    zip.putNextEntry(new ZipEntry("monitor"));
                    ((PCMonitorFrame) objects.getObject(PCMonitorFrame.class)).saveState(zip);
                    zip.closeEntry();
                    zip.finish();
                    zip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (src == processorFrame) {
            ProcessorFrame pf = (ProcessorFrame) objects.getObject(ProcessorFrame.class);
            if (pf != null) {
                bringToFront(pf);
            } else {
                pf = new ProcessorFrame();
                addInternalFrame(desktop, 10, 10, pf);
            }
        } else if (src == physicalMemoryViewer) {
            MemoryViewer mv = (MemoryViewer) objects.getObject(MemoryViewer.class);
            if (mv != null) {
                bringToFront(mv);
            } else {
                mv = new MemoryViewer("Physical Memory");
                addInternalFrame(desktop, 360, 50, mv);
            }
        } else if (src == linearMemoryViewer) {
            LinearMemoryViewer lmv = (LinearMemoryViewer) objects.getObject(LinearMemoryViewer.class);
            if (lmv != null) {
                bringToFront(lmv);
            } else {
                lmv = new LinearMemoryViewer("Linear Memory");
                addInternalFrame(desktop, 360, 50, lmv);
            }
        } else if (src == breakpoints) {
            BreakpointsFrame bp = (BreakpointsFrame) objects.getObject(BreakpointsFrame.class);
            if (bp != null) {
                bringToFront(bp);
            } else {
                bp = new BreakpointsFrame();
                addInternalFrame(desktop, 550, 360, bp);
            }
        } else if (src == watchpoints) {
            WatchpointsFrame wp = (WatchpointsFrame) objects.getObject(WatchpointsFrame.class);
            if (wp != null) {
                bringToFront(wp);
            } else {
                wp = new WatchpointsFrame();
                addInternalFrame(desktop, 550, 360, wp);
            }
        } else if (src == opcodeFrame) {
            OpcodeFrame op = (OpcodeFrame) objects.getObject(OpcodeFrame.class);
            if (op != null) {
                bringToFront(op);
            } else {
                op = new OpcodeFrame();
                addInternalFrame(desktop, 100, 200, op);
            }
        } else if (src == traceFrame) {
            ExecutionTraceFrame tr = (ExecutionTraceFrame) objects.getObject(ExecutionTraceFrame.class);
            if (tr != null) {
                bringToFront(tr);
            } else {
                tr = new ExecutionTraceFrame();
                addInternalFrame(desktop, 30, 100, tr);
            }
        } else if (src == monitor) {
            PCMonitorFrame m = (PCMonitorFrame) objects.getObject(PCMonitorFrame.class);
            if (m != null) {
                bringToFront(m);
            } else {
                m = new PCMonitorFrame();
                addInternalFrame(desktop, 30, 30, m);
            }
        }
        refresh();
    }
