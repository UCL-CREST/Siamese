    @SuppressWarnings("unchecked")
    public static <T extends Class> Collection<T> listServices(T serviceType, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        final Collection<T> result = new LinkedHashSet<T>();
        final Enumeration<URL> resouces = classLoader.getResources("META-INF/services/" + serviceType.getName());
        while (resouces.hasMoreElements()) {
            final URL url = resouces.nextElement();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            try {
                String line = reader.readLine();
                while (line != null) {
                    if (line.startsWith("#")) {
                    } else if ("".equals(line.trim())) {
                    } else {
                        final T implClass = (T) Class.forName(line, true, classLoader);
                        if (!serviceType.isAssignableFrom(implClass)) {
                            throw new IllegalStateException(String.format("%s: class %s does not implement required interfafce %s", url, implClass, serviceType));
                        }
                        result.add(implClass);
                    }
                    line = reader.readLine();
                }
            } finally {
                reader.close();
            }
        }
        return result;
    }
