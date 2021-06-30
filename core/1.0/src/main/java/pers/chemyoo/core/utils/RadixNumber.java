package pers.chemyoo.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

/**
 * 按排列组合获取字符串
 * 
 * @author jianqing.liu
 * @since 2021年6月29日 上午10:54:50
 */
public class RadixNumber implements Serializable
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 612543780622575305L;

	private int length;

	private int[] mark;

	private int radix;

	private int count = -1;

	public RadixNumber(int length, int radix)
	{
		if (radix >= base.length)
		{
			radix = base.length;
		}
		this.length = length;
		this.mark = new int[length];
		if (radix < 2)
		{
			radix = 2;
		}
		this.radix = radix - 1;
	}

	private Character[] base = initBase();

	private int min = 0;

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public int getMin()
	{
		return min;
	}

	public void setMin(int min)
	{
		this.min = min;
	}

	public Character[] getBase()
	{
		return base;
	}

	public int getRadix()
	{
		return radix;
	}

	public boolean isOverflow()
	{
		return this.count > 0;
	}

	public Character[] initBase()
	{
		List<Character> list = new ArrayList<>();
		for (char i = '0'; i <= '9'; i++)
		{
			list.add(i);
		}
		for (char i = 'a'; i <= 'z'; i++)
		{
			list.add(i);
		}
		for (char i = 'A'; i <= 'Z'; i++)
		{
			list.add(i);
		}
		for (char i = '!'; i <= '/'; i++)
		{
			list.add(i);
		}
		for (char i = ':'; i <= '@'; i++)
		{
			list.add(i);
		}
		for (char i = '['; i <= '`'; i++)
		{
			list.add(i);
		}
		for (char i = '{'; i <= '~'; i++)
		{
			list.add(i);
		}
		list.add(':');
		list.add(';');
		list.add('=');
		return list.toArray(new Character[0]);
	}

	public RadixNumber getNextNumber()
	{
		int pos = this.length - 1;
		while (pos >= min)
		{
			int temp = mark[pos];
			if (temp >= radix)
			{
				mark[pos] = 0;
				return getNextNumber(pos);
			}
			else
			{
				mark[pos] = ++mark[pos];
				break;
			}
		}
		boolean isAllFull = true;
		for (int i = 0; i < mark.length; i++)
		{
			if (mark[i] != radix)
			{
				isAllFull = false;
			}
		}
		if (isAllFull)
		{
			this.count++;
		}
		return this;
	}

	private RadixNumber getNextNumber(int index)
	{
		int pos = index - 1;
		while (pos >= min)
		{
			int temp = mark[pos];
			if (temp >= radix)
			{
				mark[pos] = 0;
				return getNextNumber(pos);
			}
			else
			{
				mark[pos] = ++mark[pos];
				break;
			}
		}
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			builder.append(base[mark[i]]);
		}
		return builder.toString();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		RadixNumber num = getRecord();
		int maxlength = 16;
		record(num);
		while (true)
		{
			if (num.getLength() > maxlength || "liujianqing".equals(num.toString()))
			{
				break;
			}
			System.err.println(num);
			if (num.isOverflow())
			{
				num = new RadixNumber(num.getLength() + 1, 100);
				continue;
			}
			num.getNextNumber();
		}
	}

	private static void record(RadixNumber num)
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				try (OutputStream out = new FileOutputStream("D:/num.obj");

						ObjectOutputStream outObj = new ObjectOutputStream(out))
				{
					outObj.writeObject(num);
					System.err.println("存储值" + num.toString());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}, TimeUnit.SECONDS.toMillis(5), TimeUnit.SECONDS.toMillis(10));
	}

	private static RadixNumber getRecord() throws IOException, ClassNotFoundException
	{
		File file = new File("D:/num.obj");
		if (file.exists())
		{
			try (ObjectInputStream in = new ObjectInputStream(FileUtils.openInputStream(file)))
			{
				return (RadixNumber) in.readObject();
			}
		}
		return new RadixNumber(8, 92);
	}

}
