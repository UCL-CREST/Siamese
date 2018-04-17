    public static void main(String... args) throws Exception {
        File testSrc = new File(System.getProperty("test.src"));
        String source = new File(testSrc, "T6410653.java").getPath();
        ClassLoader cl = ToolProvider.getSystemToolClassLoader();
        Tool compiler = ToolProvider.getSystemJavaCompiler();
        Class<?> main = Class.forName("pl.wcislo.sbql4j.tools.javac.main.Main", true, cl);
        Method useRawMessages = main.getMethod("useRawMessages", boolean.class);
        useRawMessages.invoke(null, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        compiler.run(null, null, out, "-d", source, source);
        useRawMessages.invoke(null, false);
        if (!out.toString().equals(String.format("%s%n%s%n", "javac: javac.err.file.not.directory", "javac.msg.usage"))) {
            throw new AssertionError(out);
        }
        System.out.println("Test PASSED.  Running javac again to see localized output:");
        compiler.run(null, null, System.out, "-d", source, source);
    }
