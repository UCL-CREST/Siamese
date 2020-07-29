    public static void rewrite(final String filePath, final boolean scrub, final PrintWriter pwOut) throws IOException {
        final HashMap<String, AnnotationInfo> annInfo = BytecodeRewriter.collectAnnotationInfo(filePath);
        final FileInputStream fis = new FileInputStream(filePath);
        final ClassReader cr = new ClassReader(fis);
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        final ClassAdapter ca = new ClassAdapter(cw) {

            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                final MethodNode mn = new MethodNode(access, name, desc, signature, exceptions) {

                    @Override
                    public void visitEnd() {
                        BytecodeRewriter.rewrite(this, scrub, annInfo);
                        accept(mv);
                    }
                };
                return mn;
            }
        };
        cr.accept(ca, 0);
        final byte[] newCode = cw.toByteArray();
        fis.close();
        if (pwOut != null) {
            final TraceClassVisitor tcv = new TraceClassVisitor(new PrintWriter(pwOut));
            new ClassReader(newCode).accept(tcv, 0);
        }
        final FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(newCode);
        fos.close();
    }
