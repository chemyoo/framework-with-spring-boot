package pers.chemyoo.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 按排列组合获取字符串
 * 
 * @author jianqing.liu
 * @since 2021年6月29日 上午10:54:50
 */
public class RadixNumber
{
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
		for (char i = '?'; i <= '@'; i++)
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

	public static void main(String[] args)
	{
		RadixNumber num = new RadixNumber(8, 10);
		System.err.println(num);
		for (int i = 0; i < 20; i++)
		{
			if (num.isOverflow())
			{
				num = new RadixNumber(num.getLength() + 1, 10);
			}
			System.err.println(num.getNextNumber());
		}
	}

}
