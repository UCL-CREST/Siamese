        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            final Checksum crc = new CRC32();
            byte[] b;
            crc.update(b = className.getBytes(), 0, b.length);
            crc.update(b = name.getBytes(), 0, b.length);
            crc.update(b = desc.getBytes(), 0, b.length);
            final Method method = new Method(className, name, Integer.toString((int) crc.getValue(), Character.MAX_RADIX));
            methods.add(method);
            return method;
        }
