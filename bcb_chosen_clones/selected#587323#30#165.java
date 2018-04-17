    public void syncFiles(String src, String dest, List<String> excludes) {
        dest = Path.getCorrectPath(dest);
        File srcFile = new File(src);
        if (srcFile.exists() == false) return;
        if (ConfigHelper.useSpaceCheck() == true) {
            if (hasFreeSpace(src, dest) == false) return;
        }
        if (srcFile.isFile()) {
            String destPath = Path.combine(dest, srcFile.getName());
            File destFile = new File(destPath);
            if (ConfigHelper.useHashing()) {
                String name = srcFile.getName();
                if (HashHelper.isHashFile(name) == true) return;
                String fileHash = HashHelper.getCheckSum(src);
                String currentHash = null;
                if (ConfigHelper.useHashFile() == true) currentHash = HashHelper.getHashFromFile(dest, name); else currentHash = HashHelper.getCheckSum(src);
                if (fileHash.equals(currentHash) == false) {
                    if (currentHash != null && ConfigHelper.isDebugMode() == true) LogHelper.writeInfoFormat("hashChanged", fileHash, src, currentHash, dest);
                    HashHelper.writeHashToFile(dest, name, fileHash);
                    FileManager.copyFile(srcFile, destFile);
                }
            } else if (fileChanged(srcFile, destFile) == true) {
                FileManager.copyFile(srcFile, destFile);
            }
            return;
        }
        if (srcFile.isDirectory()) {
            src = Path.getCorrectPath(src);
            File destFile = new File(dest);
            if (destFile.exists() == false) {
                if (FileHelper.createDestinationFolder(dest) == false) {
                    LogHelper.writeErrorFormat("directoryNotCreated", dest);
                    return;
                }
            }
            StringBuilder srcBuilder = new StringBuilder(src);
            StringBuilder destBuilder = new StringBuilder(dest);
            listRelativeFolderPaths(dest);
            while (Folders.size() > 0) {
                srcBuilder = resetStringBuilder(srcBuilder, src);
                destBuilder = resetStringBuilder(destBuilder, dest);
                String folder = Folders.remove(0);
                String srcPath = srcBuilder.append(folder).toString();
                String destPath = destBuilder.append(folder).toString();
                srcFile = new File(srcPath);
                destFile = new File(destPath);
                if (srcFile.exists() == false) {
                    deleteDirectory(destFile);
                    continue;
                }
                List<String> files = listFiles(destPath, dest);
                if (files == null) continue;
                while (files.size() > 0) {
                    srcBuilder = resetStringBuilder(srcBuilder, src);
                    destBuilder = resetStringBuilder(destBuilder, dest);
                    String relFilePath = files.remove(0);
                    srcPath = srcBuilder.append(relFilePath).toString();
                    destPath = destBuilder.append(relFilePath).toString();
                    srcFile = new File(srcPath);
                    destFile = new File(destPath);
                    if (srcFile.exists() == false || srcFile.isFile() == false) {
                        deleteFile(destFile);
                        continue;
                    } else {
                        if (ConfigHelper.useHashing()) {
                            if (HashHelper.isHashFile(destFile.getName()) == false) {
                                deleteFile(destFile);
                                continue;
                            }
                        } else {
                            if (filesAreEqual(srcFile, destFile) == false) {
                                deleteFile(destFile);
                                continue;
                            }
                        }
                    }
                }
            }
            if (ConfigHelper.useSpaceCheck() == true) {
                if (hasFreeSpace(src, dest) == false) return;
            }
            listRelativeFolderPaths(src);
            while (Folders.size() > 0) {
                CommonHelper.sleep(1);
                srcBuilder = resetStringBuilder(srcBuilder, src);
                destBuilder = resetStringBuilder(destBuilder, dest);
                String folder = Folders.remove(0);
                String srcPath = srcBuilder.append(folder).toString();
                String destPath = destBuilder.append(folder).toString();
                srcFile = new File(srcPath);
                destFile = new File(destPath);
                if (isExcludePath(srcFile, excludes)) continue;
                if (ConfigHelper.isDebugMode()) LogHelper.writeInfoFormat("checkDirectory", srcPath);
                if (destFile.exists() == false) {
                    if (FileHelper.createDestinationFolder(destFile) == false) LogHelper.writeErrorFormat("directoryNotCreated", destPath);
                }
                if (srcFile.canRead() == false) {
                    LogHelper.writeErrorFormat("directoryNotReadable", srcPath);
                    continue;
                }
                List<String> files = listFiles(srcPath, src);
                if (files == null) continue;
                while (files.size() > 0) {
                    CommonHelper.sleep(1);
                    srcBuilder = resetStringBuilder(srcBuilder, src);
                    destBuilder = resetStringBuilder(destBuilder, dest);
                    String relFilePath = files.remove(0);
                    srcPath = srcBuilder.append(relFilePath).toString();
                    destPath = destBuilder.append(relFilePath).toString();
                    srcFile = new File(srcPath);
                    destFile = new File(destPath);
                    if (ConfigHelper.isDebugMode()) LogHelper.writeInfoFormat("checkFile", srcPath);
                    if (srcFile.canRead() == false) {
                        LogHelper.writeErrorFormat("fileNotReadable", srcPath);
                        continue;
                    }
                    if (ConfigHelper.useHashing() == true) {
                        if (HashHelper.isHashFile(srcFile.getName()) == true) continue;
                        String currentHash = null;
                        String fileHash = HashHelper.getCheckSum(srcPath);
                        String destCheckParentPath = destFile.getParent();
                        if (ConfigHelper.useHashFile() == true) currentHash = HashHelper.getHashFromFile(destCheckParentPath, srcFile.getName()); else currentHash = HashHelper.getCheckSum(destPath);
                        if (fileHash.equals(currentHash) == false) {
                            if (currentHash != null && ConfigHelper.isDebugMode() == true) LogHelper.writeInfoFormat("hashChanged", fileHash, destPath, currentHash, destCheckParentPath);
                            HashHelper.writeHashToFile(dest, srcFile.getName(), fileHash);
                            FileManager.copyFile(srcFile, destFile);
                            continue;
                        }
                    } else if (fileChanged(srcFile, destFile) == true) {
                        FileManager.copyFile(srcFile, destFile);
                        continue;
                    }
                }
            }
        }
    }
