    String[] doOpen() {
        JFileChooser fcOpen = new JFileChooser();
        fcOpen.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fcOpen.setAcceptAllFileFilterUsed(false);
        fcOpen.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String s = f.getName();
                int i = s.lastIndexOf('.');
                if (i > 0 && i < s.length() - 1) {
                    String ext;
                    ext = s.substring(i + 1).toLowerCase();
                    if (ext.equals("class")) return true;
                }
                return false;
            }

            public String getDescription() {
                return "Accepts .class files";
            }
        });
        if (fcOpen.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return null;
        File file = fcOpen.getSelectedFile();
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if (i != -1) name = name.substring(0, i);
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("javap -v " + name);
            ArrayList<String> linesBuffer = new ArrayList<String>();
            InputStream is = process.getInputStream();
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = is.read()) != -1) if (ch == '\r') continue; else if (ch == '\n') {
                if (sb.length() == 0) sb.append(' ');
                linesBuffer.add(sb.toString());
                sb.setLength(0);
            } else sb.append((char) ch);
            String[] lines = linesBuffer.toArray(new String[0]);
            for (i = 0; i < lines.length; i++) lines[i] = lines[i].replace('\t', ' ');
            return lines;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return null;
        }
    }
