    public void loadProject() {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            newProject();
            if (debug > 4) System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            String filePath = chooser.getCurrentDirectory() + "/" + chooser.getSelectedFile().getName();
            BufferedReader rd;
            java.util.List lines = new LinkedList();
            File file;
            String line;
            try {
                rd = new BufferedReader(new FileReader(filePath));
                while ((line = rd.readLine()) != null) {
                    lines.add(line);
                }
                Iterator it = lines.iterator();
                while (it.hasNext()) {
                    line = (String) it.next();
                    parse(line);
                    display_code_textarea.append(line + "\n");
                    if (debug > 4) System.out.println(line);
                }
                rd.close();
            } catch (FileNotFoundException fnfe) {
                System.err.println("File not found: ");
                return;
            } catch (IOException ioe) {
                System.err.println("IO Exception reading file: ");
                System.exit(1);
            }
        }
    }
