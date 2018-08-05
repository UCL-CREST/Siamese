    private void doDecrypt(boolean createOutput) throws IOException {
        FileInputStream input = null;
        FileOutputStream output = null;
        File tempOutput = null;
        try {
            input = new FileInputStream(infile);
            String cipherBaseFilename = basename(infile);
            byte[] magic = new byte[MAGIC.length];
            input.read(magic);
            for (int i = 0; i < MAGIC.length; i++) {
                if (MAGIC[i] != magic[i]) throw new IOException("Not a BORK file (bad magic number)");
            }
            short version = readShort(input);
            if (version / 1000 > VERSION / 1000) throw new IOException("File created by an incompatible future version: " + version + " > " + VERSION);
            String cipherName = readString(input);
            Cipher cipher = createCipher(cipherName, createSessionKey(password, cipherBaseFilename));
            CipherInputStream decryptedInput = new CipherInputStream(input, cipher);
            long headerCrc = Unsigned.promote(readInt(decryptedInput));
            decryptedInput.resetCRC();
            outfile = new File(outputDir, readString(decryptedInput));
            if (!createOutput || outfile.exists()) {
                skipped = true;
                return;
            }
            tempOutput = File.createTempFile("bork", null, outputDir);
            tempOutput.deleteOnExit();
            byte[] buffer = new byte[BUFFER_SIZE];
            output = new FileOutputStream(tempOutput);
            int bytesRead;
            while ((bytesRead = decryptedInput.read(buffer)) != -1) output.write(buffer, 0, bytesRead);
            output.close();
            output = null;
            if (headerCrc != decryptedInput.getCRC()) {
                outfile = null;
                throw new IOException("CRC mismatch: password is probably incorrect");
            }
            if (!tempOutput.renameTo(outfile)) throw new IOException("Failed to rename temp output file " + tempOutput + " to " + outfile);
            outfile.setLastModified(infile.lastModified());
        } finally {
            close(input);
            close(output);
            if (tempOutput != null) tempOutput.delete();
        }
    }
