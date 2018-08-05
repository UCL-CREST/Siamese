    public static void main(String[] args) throws IOException {
        for (File jarFile : new File("d:\\Work\\Stuff\\serendepity-tutorial\\Serendipity 3.0\\target\\serendipity-3.0\\WEB-INF\\lib\\").listFiles()) {
            if (jarFile.isDirectory()) {
                continue;
            }
            ZipInputStream file = new ZipInputStream(new FileInputStream(jarFile));
            ZipEntry ze;
            String outFile = jarFile.getParent() + "/1/" + jarFile.getName();
            ZipOutputStream output = new ZipOutputStream(new FileOutputStream(outFile));
            System.out.println("Input:" + jarFile.getAbsolutePath());
            System.out.println("Otput:" + outFile);
            while ((ze = file.getNextEntry()) != null) {
                output.putNextEntry(new ZipEntry(ze.getName()));
                if (ze.getName().endsWith(".class")) {
                    ClassReader reader = new ClassReader(file);
                    ClassWriter cw = new ClassWriter(0);
                    reader.accept(new ClassVisitorImplementation(cw), ClassReader.SKIP_FRAMES);
                    output.write(cw.toByteArray());
                } else {
                    byte[] data = new byte[16 * 1024];
                    while (true) {
                        int r = file.read(data);
                        if (r == -1) {
                            break;
                        }
                        output.write(data, 0, r);
                    }
                }
            }
            output.flush();
            output.close();
        }
    }
