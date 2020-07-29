    protected void discoverRegistryEntries() {
        DataSourceRegistry registry = this;
        try {
            ClassLoader loader = DataSetURI.class.getClassLoader();
            Enumeration<URL> urls;
            if (loader == null) {
                urls = ClassLoader.getSystemResources("META-INF/org.virbo.datasource.DataSourceFactory.extensions");
            } else {
                urls = loader.getResources("META-INF/org.virbo.datasource.DataSourceFactory.extensions");
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String s = reader.readLine();
                while (s != null) {
                    s = s.trim();
                    if (s.length() > 0) {
                        String[] ss = s.split("\\s");
                        for (int i = 1; i < ss.length; i++) {
                            if (ss[i].contains(".")) {
                                System.err.println("META-INF/org.virbo.datasource.DataSourceFactory.extensions contains extension that contains period: ");
                                System.err.println(ss[0] + " " + ss[i] + " in " + url);
                                System.err.println("This sometimes happens when extension files are concatenated, so check that all are terminated by end-of-line");
                                System.err.println("");
                                throw new IllegalArgumentException("DataSourceFactory.extensions contains extension that contains period: " + url);
                            }
                            registry.registerExtension(ss[0], ss[i], null);
                        }
                    }
                    s = reader.readLine();
                }
                reader.close();
            }
            if (loader == null) {
                urls = ClassLoader.getSystemResources("META-INF/org.virbo.datasource.DataSourceFactory.mimeTypes");
            } else {
                urls = loader.getResources("META-INF/org.virbo.datasource.DataSourceFactory.mimeTypes");
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String s = reader.readLine();
                while (s != null) {
                    s = s.trim();
                    if (s.length() > 0) {
                        String[] ss = s.split("\\s");
                        for (int i = 1; i < ss.length; i++) {
                            registry.registerMimeType(ss[0], ss[i]);
                        }
                    }
                    s = reader.readLine();
                }
                reader.close();
            }
            if (loader == null) {
                urls = ClassLoader.getSystemResources("META-INF/org.virbo.datasource.DataSourceFormat.extensions");
            } else {
                urls = loader.getResources("META-INF/org.virbo.datasource.DataSourceFormat.extensions");
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String s = reader.readLine();
                while (s != null) {
                    s = s.trim();
                    if (s.length() > 0) {
                        String[] ss = s.split("\\s");
                        for (int i = 1; i < ss.length; i++) {
                            if (ss[i].contains(".")) {
                                System.err.println("META-INF/org.virbo.datasource.DataSourceFormat.extensions contains extension that contains period: ");
                                System.err.println(ss[0] + " " + ss[i] + " in " + url);
                                System.err.println("This sometimes happens when extension files are concatenated, so check that all are terminated by end-of-line");
                                System.err.println("");
                                throw new IllegalArgumentException("DataSourceFactory.extensions contains extension that contains period: " + url);
                            }
                            registry.registerFormatter(ss[0], ss[i]);
                        }
                    }
                    s = reader.readLine();
                }
                reader.close();
            }
            if (loader == null) {
                urls = ClassLoader.getSystemResources("META-INF/org.virbo.datasource.DataSourceEditorPanel.extensions");
            } else {
                urls = loader.getResources("META-INF/org.virbo.datasource.DataSourceEditorPanel.extensions");
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String s = reader.readLine();
                while (s != null) {
                    s = s.trim();
                    if (s.length() > 0) {
                        String[] ss = s.split("\\s");
                        for (int i = 1; i < ss.length; i++) {
                            if (ss[i].contains(".")) {
                                System.err.println("META-INF/org.virbo.datasource.DataSourceEditorPanel.extensions contains extension that contains period: ");
                                System.err.println(ss[0] + " " + ss[i] + " in " + url);
                                System.err.println("This sometimes happens when extension files are concatenated, so check that all are terminated by end-of-line");
                                System.err.println("");
                                throw new IllegalArgumentException("DataSourceFactory.extensions contains extension that contains period: " + url);
                            }
                            registry.registerEditor(ss[0], ss[i]);
                        }
                    }
                    s = reader.readLine();
                }
                reader.close();
            }
            if (loader == null) {
                urls = ClassLoader.getSystemResources("META-INF/org.virbo.datasource.DataSourceFormatEditorPanel.extensions");
            } else {
                urls = loader.getResources("META-INF/org.virbo.datasource.DataSourceFormatEditorPanel.extensions");
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String s = reader.readLine();
                while (s != null) {
                    s = s.trim();
                    if (s.length() > 0) {
                        String[] ss = s.split("\\s");
                        for (int i = 1; i < ss.length; i++) {
                            if (ss[i].contains(".")) {
                                System.err.println("META-INF/org.virbo.datasource.DataSourceFormatEditorPanel.extensions contains extension that contains period: ");
                                System.err.println(ss[0] + " " + ss[i] + " in " + url);
                                System.err.println("This sometimes happens when extension files are concatenated, so check that all are terminated by end-of-line");
                                System.err.println("");
                                throw new IllegalArgumentException("DataSourceFactory.extensions contains extension that contains period: " + url);
                            }
                            registry.registerFormatEditor(ss[0], ss[i]);
                        }
                    }
                    s = reader.readLine();
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
