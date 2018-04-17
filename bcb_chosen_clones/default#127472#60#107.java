    public static void main(String[] argv) {
        if (1 < argv.length) {
            File[] sources = Source(argv[0]);
            if (null != sources) {
                for (File src : sources) {
                    File[] targets = Target(src, argv);
                    if (null != targets) {
                        final long srclen = src.length();
                        try {
                            FileChannel source = new FileInputStream(src).getChannel();
                            try {
                                for (File tgt : targets) {
                                    FileChannel target = new FileOutputStream(tgt).getChannel();
                                    try {
                                        source.transferTo(0L, srclen, target);
                                    } finally {
                                        target.close();
                                    }
                                    System.out.printf("Updated %s\n", tgt.getPath());
                                    File[] deletes = Delete(src, tgt);
                                    if (null != deletes) {
                                        for (File del : deletes) {
                                            if (SVN) {
                                                if (SvnDelete(del)) System.out.printf("Deleted %s\n", del.getPath()); else System.out.printf("Failed to delete %s\n", del.getPath());
                                            } else if (del.delete()) System.out.printf("Deleted %s\n", del.getPath()); else System.out.printf("Failed to delete %s\n", del.getPath());
                                        }
                                    }
                                    if (SVN) SvnAdd(tgt);
                                }
                            } finally {
                                source.close();
                            }
                        } catch (Exception exc) {
                            exc.printStackTrace();
                            System.exit(1);
                        }
                    }
                }
                System.exit(0);
            } else {
                System.err.printf("Source file(s) not found in '%s'\n", argv[0]);
                System.exit(1);
            }
        } else {
            usage();
            System.exit(1);
        }
    }
