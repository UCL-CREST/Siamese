    public static void copyDirectory(File srcDir, File dstDir, Integer totalArq, JProgressBar jProgressBar) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            String[] children = srcDir.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]), totalArq, jProgressBar);
            }
        } else {
            copyFile(srcDir, dstDir);
            qtdArqLido++;
            jProgressBar.setValue(qtdArqLido * 100 / totalArq);
            jProgressBar.setString("Atualizando Sistema " + (qtdArqLido * 100 / totalArq) + "%");
        }
    }
