package com.example.administrator.emailcontact.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音,需导入pinyin4j-2.5.0.jar
 **/
public class Pinyin {

	public boolean PINYIN_FIRST = true ;
	public boolean PINYIN_ALL  = false ;

	/**
	 * 将输入的内容中汉字转化成全拼音或首拼音，字母和数字不变
	 * @param	 chiness 汉字
	 * @param	 isFirstPinYin true:将汉字转化为首字母拼音;false ：将汉字转化为全拼音
	 * @return 汉语拼音
	 * @author youngWM
	 * @:2015.7.18
	 **/
	public static String converterToSpell( String chiness, Boolean isFirstPinYin)
	{
		String pinyinName = "" ;
		char[] nameChar = chiness.toCharArray() ;
		HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat() ;
		outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		for (int i = 0; i < chiness.length(); i++) {
			String s = String.valueOf(nameChar[i]) ;
			if (s.matches("[\\u4e00-\\u9fa5]")) {   //-[\\u4e00-\\u9fa5]汉字机器码范围  
				try {
					if (isFirstPinYin) {
						pinyinName += PinyinHelper
								.toHanyuPinyinStringArray(nameChar[i], outputFormat)[0].charAt(0) ;//-转换成全拼
					}else{
						pinyinName += PinyinHelper
								.toHanyuPinyinStringArray(nameChar[i], outputFormat)[0] ;//-转换成首字母拼音
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			}
			else if (s.matches("[a-z]") ||s.matches("[A-Z]") ||s.matches("[0-9]") ) {
				pinyinName += s ;
			}
		}

		return pinyinName.toLowerCase() ;
	}
}
