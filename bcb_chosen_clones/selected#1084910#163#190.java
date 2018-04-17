            @Override
            public void run() {
                try {
                    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
                    InputStream fis = url.openStream();
                    InputStreamReader r = new InputStreamReader(fis, "UTF-8");
                    BufferedReader br = new BufferedReader(r);
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while (null != (line = br.readLine())) {
                        sb.append(line);
                        sb.append("\r\n");
                    }
                    br.close();
                    r.close();
                    fis.close();
                    final String text = sb.toString();
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            MainPanel.this.source.setText(text);
                            MainPanel.this.source.setCaretPosition(0);
                        }
                    });
                } catch (Exception ex) {
                }
            }
