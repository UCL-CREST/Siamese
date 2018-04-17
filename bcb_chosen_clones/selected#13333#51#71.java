        private void initialize() {
            List providers = new ArrayList();
            while (this.urls.hasMoreElements()) {
                URL url = (URL) this.urls.nextElement();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        String provider = uncommentLine(line).trim();
                        if (provider != null && provider.length() > 0) {
                            providers.add(provider);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.iterator = providers.iterator();
        }
