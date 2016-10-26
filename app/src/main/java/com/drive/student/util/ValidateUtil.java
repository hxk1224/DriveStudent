package com.drive.student.util;

import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正在表达式验证工具类（验证身份证、车牌号等）
 *
 * @author iStar
 */
public class ValidateUtil {

    /**
     * 验证IP地址
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIP(String str) {
        String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
        return match(regex, str);
    }

    /**
     * 验证网址Url
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsUrl(String str) {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return match(regex, str);
    }

    /**
     * 验证电话号码
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsTelephone(String str) {
        String regex = "^(\\d{3,4}-)?\\d{6,8}$";
        return match(regex, str);
    }

    /**
     * 验证输入密码条件(字符与数据同时出现)
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPassword(String str) {
        String regex = "[A-Za-z]+[0-9]";
        return match(regex, str);
    }

    /**
     * 验证密码是否合法，6-12位的数字或者密码
     *
     * @param pwd
     * @return
     * @author 韩新凯
     * @update 2015年8月24日 下午12:06:43
     */
    public static boolean isPasswordValid(String pwd) {
        String regex = "^[0-9a-zA-Z]{6,12}$";
        return match(regex, pwd);
    }

    /**
     * 验证输入密码长度 (6-18位)
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPasswLength(String str) {
        String regex = "^\\d{6,18}$";
        return match(regex, str);
    }

    /**
     * 验证输入邮政编号
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPostalcode(String str) {
        String regex = "^\\d{6}$";
        return match(regex, str);
    }

    /**
     * 验证输入手机号码
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsHandset(String str) {
        String regex = "^[1]+[3,5]+\\d{9}$";
        return match(regex, str);
    }

    /**
     * 验证输入身份证号
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsIDcard(String str) {
        String regex = "(^\\d{18}$)|(^\\d{15}$)";
        return match(regex, str);
    }

    /**
     * 验证输入两位小数
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsDecimal(String str) {
        String regex = "^[0-9]+(.[0-9]{2})?$";
        return match(regex, str);
    }

    /**
     * 验证输入一年的12个月
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsMonth(String str) {
        String regex = "^(0?[[1-9]|1[0-2])$";
        return match(regex, str);
    }

    /**
     * 验证输入一个月的31天
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsDay(String str) {
        String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
        return match(regex, str);
    }

    /**
     * 验证日期时间
     *
     * @param 待验证的字符串
     * @return 如果是符合网址格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isDate(String str) {
// 严格验证时间格式的(匹配[2002-01-31], [1997-04-30],
// [2004-01-01])不匹配([2002-01-32], [2003-02-29], [04-01-01])
// String regex =
// "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((01,3-9])|(1[0-2]))-(29|30)))))$";
// 没加时间验证的YYYY-MM-DD
// String regex =
// "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";
// 加了时间验证的YYYY-MM-DD 00:00:00
        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
        return match(regex, str);
    }

    /**
     * 验证数字输入
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsNumber(String str) {
        String regex = "^[0-9]*$";
        return match(regex, str);
    }

    /**
     * 验证非零的正整数
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsIntNumber(String str) {
        String regex = "^\\+?[1-9][0-9]*$";
        return match(regex, str);
    }

    /**
     * 验证大写字母
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsUpChar(String str) {
        String regex = "^[A-Z]+$";
        return match(regex, str);
    }

    /**
     * 验证小写字母
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLowChar(String str) {
        String regex = "^[a-z]+$";
        return match(regex, str);
    }

    /**
     * 验证验证输入字母
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLetter(String str) {
        String regex = "^[A-Za-z]+$";
        return match(regex, str);
    }

    /**
     * 验证验证输入汉字
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsChinese(String str) {
        String regex = "^[\u4e00-\u9fa5],{0,}$";
        return match(regex, str);
    }

    /**
     * 验证验证输入字符串
     *
     * @param 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLength(String str) {
        String regex = "^.{8,}$";
        return match(regex, str);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

// 3. 检查字符串重复出现的词
// 
// private void btnWord_Click(object sender, EventArgs e)
// {
// System.Text.RegularExpressions.MatchCollection matches =
// System.Text.RegularExpressions.Regex.Matches(label1.Text,
// 
// @"\b(?<word>\w+)\s+(\k<word>)\b",
// System.Text.RegularExpressions.RegexOptions.Compiled |
// System.Text.RegularExpressions.RegexOptions.IgnoreCase);
// if (matches.Count != 0)
// {
// foreach (System.Text.RegularExpressions.Match match in matches)
// {
// string word = match.Groups["word"].Value;
// MessageBox.Show(word.ToString(),"英文单词");
// }
// }
// else { MessageBox.Show("没有重复的单词"); }
// 
// 
// }
// 
// 4. 替换字符串
// 
// private void button1_Click(object sender, EventArgs e)
// {
// 
// string strResult =
// System.Text.RegularExpressions.Regex.Replace(textBox1.Text,
// @"[A-Za-z]\*?", textBox2.Text);
// MessageBox.Show("替换前字符:" + "\n" + textBox1.Text + "\n" + "替换的字符:" + "\n"
// + textBox2.Text + "\n" +
// 
// "替换后的字符:" + "\n" + strResult,"替换");
// 
// }
// 
// 5. 拆分字符串
// 
// private void button1_Click(object sender, EventArgs e)
// {
// //实例: 甲025-8343243乙0755-2228382丙029-32983298389289328932893289丁
// foreach (string s in
// System.Text.RegularExpressions.Regex.Split(textBox1.Text,@"\d{3,4}-\d*"))
// {
// textBox2.Text+=s; //依次输出 "甲乙丙丁"
// }
// 
// }

    /**
     * 验证str是否为正确的身份证格式
     *
     * @param str
     * @return
     */
    public static boolean isIdentityCard(EditText view) {
        boolean flag = true;
        String licenc = view.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        /*
         * { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
		 * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
		 * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
		 * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
		 * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
		 * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外" }
		 */
        String provinces = "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91";

        Pattern pattern = Pattern.compile("^[1-9]\\d{14}");
        Matcher matcher = pattern.matcher(licenc);
        Pattern pattern2 = Pattern.compile("^[1-9]\\d{16}[\\d,x,X]$");
        Matcher matcher2 = pattern2.matcher(licenc);
        // 粗略判断
        if (!matcher.find() && !matcher2.find()) {
            view.setError("身份证号必须为15或18位数字（最后一位可以为X）");
            flag = false;
        } else {
            // 判断出生地
            if (provinces.indexOf(licenc.substring(0, 2)) == -1) {
                view.setError("身份证号前两位不正确！");
                flag = false;
            }

            // 判断出生日期
            if (licenc.length() == 15) {
                String birth = "19" + licenc.substring(6, 8) + "-" + licenc.substring(8, 10) + "-" + licenc.substring(10, 12);
                try {
                    Date birthday = sdf.parse(birth);
                    if (!sdf.format(birthday).equals(birth)) {
                        view.setError("出生日期非法！");
                        flag = false;
                    }
                    if (birthday.after(new Date())) {
                        view.setError("出生日期不能在今天之后！");
                        flag = false;
                    }
                } catch (ParseException e) {
                    view.setError("出生日期非法！");
                    flag = false;
                }
            } else if (licenc.length() == 18) {
                String birth = licenc.substring(6, 10) + "-" + licenc.substring(10, 12) + "-" + licenc.substring(12, 14);
                try {
                    Date birthday = sdf.parse(birth);
                    if (!sdf.format(birthday).equals(birth)) {
                        view.setError("出生日期非法！");
                        flag = false;
                    }
                    if (birthday.after(new Date())) {
                        view.setError("出生日期不能在今天之后！");
                        flag = false;
                    }
                } catch (ParseException e) {
                    view.setError("出生日期非法！");
                    flag = false;
                }
            } else {
                view.setError("身份证号位数不正确，请确认！");
                flag = false;
            }
        }
        if (!flag) {
            view.requestFocus();
        }
        return flag;
    }

    /**
     * 不为空时，验证str是否为正确的身份证格式
     *
     * @param str
     * @return
     */
    public static boolean maybeIsIdentityCard(EditText view) {
        boolean flag = true;
        String licenc = view.getText().toString();
        if (!licenc.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			/*
			 * { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
			 * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
			 * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
			 * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
			 * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
			 * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外" }
			 */
            String provinces = "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91";

            Pattern pattern = Pattern.compile("^[1-9]\\d{14}");
            Matcher matcher = pattern.matcher(licenc);
            Pattern pattern2 = Pattern.compile("^[1-9]\\d{16}[\\d,x,X]$");
            Matcher matcher2 = pattern2.matcher(licenc);
            // 粗略判断
            if (!matcher.find() && !matcher2.find()) {
                view.setError("身份证号必须为15或18位数字（最后一位可以为X）");
                flag = false;
            } else {
                // 判断出生地
                if (provinces.indexOf(licenc.substring(0, 2)) == -1) {
                    view.setError("身份证号前两位不正确！");
                    flag = false;
                }

                // 判断出生日期
                if (licenc.length() == 15) {
                    String birth = "19" + licenc.substring(6, 8) + "-" + licenc.substring(8, 10) + "-" + licenc.substring(10, 12);
                    try {
                        Date birthday = sdf.parse(birth);
                        if (!sdf.format(birthday).equals(birth)) {
                            view.setError("出生日期非法！");
                            flag = false;
                        }
                        if (birthday.after(new Date())) {
                            view.setError("出生日期不能在今天之后！");
                            flag = false;
                        }
                    } catch (ParseException e) {
                        view.setError("出生日期非法！");
                        flag = false;
                    }
                } else if (licenc.length() == 18) {
                    String birth = licenc.substring(6, 10) + "-" + licenc.substring(10, 12) + "-" + licenc.substring(12, 14);
                    try {
                        Date birthday = sdf.parse(birth);
                        if (!sdf.format(birthday).equals(birth)) {
                            view.setError("出生日期非法！");
                            flag = false;
                        }
                        if (birthday.after(new Date())) {
                            view.setError("出生日期不能在今天之后！");
                            flag = false;
                        }
                    } catch (ParseException e) {
                        view.setError("出生日期非法！");
                        flag = false;
                    }
                } else {
                    view.setError("身份证号位数不正确，请确认！");
                    flag = false;
                }
            }
            if (!flag) {
                view.requestFocus();
            }
        }
        return flag;
    }

    /**
     * 验证str是否为正确的身份证格式
     *
     * @param str
     * @return
     */
    public static boolean isIdentityCard(String licenc) {
        boolean flag = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		/*
		 * { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
		 * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
		 * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
		 * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
		 * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
		 * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外" }
		 */
        String provinces = "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91";

        Pattern pattern = Pattern.compile("^[1-9]\\d{14}");
        Matcher matcher = pattern.matcher(licenc);
        Pattern pattern2 = Pattern.compile("^[1-9]\\d{16}[\\d,x,X]$");
        Matcher matcher2 = pattern2.matcher(licenc);
        // 粗略判断
        if (!matcher.find() && !matcher2.find()) {
            flag = false;
        } else {
            // 判断出生地
            if (provinces.indexOf(licenc.substring(0, 2)) == -1) {
                flag = false;
            }

            // 判断出生日期
            if (licenc.length() == 15) {
                String birth = "19" + licenc.substring(6, 8) + "-" + licenc.substring(8, 10) + "-" + licenc.substring(10, 12);
                try {
                    Date birthday = sdf.parse(birth);
                    if (!sdf.format(birthday).equals(birth)) {
                        flag = false;
                    }
                    if (birthday.after(new Date())) {
                        flag = false;
                    }
                } catch (ParseException e) {
                    flag = false;
                }
            } else if (licenc.length() == 18) {
                String birth = licenc.substring(6, 10) + "-" + licenc.substring(10, 12) + "-" + licenc.substring(12, 14);
                try {
                    Date birthday = sdf.parse(birth);
                    if (!sdf.format(birthday).equals(birth)) {
                        flag = false;
                    }
                    if (birthday.after(new Date())) {
                        flag = false;
                    }
                } catch (ParseException e) {
                    flag = false;
                }
            } else {
                flag = false;
            }
        }

        return flag;
    }

    /**
     * 验证str是否为正确的车牌号
     *
     * @param str
     * @return
     */
    public static boolean isPlateNo(EditText view) {
        String no = view.getText().toString().trim();
        if (no == null || no.equals("")) {
            return false;
        }
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String[] str1 = {"京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼", "渝", "川", "贵", "云", "藏", "陕", "甘", "青", "宁", "新", "农", "台", "中", "武", "WJ", "亥", "戌", "酉", "申", "未", "午", "巳", "辰", "卯", "寅", "丑", "子", "葵", "壬", "辛", "庚", "己", "戊", "丁", "丙", "乙", "甲", "河北", "山西", "北京", "北", "南", "兰", "沈", "济", "成", "广", "海", "空", "军", "京V", "使"};

        if (no.equals("新车")) {
            return true;
        }

        if (no.length() == 7) {
            int h = 0;
            for (int r = 0; r < no.length(); r++) {
                if (str.indexOf(no.charAt(r)) != -1) {
                    h++;
                }
            }
            if (h == 7) {
                return true;
            }
        }
        if (no.length() > 1) {

            String jq1 = no.substring(0, 1);
            String jq2 = no.substring(0, 2);

            for (int k = 0; k < str1.length; k++) {
                if (str1[k].equals(jq1)) {
                    if (no.length() <= 8) {
                        return true;
                    }
                }
                if (str1[k].equals(jq2)) {
                    if (no.length() <= 8) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isEmpty(TextView w, String displayStr) {
        if (StringUtil.empty(w.getText().toString().trim())) {
            w.setError(displayStr + "不能为空！");
            w.setFocusable(true);
            w.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean isNum(TextView w, String displayStr) {
        if (!StringUtil.num(w.getText().toString().trim())) {
            w.setError(displayStr + "必须为整数且大于0！");
            w.setFocusable(true);
            w.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    public static boolean isDouble(TextView w, String displayStr) {
        if (!StringUtil.decimal(w.getText().toString().trim())) {
            w.setError(displayStr + "必须为数字且大于0！");
            w.setFocusable(true);
            w.requestFocus();
            return true;
        }
        return false;
    }

    // 可以空,非空时必须正确
    public static boolean maybeAccountNumberEmpty(TextView w) {
        if (!StringUtil.empty(w.getText().toString().trim())) {
            return ValidateUtil.isAccountNumber(w);
        }
        return true;
    }

    // 可以空,非空时必须正确
    public static boolean maybeMobileEmpty(TextView w) {
        if (!StringUtil.empty(w.getText().toString().trim())) {
            return ValidateUtil.isMobileNumber(w);
        }
        return true;
    }

    public static boolean isArea(TextView w) {
        if (!RegexUtil.isArea(w.getText().toString().trim())) {
            w.setError("面积有非法字符！");
            w.setFocusable(true);
            return false;
        }
        return true;
    }

    public static boolean isMobileNumber(TextView w) {
        if (!RegexUtil.isMobileNumber(w.getText().toString().trim())) {
            w.setError("手机号码为11位数字！");
            w.setFocusable(true);
            return false;
        }
        return true;
    }

    public static boolean isMobileNumber(String phone) {
        return RegexUtil.isMobileNumber(phone);
    }

    /** 银行帐号为16-21位的数字 */
    public static boolean isAccountNumber(TextView w) {
        if (!RegexUtil.isAccountNumber(w.getText().toString().trim())) {
            w.setError("银行帐号必须为16-21位的数字！");
            w.setFocusable(true);
            return false;
        }
        return true;
    }

    /** 银行帐号为16-21位的数字 */
    public static boolean isAccountNumber(String cardNo) {
        return !(StringUtil.isBlank(cardNo) || !RegexUtil.isAccountNumber(cardNo));
    }

}
