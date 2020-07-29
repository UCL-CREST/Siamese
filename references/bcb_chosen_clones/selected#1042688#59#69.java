        private void internalCopy(File fSource, File file) throws FileNotFoundException, IOException {
            if (fSource.getName().equals("Thums.db")) return;
            System.out.println("copying " + fSource + " in " + file);
            OutputStream o = new BufferedOutputStream(new FileOutputStream(file));
            InputStream i = new BufferedInputStream(new FileInputStream(fSource));
            byte[] b = new byte[8192];
            int n;
            while ((n = i.read(b)) > 0) o.write(b, 0, n);
            i.close();
            o.close();
        }
