    public void setPage(String url) {
        System.out.println("SetPage(" + url + ")");
        if (url != null) {
            if (!url.startsWith("http://")) {
                url = "http://" + url;
            }
            boolean exists = false;
            for (int i = 0; i < urlComboBox.getItemCount(); i++) {
                if (((String) urlComboBox.getItemAt(i)).equals(url)) {
                    exists = true;
                    urlComboBox.setSelectedItem(url);
                }
            }
            if (!exists) {
                int i = urlComboBox.getSelectedIndex();
                if (i == -1 || urlComboBox.getItemCount() == 0) {
                    i = 0;
                } else {
                    i++;
                }
                urlComboBox.insertItemAt(url, i);
                urlComboBox.setSelectedItem(url);
            }
            boolean image = false;
            for (final String element : imageExtensions) {
                if (url.endsWith(element)) {
                    image = true;
                }
            }
            try {
                if (image) {
                    final String html = "<html><img src=\"" + url + "\"></html>";
                } else {
                    final String furl = url;
                    Runnable loadPage = new Runnable() {

                        public void run() {
                            try {
                                System.out.println("Setting page on Cobra");
                                SimpleHtmlRendererContext rendererContext = new SimpleHtmlRendererContext(htmlPanel, new SimpleUserAgentContext());
                                int nodeBaseEnd = furl.indexOf("/", 10);
                                if (nodeBaseEnd == -1) nodeBaseEnd = furl.length();
                                String nodeBase = furl.substring(0, nodeBaseEnd);
                                InputStream pageStream = new URL(furl).openStream();
                                BufferedReader pageStreamReader = new BufferedReader(new InputStreamReader(pageStream));
                                String pageContent = "";
                                String line;
                                while ((line = pageStreamReader.readLine()) != null) pageContent += line;
                                pageContent = borderImages(pageContent, nodeBase);
                                htmlPanel.setHtml(pageContent, furl, rendererContext);
                            } catch (Exception e) {
                                System.out.println("Error loading page " + furl + " : " + e);
                            }
                        }
                    };
                    new Thread(loadPage).start();
                }
            } catch (final Throwable exception) {
                System.out.println("Error in Browser.setPage(): " + exception);
            }
        }
    }
