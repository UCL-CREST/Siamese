    private void execute(String file, String[] ebms, String[] eems, String[] ims, String[] efms) throws BuildException {
        if (verbose) {
            System.out.println("Preprocessing file " + file + " (type: " + type + ")");
        }
        try {
            File targetFile = new File(file);
            FileReader fr = new FileReader(targetFile);
            BufferedReader reader = new BufferedReader(fr);
            File preprocFile = File.createTempFile(targetFile.getName(), "tmp", targetFile.getParentFile());
            FileWriter fw = new FileWriter(preprocFile);
            BufferedWriter writer = new BufferedWriter(fw);
            int result = preprocess(reader, writer, ebms, eems, ims, efms);
            reader.close();
            writer.close();
            switch(result) {
                case OVERWRITE:
                    if (!targetFile.delete()) {
                        System.out.println("Can't overwrite target file with preprocessed file");
                        throw new BuildException("Can't overwrite target file " + target + " with preprocessed file");
                    }
                    preprocFile.renameTo(targetFile);
                    if (verbose) {
                        System.out.println("File " + preprocFile.getName() + " modified.");
                    }
                    modifiedCnt++;
                    break;
                case REMOVE:
                    if (!targetFile.delete()) {
                        System.out.println("Can't delete target file");
                        throw new BuildException("Can't delete target file " + target);
                    }
                    if (!preprocFile.delete()) {
                        System.out.println("Can't delete temporary preprocessed file " + preprocFile.getName());
                        throw new BuildException("Can't delete temporary preprocessed file " + preprocFile.getName());
                    }
                    if (verbose) {
                        System.out.println("File " + preprocFile.getName() + " removed.");
                    }
                    removedCnt++;
                    break;
                case KEEP:
                    if (!preprocFile.delete()) {
                        System.out.println("Can't delete temporary preprocessed file " + preprocFile.getName());
                        throw new BuildException("Can't delete temporary preprocessed file " + preprocFile.getName());
                    }
                    break;
                default:
                    throw new BuildException("Unexpected preprocessing result for file " + preprocFile.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e.getMessage());
        }
    }
