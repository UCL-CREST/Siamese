        public void finishDigest(String outputDirectory) throws IOException {
            localizedPrintWriter.println("</digest>");
            if (localizedPrintWriter != null) {
                localizedPrintWriter.close();
            }
            File digest = new File(outputDirectory + File.separator + "digest" + (locale == null || locale.equals("") ? "" : "_" + locale) + ".zip");
            System.out.println(digest.getAbsolutePath());
            System.out.println(digest.getName());
            if (digest.exists()) {
                digest.delete();
            }
            digest.createNewFile();
            OutputStream os = new FileOutputStream(digest);
            JarOutputStream jos = new JarOutputStream(os);
            jos.putNextEntry(new ZipEntry("digest.xml"));
            InputStream is = new FileInputStream(tempDigestDirectory);
            byte[] b = new byte[4096];
            int bytesRead = 0;
            do {
                bytesRead = is.read(b);
                if (bytesRead > 0) {
                    jos.write(b, 0, bytesRead);
                }
            } while (bytesRead > 0);
            jos.closeEntry();
            jos.close();
            os.close();
            is.close();
            tempDigestDirectory.delete();
        }
