def writefile(filename, fcontent, mode, isprint):
    """
    Write the string content to a file
    copied from
    http://www.pythonforbeginners.com/files/reading-and-writing-files-in-python
    :param filename: name of the file
    :param fcontent: string content to put into the file
    :param mode: writing mode, 'w' overwrite every time, 'a' append to an existing file
    :return: N/A
    """
    # try:
    file = open(filename, mode)
    file.write(fcontent)
    file.close()