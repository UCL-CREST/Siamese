    public static Collection<Class<? extends Page>> loadPages() throws IOException {
        ClassLoader ldr = Thread.currentThread().getContextClassLoader();
        Collection<Class<? extends Page>> pages = new ArrayList<Class<? extends Page>>();
        Enumeration<URL> e = ldr.getResources("META-INF/services/" + Page.class.getName());
        while (e.hasMoreElements()) {
            URL url = e.nextElement();
            InputStream is = url.openStream();
            ;
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while (true) {
                    String line = r.readLine();
                    if (line == null) break;
                    int comment = line.indexOf('#');
                    if (comment >= 0) line = line.substring(0, comment);
                    String name = line.trim();
                    if (name.length() == 0) continue;
                    Class<?> clz = Class.forName(name, true, ldr);
                    Class<? extends Page> impl = clz.asSubclass(Page.class);
                    pages.add(impl);
                }
            } catch (Exception ex) {
                System.out.println(ex);
            } finally {
                try {
                    is.close();
                } catch (Exception ex) {
                }
            }
        }
        return pages;
    }
