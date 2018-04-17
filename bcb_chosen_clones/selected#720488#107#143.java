    private void flushPairs() throws IOException {
        for (int i = 0; i < pairs.size(); i++) {
            pair p = pairs.elementAt(i);
            String s = p.name;
            if (p.obj instanceof InputStream) {
                InputStream is = (InputStream) p.obj;
                if (zosOut != null) {
                    ZipEntry z = new ZipEntry(s);
                    zosOut.putNextEntry(z);
                    Utils.writeTo(is, zosOut);
                } else if (dirOut != null) {
                    File f = new File(dirOut, s);
                    FileOutputStream fos = new FileOutputStream(f);
                    Utils.writeTo(is, fos);
                    fos.close();
                }
            } else if (p.obj instanceof ByteArrayOutputStream) {
                ByteArrayOutputStream bos = (ByteArrayOutputStream) p.obj;
                String cnt = new String(bos.toByteArray());
                for (Enumeration<String> ks = anchorDest.keys(); ks.hasMoreElements(); ) {
                    String k = ks.nextElement();
                    String v = anchorDest.get(k);
                    cnt = cnt.replace("href=\"#" + k, "href=\"" + v);
                }
                if (zosOut != null) {
                    ZipEntry z = new ZipEntry(s);
                    zosOut.putNextEntry(z);
                    zosOut.write(cnt.getBytes());
                } else if (dirOut != null) {
                    File f = new File(dirOut, s);
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(cnt.getBytes());
                    fos.close();
                }
            }
        }
    }
