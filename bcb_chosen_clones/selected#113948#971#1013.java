    private byte[] resourceContent(String resourceName) throws IOException {
        if (path == null) {
            throw new IOException("No resources available");
        }
        InputStream input = null;
        for (File f : path) {
            if (f.isDirectory()) {
                File file = new File(f, resourceName.replace('/', File.separatorChar));
                boolean exists = AccessController.doPrivileged(new FileExistsAction(file));
                if (exists) {
                    try {
                        input = AccessController.doPrivileged(new FileInputStreamAction(file));
                    } catch (PrivilegedActionException e) {
                        throw (IOException) e.getException();
                    }
                    break;
                }
            } else {
                try {
                    JarFile jar = AccessController.doPrivileged(new JarFileAction(f));
                    ZipEntry entry = jar.getEntry(resourceName);
                    if (entry != null) {
                        input = jar.getInputStream(entry);
                        break;
                    }
                } catch (PrivilegedActionException e) {
                    throw (IOException) e.getException();
                }
            }
        }
        if (input == null) {
            throw new FileNotFoundException("No such file: " + resourceName);
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int size;
        byte[] buffer = new byte[SIZE_BUFFER];
        while ((size = input.read(buffer)) != -1) {
            output.write(buffer, 0, size);
        }
        input.close();
        output.close();
        return output.toByteArray();
    }
