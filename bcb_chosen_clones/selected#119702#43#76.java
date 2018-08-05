    private static <T> Collection<T> loadFromServices(Class<T> interf) throws Exception {
        ClassLoader classLoader = DSServiceLoader.class.getClassLoader();
        Enumeration<URL> e = classLoader.getResources("META-INF/services/" + interf.getName());
        Collection<T> services = new ArrayList<T>();
        while (e.hasMoreElements()) {
            URL url = e.nextElement();
            InputStream is = url.openStream();
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while (true) {
                    String line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    int comment = line.indexOf('#');
                    if (comment >= 0) {
                        line = line.substring(0, comment);
                    }
                    String name = line.trim();
                    if (name.length() == 0) {
                        continue;
                    }
                    Class<?> clz = Class.forName(name, true, classLoader);
                    Class<? extends T> impl = clz.asSubclass(interf);
                    Constructor<? extends T> ctor = impl.getConstructor();
                    T svc = ctor.newInstance();
                    services.add(svc);
                }
            } finally {
                is.close();
            }
        }
        return services;
    }
