    ConcreteProject(String filename) {
        this.filename = filename;
        Workbench.addToTab("Project", new ProjectTab(this), false);
        project_clock = 0;
        StringTokenizer token;
        int nr_comps = 0;
        int nr_connects = 0;
        try {
            RandomAccessFile project_file = new RandomAccessFile(filename, "r");
            String line = " ";
            line = project_file.readLine();
            line = project_file.readLine();
            if (line.indexOf("Version") > 0) {
                String verparts[] = line.split(" ");
                version = Double.parseDouble(verparts[2]);
                line = project_file.readLine();
            }
            line = project_file.readLine();
            while (!(line.charAt(0) == '#')) {
                line = project_file.readLine();
                nr_comps++;
            }
            line = project_file.readLine();
            while (!(line.charAt(0) == '#')) {
                line = project_file.readLine();
                nr_connects++;
            }
            comps = new Component[nr_comps];
            connects = new Connection[nr_connects];
            comp_types = new String[nr_comps];
            comp_data = new String[nr_comps];
            project_file.seek(0);
            while (!line.equals("# Components:")) line = project_file.readLine();
            for (int i = 0; i < nr_comps; i++) {
                token = new StringTokenizer(project_file.readLine());
                comp_types[i] = token.nextToken();
                if (token.hasMoreTokens()) comp_data[i] = token.nextToken();
                while (token.hasMoreTokens()) comp_data[i] = comp_data[i] + " " + token.nextToken();
            }
            createComponents();
            while (!line.equals("# Connections:")) line = project_file.readLine();
            for (int i = 0; i < nr_connects; i++) {
                token = new StringTokenizer(project_file.readLine(), ":,", false);
                String src = token.nextToken();
                String src_pin = token.nextToken();
                String dest = token.nextToken();
                String dest_pin = token.nextToken();
                connects[i] = new Connection(comps[Integer.valueOf(src).intValue()], Integer.valueOf(src_pin).intValue(), comps[Integer.valueOf(dest).intValue()], Integer.valueOf(dest_pin).intValue());
            }
            if (version >= 1.0) {
                while (!line.equals("# Connection info:")) line = project_file.readLine();
                for (int i = 0; i < nr_connects; i++) connectioninfo.addElement(project_file.readLine());
                infowindow = new ConnectionInfo(connectioninfo);
            }
        } catch (IOException e) {
            System.out.println("Error reading project file");
        }
        bottompanel = new BottomPanel(this, filename);
        postPinUpdate(null);
    }
