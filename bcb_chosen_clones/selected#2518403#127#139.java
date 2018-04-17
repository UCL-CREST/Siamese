        private void writeToJar() throws IOException, FileNotFoundException {
            JarOutputStream jar = new JarOutputStream(new FileOutputStream(output));
            for (MemoryJavaFileObject file : toOutput) {
                String path = file.toUri().getPath();
                if (path.equals("/fake/Fake.class")) {
                    continue;
                }
                ZipEntry entry = new ZipEntry(path.substring(1));
                jar.putNextEntry(entry);
                jar.write(file.bytes.toByteArray());
            }
            jar.close();
        }
