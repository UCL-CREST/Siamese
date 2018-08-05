    public void googleImageSearch(String start) {
        try {
            String u = "http://images.google.com/images?q=" + custom + start;
            if (u.contains(" ")) {
                u = u.replace(" ", "+");
            }
            URL url = new URL(u);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
            BufferedReader readIn = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            googleImages.clear();
            String text = "";
            String lin = "";
            while ((lin = readIn.readLine()) != null) {
                text += lin;
            }
            readIn.close();
            if (text.contains("\n")) {
                text = text.replace("\n", "");
            }
            String[] array = text.split("\\Qhref=\"/imgres?imgurl=\\E");
            for (String s : array) {
                if (s.startsWith("http://") || s.startsWith("https://") && s.contains("&amp;")) {
                    String s1 = s.substring(0, s.indexOf("&amp;"));
                    googleImages.add(s1);
                }
            }
        } catch (Exception ex4) {
            MusicBoxView.showErrorDialog(ex4);
        }
        jButton4.setEnabled(true);
        jButton2.setEnabled(true);
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
