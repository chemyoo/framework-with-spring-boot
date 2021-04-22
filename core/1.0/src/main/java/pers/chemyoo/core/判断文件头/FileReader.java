package pers.chemyoo.core.判断文件头;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;


/**
 * 支持动态加载
 * 
 * @author jianqing.liu
 * @since 2020年4月9日 上午10:48:17
 */
public final class FileReader
{

	private FileReader()
	{
		throw new AbstractMethodError("FileReader can not instance.");
	}

	public static FileObject getFileType(InputStream is)
	{
		FileObject fileObject = getFileHeader(is);
//		for (FileType type : FileType.values())
//		{
//			if (fileObject.getFileHeader().toUpperCase().startsWith(type.getValue()))
//			{
//				fileObject.setFileHeader(type.getValue());
//				return fileObject;
//			}
//		}
		return fileObject;
	}

	public static String bytesToHexString(byte[] b)
	{

		StringBuilder stringBuilder = new StringBuilder();
		if (b == null || b.length <= 0)
		{
			return null;
		}
		for (int i = 0; i < b.length; i++)
		{
			int v = b[i] & 0xFF;
			String str = Integer.toHexString(v);
			if (str.length() < 2)
			{
				stringBuilder.append(0);
			}
			stringBuilder.append(str);
		}
		return stringBuilder.toString();
	}

	public static FileObject getFileHeader(InputStream inputStream)
	{
		byte[] b = new byte[28];
		try
		{
			if (inputStream.markSupported())
			{
				inputStream.mark(28);
				inputStream.read(b, 0, 28);
				inputStream.reset();
			}
			else
			{
				final List<ByteArrayInputStream> list = new ArrayList<>();
				byte[] buffers = new byte[8192];
				int length = -1;
				try
				{
					while ((length = inputStream.read(buffers)) > -1)
					{
						list.add(new ByteArrayInputStream(buffers, 0, length));
						buffers = new byte[8192];
					}
				}
				catch (IOException e)
				{
					list.clear();
				}
				if (!list.isEmpty())
				{
					ByteArrayInputStream is = list.get(0);
					is.mark(28);
					is.read(b, 0, 28);
					is.reset();
					return new FileObject(bytesToHexString(b), new SequenceInputStream(Collections.enumeration(list)));
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return new FileObject(bytesToHexString(b), inputStream);
	}

	public static void main(String[] args) throws IOException
	{
		File file = new File("C:", "\\Users\\chemyoo\\Desktop\\山东输变电技经协同\\V1.0.0\\ceb");
		for (File f : file.listFiles())
		{
			if (f.isFile())
			{
				FileInputStream fs = FileUtils.openInputStream(f);
				FileObject fo = FileReader.getFileType(fs);
				System.err.println(fo.getFileHeader());
			}
		}
	}

}
