    private byte[] getTypeFromMutator(String typeName) {
        FileInputStream fis;
        String fileName = path + File.separatorChar + typeName.replace('.', File.separatorChar) + ".class";
        System.out.println("trying to load: " + fileName);
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int c = bis.read();
            while (c != -1) {
                out.write(c);
                c = bis.read();
            }
        } catch (IOException e) {
            return null;
        }
        return out.toByteArray();
    }
