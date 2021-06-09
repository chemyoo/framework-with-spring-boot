package pers.chemyoo.core.utils;

import java.util.Random;

public class CodeArray
{

	/**
	 * 基础码表
	 */
	private static final char[] CODES_ARRAY = initCodesArray();

	private CodeArray()
	{
		throw new AbstractMethodError("CodeArray can not be instance.");
	}

	/**
	 * 为了安全可以选择算法打乱基础码表
	 * 
	 * @return
	 */
	private static char[] initCodesArray()
	{
		char[] chars = new char[26 + 10 + 26 + 1];
		int index = 1;
		for (int i = 0; i < 26; i++, index++)
		{
			chars[index] = (char) ('A' + i);
		}
		for (int i = 0; i < 10; i++, index++)
		{
			chars[index] = (char) ('0' + i);
		}
		for (int i = 0; i < 26; i++, index++)
		{
			chars[index] = (char) ('a' + i);
		}
		chars[0] = '/';
		return chars;
	}

	/**
	 * 按指定方式打乱后的码表
	 * 
	 * @param seed 随机种子，随机种子相同，则生成的序列相同
	 * @param length 返回数组的长度
	 * @return
	 */
	public static char[] getCodesArray(final int seed, int length)
	{
		if (length < 10)
		{
			length = 10;
		}
		char[] chars = new char[CODES_ARRAY.length];
		if (length > chars.length)
		{
			length = chars.length;
		}
		System.arraycopy(CODES_ARRAY, 0, chars, 0, chars.length);
		Random random = new Random(seed);
		for (int i = chars.length - 1; i > chars.length - length; i--)
		{
			int index = random.nextInt(i);
			char temp = chars[index];
			chars[index] = chars[i];
			chars[i] = temp;
		}
		if (length == chars.length)
		{
			return chars;
		}
		char[] res = new char[length];
		System.arraycopy(chars, chars.length - length, res, 0, length);
		return res;
	}

	public static char[] getCodesArray(final int seed)
	{
		return getCodesArray(seed, CODES_ARRAY.length);
	}

	public static String getPulicKey(final int size)
	{
		Random random = new Random();
		StringBuilder sbBuilder = new StringBuilder();
		for (int i = 0; i < size; i++)
		{
			int x = random.nextInt(CODES_ARRAY.length);
			String hex = "0" + Integer.toHexString(x);
			sbBuilder.append(hex.substring(hex.length() - 2, hex.length()));
		}
		return sbBuilder.toString();
	}

	public static String getPrivateKey(final int seed, final String publicKey)
	{
		StringBuilder sbBuilder = new StringBuilder();
		char[] chars = getCodesArray(seed);
		int[] index = arrayIndex(publicKey);
		for (int i : index)
		{
			sbBuilder.append(chars[i]);
		}
		return sbBuilder.toString();
	}

	private static int[] arrayIndex(final String publicKey)
	{
		int[] index = new int[publicKey.length() / 2];
		for (int i = 0; i < index.length; i++)
		{
			index[i] = Integer.valueOf(publicKey.substring(i * 2, (i * 2) + 2), 16);
		}
		return index;
	}

	public static String getCodesString(final int seed, int length)
	{
		StringBuilder sbBuilder = new StringBuilder();
		char[] chars = getCodesArray(seed, length);
		for (char c : chars)
		{
			sbBuilder.append(c);
		}
		return sbBuilder.toString();
	}

	/**
	 * 通过基础码表生成加密的秘钥，公钥可在网络中传输，通过公钥到按指定序列的码表中获取私钥，从而达到公钥私钥动态生成的目标。
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.err.println(CODES_ARRAY);
		System.err.println(getCodesArray(20));
		String pulicKey = getPulicKey(32);
		System.err.println(pulicKey);
		System.err.println(getPrivateKey(20, pulicKey));
	}

}
