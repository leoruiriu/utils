package jp.webtoon.core.bitflag.util;

import org.apache.commons.lang.StringUtils;

public class Base32BitFlag {

	/** 32進数一桁が表現できるbit数 */
	private static final int EXPONENT = 5;

	private enum Base32Digits {
		a(0), b(1), c(2), d(3), e(4), f(5), g(6), h(7),
		i(8), j(9), k(10), l(11), m(12), n(13), o(14), p(15),
		q(16), r(17), s(18), t(19), u(20), v(21), w(22), x(23),
		y(24), z(25), A(26), B(27), C(28), D(29), E(30), F(31);

		private int decimalNo;

		Base32Digits(int decimalNo) {
			this.decimalNo = decimalNo;
		}

		public int getDecimalNo() {
			return this.decimalNo;
		}
	};

	private static int[] ON_FLAG_DECIMAL_NOS = new int[] {1, 2, 4, 8, 16};

	public static String turnOnFlag(String originStr, int targetBitIdx) throws IllegalArgumentException {
		// onにするbitの位置が何番目の文字なのか計算
		int targetCharIdx = (targetBitIdx - 1) / EXPONENT;

		String resultStr = originStr;
		// 元の文字列の長さが足しない場合は「a」で埋めておく
		if (StringUtils.isBlank(resultStr)) {
			resultStr = StringUtils.rightPad(StringUtils.EMPTY, targetCharIdx + 1, Base32Digits.a.name());
		} else if (resultStr.length() < (targetCharIdx + 1)) {
			resultStr = StringUtils.rightPad(resultStr, targetCharIdx + 1, Base32Digits.a.name());
		}

		// 長さに問題なければ、対象となる文字だけ抽出
		String originChar = StringUtils.substring(resultStr, targetCharIdx, targetCharIdx + 1);

		// enumに設定されてchar以外はIllegalArgument例外
		Base32Digits originDigit = Base32Digits.valueOf(originChar);

		// 余りが、該当charの対象bitポジション
		int targetDecimalNo = ON_FLAG_DECIMAL_NOS[(targetBitIdx - 1) % EXPONENT];
		// 既に対象のindexのフラグがonの場合は、既存のoriginを返す
		if ((originDigit.getDecimalNo() & targetDecimalNo) == targetDecimalNo) {
			return originStr;
		}

		// 対象のindexをフラグonした文字に置き換えて返す
		return changeCharInPosition(resultStr, targetCharIdx, Base32Digits.values()[originDigit.getDecimalNo() | targetDecimalNo].name());
	}

	public static boolean isFlagOn(String originStr, int targetBitIdx) throws IllegalArgumentException {
		// onにするbitの位置が何番目の文字なのか計算
		int targetCharIdx = (targetBitIdx - 1) / EXPONENT;

		// 元の文字列の長さが足りない場合は、フラがは必ずoff
		if (StringUtils.isBlank(originStr) || originStr.length() < (targetCharIdx + 1)) {
			return false;
		}

		// 長さに問題なければ、対象となる文字だけ抽出
		String originChar = StringUtils.substring(originStr, targetCharIdx, targetCharIdx + 1);

		// enumに設定されてchar以外はIllegalArgument例外
		Base32Digits originDigit = Base32Digits.valueOf(originChar);

		// 余りが、該当charの対象bitポジション
		int targetDecimalNo = ON_FLAG_DECIMAL_NOS[(targetBitIdx - 1) % EXPONENT];
		// 既に対象のindexのフラグがonの場合は、true
		if ((originDigit.getDecimalNo() & targetDecimalNo) == targetDecimalNo) {
			return true;
		}

		return false;
	}

	private static String changeCharInPosition(String str, int pos, String ch){
		if (StringUtils.isBlank(str) || StringUtils.isBlank(ch)) return str;
		if (str.length() <= pos || ch.length() != 1) return str;

	    char[] charArray = str.toCharArray();
	    charArray[pos] = ch.charAt(0);
	    return new String(charArray);
	}

	public static void main(String[] args) {
		System.out.println(turnOnFlag("abq", 126));
		System.out.println(isFlagOn("abF", 15));
	}
}
