    private void read(String filename, boolean initialise) {
        this.filename = filename;
        StringTokenizer token;
        try {
            RandomAccessFile project_file = new RandomAccessFile(filename, "r");
            String line = " ";
            line = project_file.readLine();
            line = project_file.readLine();
            if (line.indexOf("Version") > 0) {
                String verparts[] = line.split(" ");
                version = Double.parseDouble(verparts[2]);
                if (version == 1.0) {
                    if (JOptionPane.showConfirmDialog(Workbench.mainframe, "Do you want to convert to latest project file version", "Project file format conversion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        project_file.close();
                        upgradeToVersion1dot1();
                        project_file = new RandomAccessFile(filename, "r");
                        line = project_file.readLine();
                        line = project_file.readLine();
                        if (line.indexOf("Version") > 0) {
                            String nverparts[] = line.split(" ");
                            version = Double.parseDouble(nverparts[2]);
                        }
                    }
                }
                line = project_file.readLine();
            }
            while (!line.equals("# Components:")) line = project_file.readLine();
            line = project_file.readLine();
            while (line.charAt(0) != '#') {
                token = new StringTokenizer(line);
                String name;
                name = token.nextToken();
                String data = "";
                if (token.hasMoreTokens()) data = token.nextToken();
                while (token.hasMoreTokens()) data = data + " " + token.nextToken();
                if (initialise) components.addElement(new ComponentInfo(project, name, data)); else {
                    Boolean componentFound = false;
                    ComponentInfo temp;
                    for (int i = 0; i < availableComponents.size(); i++) {
                        temp = (ComponentInfo) availableComponents.getElementAt(i);
                        if (temp.name.equals(name)) {
                            components.addElement(new ComponentInfo(name, temp.disp_name, data, temp.pins));
                            componentFound = true;
                        }
                    }
                    if (!componentFound) {
                        JOptionPane.showMessageDialog(Workbench.mainframe, "We can't find the component " + name, "Project file error", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                }
                line = project_file.readLine();
            }
            while (!line.equals("# Connections:")) line = project_file.readLine();
            line = project_file.readLine();
            while (line.charAt(0) != '#') {
                token = new StringTokenizer(line, ":,", false);
                String src = token.nextToken();
                String src_pin = token.nextToken();
                String dest = token.nextToken();
                String dest_pin = token.nextToken();
                connections.addElement(new Connection(Integer.valueOf(src).intValue(), Integer.valueOf(src_pin).intValue(), Integer.valueOf(dest).intValue(), Integer.valueOf(dest_pin).intValue()));
                line = project_file.readLine();
            }
            while (!line.equals("# Connection info:")) line = project_file.readLine();
            line = project_file.readLine();
            for (int i = 0; line.charAt(0) != '#'; i++) {
                ((Connection) connections.getElementAt(i)).setConnectionInfo(line);
                line = project_file.readLine();
            }
            if (version >= 1.1) {
                try {
                    while (!line.equals("# Locations:")) line = project_file.readLine();
                    line = project_file.readLine();
                    while (line.charAt(0) != '#') {
                        ComponentInfo tempComp;
                        token = new StringTokenizer(line, ":,", false);
                        String compID = token.nextToken();
                        String compXPos = token.nextToken();
                        String compYPos = token.nextToken();
                        tempComp = (ComponentInfo) components.getElementAt(Integer.valueOf(compID).intValue());
                        tempComp.setLocation(new Point(Integer.valueOf(compXPos).intValue(), Integer.valueOf(compYPos).intValue()));
                        if (initialise) tempComp.comp.setLocation(tempComp.getLocation().x, tempComp.getLocation().y);
                        line = project_file.readLine();
                    }
                } catch (java.util.NoSuchElementException e) {
                    JOptionPane.showMessageDialog(Workbench.mainframe, "Error in Location information", "Project file format error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            } else for (int i = 0; i < components.size(); i++) ((ComponentInfo) components.getElementAt(Integer.valueOf(i).intValue())).setLocation(new Point(0, 0));
            if (!line.equals("# End")) {
                JOptionPane.showMessageDialog(Workbench.mainframe, "# End line not found", "Project file format error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            project_file.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Workbench.mainframe, "IO error whilst reading project file", "Project file format error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
