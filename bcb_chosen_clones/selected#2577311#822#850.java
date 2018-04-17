    public static SubtitleFile getInstance(File file, ArrayList<Subtitle> subtitleList, String charset, boolean useEmptyConstructor) throws FileConversionException, Exception {
        SubtitleFile result = null;
        String extension = Utils.getExtension(file);
        extension = extension.substring(0, 1).toUpperCase() + extension.substring(1).toLowerCase();
        try {
            Class<?> subtitleFileClass = Class.forName(SubtitleFile.class.getPackage().getName() + "." + extension + "File");
            if (!useEmptyConstructor) {
                Constructor<?> constructorClass = subtitleFileClass.getConstructor(File.class, ArrayList.class, String.class);
                result = (SubtitleFile) constructorClass.newInstance(file, subtitleList, charset);
            } else {
                Constructor<?> constructorClass = subtitleFileClass.getConstructor();
                result = (SubtitleFile) constructorClass.newInstance();
            }
        } catch (ClassNotFoundException e) {
            throw FileConversionException.getUnsupportedFileFormatException(file, extension);
        } catch (InvocationTargetException e) {
            Throwable wrappedException = e.getCause();
            if (wrappedException instanceof FileConversionException) {
                throw (FileConversionException) wrappedException;
            } else {
                throw (Exception) e;
            }
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
        return result;
    }
