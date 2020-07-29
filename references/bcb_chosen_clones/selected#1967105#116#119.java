            public void finish(Bundle bundle, ZipOutputStream zout) throws IOException {
                zout.putNextEntry(new ZipEntry(name));
                IOUtils.copy(new StringReader(content), zout, "UTF-8");
            }
