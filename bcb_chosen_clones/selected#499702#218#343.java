  @SuppressWarnings("finally")
  private void compress(File src) throws IOException
  {
    if (this.switches.contains(Switch.test))
      return;

    checkSourceFile(src);
    if (src.getPath().endsWith(".bz2"))
    {
      this.log.println("WARNING: skipping file because it already has .bz2 suffix:").println(src);
      return;
    }

    final File dst = new File(src.getPath() + ".bz2").getAbsoluteFile();
    if (!checkDestFile(dst))
      return;

    FileChannel       inChannel   = null;
    FileChannel       outChannel  = null;
    FileOutputStream  fileOut     = null;
    BZip2OutputStream bzOut       = null;
    FileLock          inLock      = null;
    FileLock          outLock     = null;

    try
    {
      inChannel = new FileInputStream(src).getChannel();
      final long inSize = inChannel.size();
      inLock = inChannel.tryLock(0, inSize, true);
      if (inLock == null)
        throw error("source file locked by another process: " + src);

      fileOut     = new FileOutputStream(dst);
      outChannel  = fileOut.getChannel();
      bzOut       = new BZip2OutputStream(
        new BufferedXOutputStream(fileOut, 8192),
        Math.min(
          (this.blockSize == -1) ? BZip2OutputStream.MAX_BLOCK_SIZE : this.blockSize,
          BZip2OutputStream.chooseBlockSize(inSize)
        )
      );

      outLock = outChannel.tryLock();
      if (outLock == null)
        throw error("destination file locked by another process: " + dst);

      final boolean showProgress = this.switches.contains(Switch.showProgress);
      long pos = 0;
      int progress = 0;

      if (showProgress || this.verbose)
      {
        this.log.print("source: " + src).print(": size=").println(inSize);
        this.log.println("target: " + dst);
      }

      while (true)
      {
        final long maxStep = showProgress ? Math.max(8192, (inSize - pos) / MAX_PROGRESS) : (inSize - pos);
        if (maxStep <= 0)
        {
          if (showProgress)
          {
            for (int i = progress; i < MAX_PROGRESS; i++)
              this.log.print('#');
            this.log.println(" done");
          }
          break;
        }
        else
        {
          final long step = inChannel.transferTo(pos, maxStep, bzOut);
          if ((step == 0) && (inChannel.size() != inSize))
            throw error("file " + src + " has been modified concurrently by another process");

          pos += step;
          if (showProgress)
          {
            final double  p           = (double) pos / (double) inSize;
            final int     newProgress = (int) (MAX_PROGRESS * p);
            for (int i = progress; i < newProgress; i++)
              this.log.print('#');
            progress = newProgress;
          }
        }
      }

      inLock.release();
      inChannel.close();
      bzOut.closeInstance();
      final long outSize = outChannel.position();
      outChannel.truncate(outSize);
      outLock.release();
      fileOut.close();

      if (this.verbose)
      {
        final double ratio = (inSize == 0) ? (outSize * 100) : ((double) outSize / (double) inSize);
        this.log.print("raw size: ").print(inSize)
          .print("; compressed size: ").print(outSize)
          .print("; compression ratio: ").print(ratio).println('%');
      }

      if (!this.switches.contains(Switch.keep))
      {
        if (!src.delete())
          throw error("unable to delete sourcefile: " + src);
      }
    }
    catch (final IOException ex)
    {
      IO.tryClose(inChannel);
      IO.tryClose(bzOut);
      IO.tryClose(fileOut);
      IO.tryRelease(inLock);
      IO.tryRelease(outLock);
      try
      {
        this.log.println();
      }
      finally
      {
        throw ex;
      }
    }
  }
