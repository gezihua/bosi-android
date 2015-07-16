package org.leon.aes;

public final class AES
{
	static
	{
		System.loadLibrary("aes");
	}
	
	// --------------加密和解密----------------
	private static final int	SUCCESSED						= 0;	// 加密或者解密成功
	private static final int	DST_FILE_EXIST					= -1;	// 目标文件已经存在
	private static final int	SRC_FILE_OPEN_FAIL				= -2;	// 原始文件打开失败
	private static final int	DST_FILE_CREATE_FAIL			= -3;	// 目标文件创建失败
	// --------------解密----------------
	private static final int	ENC_FILE_VERIFY_ERROR			= -4;	// 密文文件验证错误
	private static final int	ENC_FILE_VERIFY_LENGTH_ERROR	= -5;	// 密文文件长度验证错误

	// aes加密
	private native static int enc_AES(String key, String srcPath, String dstPath);

	// aes解密
	private native static int dec_AES(String key, String srcPath, String dstPath);
	
	private AES()
	{
	}

	/**
	 * 
	 * @return
	 */
	/**
	 * AES加密
	 * 
	 * @param key
	 *            对应的key
	 * @param srcPath
	 *            需要加密的明文文件路径
	 * @param dstPath
	 *            所给的密文文件路径
	 * @return 返回操作完成结果状态码
	 */
	public static final int encAes(final String key, final String srcPath, final String dstPath)
	{
		return enc_AES(key, srcPath, dstPath);
	}

	/**
	 * AES解密
	 * 
	 * @param key
	 *            对应key
	 * @param srcPath
	 *            需要解密的密文文件路径
	 * @param dstPath
	 *            所给的明文文件路径
	 * @return 返回操作完成结果状态码
	 */
	public static final int decAes(final String key, final String srcPath, final String dstPath)
	{
		return dec_AES(key, srcPath, dstPath);
	}
}
