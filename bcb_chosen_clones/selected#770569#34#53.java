        @Override
        public Class<?> loadClass(final String name) throws ClassNotFoundException {
            final String baseName = StringUtils.substringBefore(name, "$");
            if (baseName.startsWith("java") && !whitelist.contains(baseName) && !additionalWhitelist.contains(baseName)) {
                throw new NoClassDefFoundError(name + " is a restricted class for GAE");
            }
            if (!name.startsWith("com.gargoylesoftware")) {
                return super.loadClass(name);
            }
            super.loadClass(name);
            final InputStream is = getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                IOUtils.copy(is, bos);
                final byte[] bytes = bos.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
