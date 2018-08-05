        @SuppressWarnings("unchecked")
        private List<String> getLogFile() {
            String homeServer = "";
            Realm realm = null;
            if (null == node) {
                if (null != System.getProperty("ThinClientManager.server.Codebase")) try {
                    homeServer = new URL(System.getProperty("ThinClientManager.server.Codebase")).getHost();
                } catch (final MalformedURLException e1) {
                    e1.printStackTrace();
                }
            } else {
                realm = (Realm) node.getLookup().lookup(Realm.class);
                if (null != realm.getSchemaProviderName()) homeServer = realm.getSchemaProviderName(); else if (null != realm.getConnectionDescriptor().getHostname()) homeServer = realm.getConnectionDescriptor().getHostname();
            }
            if (homeServer.length() == 0) homeServer = "localhost";
            try {
                final URL url = new URL("http", homeServer, 8080, fileName);
                final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                final ArrayList<String> lines = new ArrayList<String>();
                String line;
                if (isClient) {
                    while ((line = br.readLine()) != null) if (line.contains(macAdress)) lines.add(line);
                    if (lines.size() == 0) lines.add(Messages.getString("LogDetailView.getLogFile.NoEntrysForTC", macAdress));
                } else while ((line = br.readLine()) != null) lines.add(line);
                br.close();
                if (lines.size() == 0) lines.add(Messages.getString("LogDetailView.getLogFile.NoEntrys"));
                return lines;
            } catch (final MalformedURLException e) {
                e.printStackTrace();
                ErrorManager.getDefault().notify(e);
            } catch (final IOException e) {
                e.printStackTrace();
                ErrorManager.getDefault().notify(e);
            }
            return Collections.EMPTY_LIST;
        }
