    public boolean excuteBackup(String backupOrginlDrctry, String targetFileNm, String archiveFormat) throws JobExecutionException {
        File targetFile = new File(targetFileNm);
        File srcFile = new File(backupOrginlDrctry);
        if (!srcFile.exists()) {
            log.error("백업원본디렉토리[" + srcFile.getAbsolutePath() + "]가 존재하지 않습니다.");
            throw new JobExecutionException("백업원본디렉토리[" + srcFile.getAbsolutePath() + "]가 존재하지 않습니다.");
        }
        if (srcFile.isFile()) {
            log.error("백업원본디렉토리[" + srcFile.getAbsolutePath() + "]가 파일입니다. 디렉토리명을 지정해야 합니다. ");
            throw new JobExecutionException("백업원본디렉토리[" + srcFile.getAbsolutePath() + "]가 파일입니다. 디렉토리명을 지정해야 합니다. ");
        }
        boolean result = false;
        FileInputStream finput = null;
        FileOutputStream fosOutput = null;
        ArchiveOutputStream aosOutput = null;
        ArchiveEntry entry = null;
        try {
            log.debug("charter set : " + Charset.defaultCharset().name());
            fosOutput = new FileOutputStream(targetFile);
            aosOutput = new ArchiveStreamFactory().createArchiveOutputStream(archiveFormat, fosOutput);
            if (ArchiveStreamFactory.TAR.equals(archiveFormat)) {
                ((TarArchiveOutputStream) aosOutput).setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            }
            File[] fileArr = srcFile.listFiles();
            ArrayList list = EgovFileTool.getSubFilesByAll(fileArr);
            for (int i = 0; i < list.size(); i++) {
                File sfile = new File((String) list.get(i));
                finput = new FileInputStream(sfile);
                if (ArchiveStreamFactory.TAR.equals(archiveFormat)) {
                    entry = new TarArchiveEntry(sfile, new String(sfile.getAbsolutePath().getBytes(Charset.defaultCharset().name()), "8859_1"));
                    ((TarArchiveEntry) entry).setSize(sfile.length());
                } else {
                    entry = new ZipArchiveEntry(sfile.getAbsolutePath());
                    ((ZipArchiveEntry) entry).setSize(sfile.length());
                }
                aosOutput.putArchiveEntry(entry);
                IOUtils.copy(finput, aosOutput);
                aosOutput.closeArchiveEntry();
                finput.close();
                result = true;
            }
            aosOutput.close();
        } catch (Exception e) {
            log.error("백업화일생성중 에러가 발생했습니다. 에러 : " + e.getMessage());
            log.debug(e);
            result = false;
            throw new JobExecutionException("백업화일생성중 에러가 발생했습니다.", e);
        } finally {
            try {
                if (finput != null) finput.close();
            } catch (Exception e2) {
                log.error("IGNORE:", e2);
            }
            try {
                if (aosOutput != null) aosOutput.close();
            } catch (Exception e2) {
                log.error("IGNORE:", e2);
            }
            try {
                if (fosOutput != null) fosOutput.close();
            } catch (Exception e2) {
                log.error("IGNORE:", e2);
            }
            try {
                if (result == false) targetFile.delete();
            } catch (Exception e2) {
                log.error("IGNORE:", e2);
            }
        }
        return result;
    }
