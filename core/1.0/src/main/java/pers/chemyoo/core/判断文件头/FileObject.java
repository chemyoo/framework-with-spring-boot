package pers.chemyoo.core.判断文件头;

import java.io.InputStream;

public class FileObject
{
	private String fileHeader;

	private InputStream inputStream;

	public FileObject(String fileHeader, InputStream inputStream)
	{
		this.fileHeader = fileHeader;
		this.inputStream = inputStream;
	}

	public String getFileHeader()
	{
		return fileHeader;
	}

	public InputStream getInputStream()
	{
		return inputStream;
	}

	public void setFileHeader(String fileHeader)
	{
		this.fileHeader = fileHeader;
	}

	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}

}
