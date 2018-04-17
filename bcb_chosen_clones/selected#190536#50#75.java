    public void copy(String sourcePath, String targetPath) throws IOException {
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) fileOutputStream.write(buffer, 0, bytesRead);
        } finally {
            if (fileInputStream != null) try {
                fileInputStream.close();
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s265") + sourcePath, AcideLanguageManager.getInstance().getLabels().getString("s266"), JOptionPane.ERROR_MESSAGE);
                AcideLog.getLog().error(exception.getMessage());
            }
            if (fileOutputStream != null) try {
                fileOutputStream.close();
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s267") + targetPath, AcideLanguageManager.getInstance().getLabels().getString("268"), JOptionPane.ERROR_MESSAGE);
                AcideLog.getLog().error(exception.getMessage());
            }
        }
    }
