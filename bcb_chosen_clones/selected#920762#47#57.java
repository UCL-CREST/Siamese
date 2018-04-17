    @Primitive
    public static Value caml_md5_string(final CodeRunner ctxt, final Value str, final Value ofs, final Value len) throws Fail.Exception {
        try {
            final MessageDigest md5 = MessageDigest.getInstance(Md5.ALGO);
            md5.update(str.asBlock().getBytes(), ofs.asLong(), len.asLong());
            return Value.createFromBlock(Block.createString(md5.digest()));
        } catch (final NoSuchAlgorithmException nsae) {
            Fail.invalidArgument("Digest.substring");
            return Value.UNIT;
        }
    }
