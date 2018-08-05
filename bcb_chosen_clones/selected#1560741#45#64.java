        @Override
        protected void copyContent(String filename) throws IOException {
            InputStream in = null;
            try {
                String resourceDir = System.getProperty("resourceDir");
                File resource = new File(resourceDir, filename);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                if (resource.exists()) {
                    in = new FileInputStream(resource);
                } else {
                    in = LOADER.getResourceAsStream(RES_PKG + filename);
                }
                IOUtils.copy(in, out);
                setResponseData(out.toByteArray());
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
