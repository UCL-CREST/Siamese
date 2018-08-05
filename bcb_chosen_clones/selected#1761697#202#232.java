                    public void handleEntry(ZipEntry entry, byte[] byteCode) throws Exception {
                        logger.verbose("starting entry : " + entry.toString());
                        if (!entry.isDirectory()) {
                            DataInputStream din = new DataInputStream(new ByteArrayInputStream(byteCode));
                            if (din.readInt() == CLASS_MAGIC) {
                                ClassDescriptor descriptor = getClassDescriptor(byteCode);
                                ClassTransformer transformer = getClassTransformer(descriptor);
                                if (transformer == null) {
                                    logger.verbose("skipping entry : " + entry.toString());
                                } else {
                                    logger.info("processing class [" + descriptor.getName() + "]; entry = " + file.toURL());
                                    byteCode = transformer.transform(getClass().getClassLoader(), descriptor.getName(), null, null, descriptor.getBytes());
                                }
                            } else {
                                logger.verbose("ignoring zip entry : " + entry.toString());
                            }
                        }
                        ZipEntry outEntry = new ZipEntry(entry.getName());
                        outEntry.setMethod(entry.getMethod());
                        outEntry.setComment(entry.getComment());
                        outEntry.setSize(byteCode.length);
                        if (outEntry.getMethod() == ZipEntry.STORED) {
                            CRC32 crc = new CRC32();
                            crc.update(byteCode);
                            outEntry.setCrc(crc.getValue());
                            outEntry.setCompressedSize(byteCode.length);
                        }
                        out.putNextEntry(outEntry);
                        out.write(byteCode);
                        out.closeEntry();
                    }
