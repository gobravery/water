package unionlock.iceserver.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class CryptionDataUtils
{
	public static final String INIT_PWD = "poiuy123";

	private static String EncryptionString = "www.vnet.com.cn";
	
	

	// ��ʸ������Ҫ8�ֽڣ����ɱ����Ա�Լ�����
	private static final byte[] EncryptionIV = "qwerty".getBytes();

	// �������ܱ�׼
	private static final String DES = "DES/CBC/PKCS5Padding";
	
	private static boolean FLAG = false;

	public CryptionDataUtils()
	{
	}

	public CryptionDataUtils(String EncryptionString)
	{
		this.EncryptionString = EncryptionString;
	}

	/**
	 *根据控制加密
	 */
	public static byte[] EncryptionByteData(byte[] SourceData) throws Exception
	{
		if(FLAG) {
			byte[] retByte = null;
	
			byte[] EncryptionByte = EncryptionString.getBytes();
			DESKeySpec dks = new DESKeySpec(EncryptionByte);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
	
			IvParameterSpec spec = new IvParameterSpec(EncryptionIV);
	
			Cipher cipher = Cipher.getInstance(DES);
	
			cipher.init(Cipher.ENCRYPT_MODE, securekey, spec);
	
			retByte = cipher.doFinal(SourceData);
			return retByte;
		}else {
			return SourceData;
		}
	}

	/**
	 *根据控制 解密
	 */
	public static byte[] DecryptionByteData(byte[] SourceData) throws Exception
	{
		
		if(FLAG) {
			byte[] retByte = null;
	
			byte[] EncryptionByte = EncryptionString.getBytes();
			DESKeySpec dks = new DESKeySpec(EncryptionByte);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
	
			IvParameterSpec spec = new IvParameterSpec(EncryptionIV);
	
			Cipher cipher = Cipher.getInstance(DES);
	
			cipher.init(Cipher.DECRYPT_MODE, securekey, spec);
	
			retByte = cipher.doFinal(SourceData);
	
			return retByte;
		}else {
			return SourceData;
		}
	}

	/**

		根据控制加密
	 */
	public static String EncryptionStringData(String SourceData) throws Exception
	{
		if(StringUtils.isEmpty(SourceData)){
			return null;
		}
		if(FLAG) {
			return En(SourceData);
		}else {
			return SourceData;
		}
		// return SourceData;
	}
	/**

	直接控制加密
	 */
	public static String En(String SourceData) throws Exception{
		if(StringUtils.isEmpty(SourceData)){
			return null;
		}
		String retStr = null;
		byte[] retByte = null;

		byte[] sorData = SourceData.getBytes();

		retByte = EncryptionByteData(sorData);

		Base64 be = new Base64();
		retStr = new String(be.encode(retByte));
		return retStr;
		
	}
	/**
	 * 根据控制解密
	 */
	public static String DecryptionStringData(String SourceData) throws Exception
	{
		if(StringUtils.isEmpty(SourceData)){
			return null;
		}
		if(FLAG) {
			return De(SourceData);
		}else {
			return SourceData;
		}
	}
	/**
	 * 直接解密
	 */
	public static String De(String SourceData) throws Exception {
		if(StringUtils.isEmpty(SourceData)){
			return null;
		}
		String retStr = null;
		byte[] retByte = null;
		Base64 be = new Base64();
		byte[] sorData = be.decode(SourceData.getBytes());
		retByte = DecryptionByteData(sorData);
		retStr = new String(retByte);
		return retStr;
	}
	
	
	public static void main(String[] args) throws Exception{
		CryptionDataUtils c=new CryptionDataUtils();
		String cc="保存成功";
		System.out.println("加密:"+c.En(cc));
		System.out.println("解密:"+c.De(c.En(cc)));
		System.out.println(cc);
	}
}
