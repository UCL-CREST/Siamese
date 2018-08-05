    protected void load() throws IOException {
        for (ClassLoader classLoader : classLoaders) {
            Enumeration<URL> en = classLoader.getResources("META-INF/services/" + serviceClass.getName());
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                InputStream in = url.openStream();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            if (!line.startsWith("#")) {
                                line = line.trim();
                                if (line.length() > 0) contributions.add(resolveClass(url, line));
                            }
                        }
                    } finally {
                        reader.close();
                    }
                } finally {
                    in.close();
                }
            }
        }
    }
