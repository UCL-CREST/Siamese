    public static void checkForUpdates() {
        try {
            URL fileURL = new URL("http://acts202.sourceforge.net/update/LATEST.txt");
            InputStream file = fileURL.openStream();
            File dest = new File("newestVersion");
            OutputStream output = new FileOutputStream(dest);
            byte[] temp = new byte[400];
            file.read(temp);
            output.write(temp);
            file.close();
            output.close();
            BufferedReader current = new BufferedReader(new FileReader("version"));
            BufferedReader latest = new BufferedReader(new FileReader("newestVersion"));
            String currentV = current.readLine().trim();
            String latestV = latest.readLine().trim();
            Double currentVer = Double.valueOf(currentV);
            Double latestVer = Double.valueOf(latestV);
            if (currentVer < latestVer) {
                final JFrame d = new JFrame("Acts 20:2 Update");
                JButton b = new JButton("New version available: " + latestV);
                b.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(new URI("http://acts202.sourceforge.net"));
                                d.dispose();
                            } catch (IOException e1) {
                            } catch (URISyntaxException e2) {
                            }
                        } else {
                        }
                    }
                });
                d.add(b);
                d.pack();
                d.setVisible(true);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
