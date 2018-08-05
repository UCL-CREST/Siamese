            @Override
            public boolean accept(File file) {
                if (file.getName().equals(".svn")) {
                    return false;
                }
                final long modify = file.lastModified();
                final long time = DateUtils.toDate("2012-03-21 17:43", "yyyy-MM-dd HH:mm").getTime();
                if (modify >= time) {
                    if (file.isFile()) {
                        File f = new File(StringsUtils.replace(file.getAbsolutePath(), filePath2, filePath1));
                        f.getParentFile().mkdirs();
                        try {
                            IOUtils.copyFile(file, f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(f.getName());
                    }
                }
                return true;
            }
