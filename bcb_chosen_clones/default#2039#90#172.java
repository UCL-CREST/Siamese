    public void convert(File src, File dest) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(src));
        DcmParser p = pfact.newDcmParser(in);
        Dataset ds = fact.newDataset();
        p.setDcmHandler(ds.getDcmHandler());
        try {
            FileFormat format = p.detectFileFormat();
            if (format != FileFormat.ACRNEMA_STREAM) {
                System.out.println("\n" + src + ": not an ACRNEMA stream!");
                return;
            }
            p.parseDcmFile(format, Tags.PixelData);
            if (ds.contains(Tags.StudyInstanceUID) || ds.contains(Tags.SeriesInstanceUID) || ds.contains(Tags.SOPInstanceUID) || ds.contains(Tags.SOPClassUID)) {
                System.out.println("\n" + src + ": contains UIDs!" + " => probable already DICOM - do not convert");
                return;
            }
            boolean hasPixelData = p.getReadTag() == Tags.PixelData;
            boolean inflate = hasPixelData && ds.getInt(Tags.BitsAllocated, 0) == 12;
            int pxlen = p.getReadLength();
            if (hasPixelData) {
                if (inflate) {
                    ds.putUS(Tags.BitsAllocated, 16);
                    pxlen = pxlen * 4 / 3;
                }
                if (pxlen != (ds.getInt(Tags.BitsAllocated, 0) >>> 3) * ds.getInt(Tags.Rows, 0) * ds.getInt(Tags.Columns, 0) * ds.getInt(Tags.NumberOfFrames, 1) * ds.getInt(Tags.NumberOfSamples, 1)) {
                    System.out.println("\n" + src + ": mismatch pixel data length!" + " => do not convert");
                    return;
                }
            }
            ds.putUI(Tags.StudyInstanceUID, uid(studyUID));
            ds.putUI(Tags.SeriesInstanceUID, uid(seriesUID));
            ds.putUI(Tags.SOPInstanceUID, uid(instUID));
            ds.putUI(Tags.SOPClassUID, classUID);
            if (!ds.contains(Tags.NumberOfSamples)) {
                ds.putUS(Tags.NumberOfSamples, 1);
            }
            if (!ds.contains(Tags.PhotometricInterpretation)) {
                ds.putCS(Tags.PhotometricInterpretation, "MONOCHROME2");
            }
            if (fmi) {
                ds.setFileMetaInfo(fact.newFileMetaInfo(ds, UIDs.ImplicitVRLittleEndian));
            }
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
            try {
            } finally {
                ds.writeFile(out, encodeParam());
                if (hasPixelData) {
                    if (!skipGroupLen) {
                        out.write(PXDATA_GROUPLEN);
                        int grlen = pxlen + 8;
                        out.write((byte) grlen);
                        out.write((byte) (grlen >> 8));
                        out.write((byte) (grlen >> 16));
                        out.write((byte) (grlen >> 24));
                    }
                    out.write(PXDATA_TAG);
                    out.write((byte) pxlen);
                    out.write((byte) (pxlen >> 8));
                    out.write((byte) (pxlen >> 16));
                    out.write((byte) (pxlen >> 24));
                }
                if (inflate) {
                    int b2, b3;
                    for (; pxlen > 0; pxlen -= 3) {
                        out.write(in.read());
                        b2 = in.read();
                        b3 = in.read();
                        out.write(b2 & 0x0f);
                        out.write(b2 >> 4 | ((b3 & 0x0f) << 4));
                        out.write(b3 >> 4);
                    }
                } else {
                    for (; pxlen > 0; --pxlen) {
                        out.write(in.read());
                    }
                }
                out.close();
            }
            System.out.print('.');
        } finally {
            in.close();
        }
    }
