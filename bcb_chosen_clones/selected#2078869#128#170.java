        Archive(Daemon daemon, File file) throws Exception {
            service = new HashSet();
            chain = new HashMap();
            name = file.getName();
            date = file.lastModified();
            JarInputStream in = new JarInput(new FileInputStream(file));
            if (daemon.host) {
                host = name.substring(0, name.lastIndexOf('.'));
                String path = "app" + File.separator + host + File.separator;
                PermissionCollection permissions = new Permissions();
                permissions.add(new SocketPermission("localhost", "resolve,connect"));
                permissions.add(new FilePermission(path + "-", "read"));
                permissions.add(new FilePermission(path + "-", "write"));
                permissions.add(new FilePermission(path + "-", "delete"));
                permissions.add(new FilePermission(System.getProperty("user.dir") + File.separator + "res" + File.separator + "-", "read"));
                permissions.add(new PropertyPermission("user.dir", "read"));
                access = new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, permissions) });
                new File(path).mkdirs();
            } else {
                host = "content";
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JarEntry entry = null;
            while ((entry = in.getNextJarEntry()) != null) {
                if (entry.getName().endsWith(".class")) {
                    pipe(in, out);
                    byte[] data = out.toByteArray();
                    out.reset();
                    String name = name(entry.getName());
                    classes.add(new Small(name, data));
                } else if (!entry.isDirectory()) {
                    Big.write(host, "/" + entry.getName(), in);
                }
            }
            int length = classes.size();
            String missing = "";
            Small small = null;
            while (classes.size() > 0) {
                small = (Small) classes.elementAt(0);
                classes.removeElement(small);
                instantiate(small, daemon);
            }
        }
