        public synchronized InputStream get(String relativePath, BundleResourceManager resManager) throws FileNotFoundException, URISyntaxException, IOException {
            byte[] ans = null;
            String key = resManager.getBundle().getBundleId() + ":" + relativePath;
            if ((ans = repository.get(key)) == null) {
                FileInputStream in = new FileInputStream(resManager.getNativeResource(relativePath));
                ByteArrayOutputStream dataBank = new ByteArrayOutputStream();
                int offset = 0;
                while ((offset = in.read(buff)) != -1) dataBank.write(buff, 0, offset);
                repository.put(key, ans = dataBank.toByteArray());
                dataBank.close();
            }
            return new ByteArrayInputStream(ans);
        }
