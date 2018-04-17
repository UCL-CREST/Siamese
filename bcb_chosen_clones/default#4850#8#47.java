    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "1. Select file to convert\n2. Select output file\n3. ???\n4. Profit!", "SC++ Converter", JOptionPane.DEFAULT_OPTION);
        JFileChooser jfc = new JFileChooser("c:\\");
        jfc.showOpenDialog(null);
        File file = jfc.getSelectedFile();
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException ex) {
        }
        ArrayList lines = new ArrayList();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            int index = line.indexOf("main(String[] args");
            if (index != -1) {
                lines.add("static SenezConsole sc=new SenezConsole();");
                lines.add(line);
            } else {
                index = line.indexOf("System.out");
                if (index != -1) {
                    lines.add("//" + line);
                    lines.add("sc" + line.substring(index + 10));
                } else {
                    lines.add(line);
                }
            }
        }
        jfc.showSaveDialog(null);
        file = jfc.getSelectedFile();
        PrintStream ps = null;
        try {
            ps = new PrintStream(file);
        } catch (FileNotFoundException ex1) {
        }
        for (int i = 0; i < lines.size(); i++) {
            ps.println(lines.get(i));
        }
        ps.close();
        JOptionPane.showMessageDialog(null, "File Converted and saved as\n" + file.getPath(), "Done", JOptionPane.DEFAULT_OPTION);
    }
