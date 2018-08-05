    private void extractSourceFiles(String jar) {
        JarInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new JarInputStream(new FileInputStream(getProjectFile(jar)));
            JarEntry item;
            byte buffer[] = new byte[4096];
            int buflength;
            while ((item = in.getNextJarEntry()) != null) if (item.getName().startsWith(PREFIX) && (!item.getName().endsWith("/"))) {
                out = new BufferedOutputStream(new FileOutputStream(new File(dest, getFileName(item))));
                while ((buflength = in.read(buffer)) != -1) out.write(buffer, 0, buflength);
                howmany++;
                out.flush();
                out.close();
                out = null;
            }
        } catch (IOException ex) {
            System.out.println("Unable to parse file " + jar + ", reason: " + ex.getMessage());
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ex) {
            }
            try {
                if (out != null) out.close();
            } catch (IOException ex) {
            }
        }
    }
