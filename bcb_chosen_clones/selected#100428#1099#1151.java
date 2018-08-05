    protected void downloadControlFile(String path, boolean validateSignature) throws IOException {
        File target = downloadFile(path);
        if (validateSignature) {
            if (_signers.isEmpty()) {
                log.info("No signers, not verifying file", "path", path);
            } else {
                File signatureFile = downloadFile(path + SIGNATURE_SUFFIX);
                byte[] signature = null;
                FileReader reader = null;
                try {
                    reader = new FileReader(signatureFile);
                    signature = StreamUtil.toByteArray(new FileInputStream(signatureFile));
                } finally {
                    StreamUtil.close(reader);
                    signatureFile.delete();
                }
                byte[] buffer = new byte[8192];
                int length, validated = 0;
                for (Certificate cert : _signers) {
                    FileInputStream dataInput = null;
                    try {
                        dataInput = new FileInputStream(target);
                        Signature sig = Signature.getInstance("SHA1withRSA");
                        sig.initVerify(cert);
                        while ((length = dataInput.read(buffer)) != -1) {
                            sig.update(buffer, 0, length);
                        }
                        if (!sig.verify(Base64.decodeBase64(signature))) {
                            log.info("Signature does not match", "cert", cert.getPublicKey());
                            continue;
                        } else {
                            log.info("Signature matches", "cert", cert.getPublicKey());
                            validated++;
                        }
                    } catch (IOException ioe) {
                        log.warning("Failure validating signature of " + target + ": " + ioe);
                    } catch (GeneralSecurityException gse) {
                    } finally {
                        StreamUtil.close(dataInput);
                        dataInput = null;
                    }
                }
                if (validated == 0) {
                    target.delete();
                    throw new IOException("m.corrupt_digest_signature_error");
                }
            }
        }
        File original = getLocalPath(path);
        if (!FileUtil.renameTo(target, original)) {
            throw new IOException("Failed to rename(" + target + ", " + original + ")");
        }
    }
