    private void googleImageSearch() {
        bottomShowing = true;
        googleSearched = true;
        googleImageLocation = 0;
        googleImages = new Vector<String>();
        custom = "";
        int r = JOptionPane.showConfirmDialog(this, "Customize google search?", "Google Search", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            custom = JOptionPane.showInputDialog("Custom Search", "");
        } else {
            custom = artist;
        }
        try {
            String u = "http://images.google.com/images?q=" + custom;
            if (u.contains(" ")) {
                u = u.replace(" ", "+");
            }
            URL url = new URL(u);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
            BufferedReader readIn = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            googleImages.clear();
            String lin = new String();
            while ((lin = readIn.readLine()) != null) {
                while (lin.contains("href=\"/imgres?imgurl=")) {
                    while (!lin.contains(">")) {
                        lin += readIn.readLine();
                    }
                    String s = lin.substring(lin.indexOf("href=\"/imgres?imgurl="), lin.indexOf(">", lin.indexOf("href=\"/imgres?imgurl=")));
                    lin = lin.substring(lin.indexOf(">", lin.indexOf("href=\"/imgres?imgurl=")));
                    if (s.contains("&amp;") && s.indexOf("http://") < s.indexOf("&amp;")) {
                        s = s.substring(s.indexOf("http://"), s.indexOf("&amp;"));
                    } else {
                        s = s.substring(s.indexOf("http://"), s.length());
                    }
                    googleImages.add(s);
                }
            }
            readIn.close();
        } catch (Exception ex4) {
            MusicBoxView.showErrorDialog(ex4);
        }
        jButton1.setEnabled(false);
        getContentPane().remove(jLabel1);
        ImageIcon icon;
        try {
            icon = new ImageIcon(new URL(googleImages.elementAt(googleImageLocation)));
            int h = icon.getIconHeight();
            int w = icon.getIconWidth();
            jLabel1.setSize(w, h);
            jLabel1.setIcon(icon);
            add(jLabel1, BorderLayout.CENTER);
        } catch (MalformedURLException ex) {
            MusicBoxView.showErrorDialog(ex);
            jLabel1.setIcon(MusicBoxView.noImage);
        }
        add(jPanel1, BorderLayout.PAGE_END);
        pack();
    }
