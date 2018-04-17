        public void applyTo(File source, File target) throws IOException {
            boolean failed = true;
            FileInputStream fin = new FileInputStream(source);
            try {
                FileChannel in = fin.getChannel();
                FileOutputStream fos = new FileOutputStream(target);
                try {
                    FileChannel out = fos.getChannel();
                    long pos = 0L;
                    for (Replacement replacement : replacements) {
                        in.transferTo(pos, replacement.pos - pos, out);
                        if (replacement.val != null) out.write(ByteBuffer.wrap(replacement.val));
                        pos = replacement.pos + replacement.len;
                    }
                    in.transferTo(pos, source.length() - pos, out);
                    failed = false;
                } finally {
                    fos.close();
                    if (failed == true) target.delete();
                }
            } finally {
                fin.close();
            }
        }
