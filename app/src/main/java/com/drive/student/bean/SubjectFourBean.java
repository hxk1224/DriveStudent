package com.drive.student.bean;

import java.io.Serializable;

/** 科目一/科目四练习题 */
public class SubjectFourBean implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * {
     * "id" : "78",
     * "subject_number" : "118",
     * "subject_type" : "1",
     * "subject_title" : "如图所示，行驶过程中遇前方有障碍物的情况怎么办？",
     * "subject_img" : "http:\/\/appserver.huidang2105.com\/data\/uploads\/app\/subject_four\/choice\/118.jpg",
     * "answer_a" : "减速靠右行驶",
     * "answer_b" : "抢在绿车前绕过障碍",
     * "answer_c" : "开启左转向灯"
     * "answer_d" : "借对向车道绕过障碍",
     * "answer_true" : "A",
     * "subject_analysis" : "标准答案：A。安全第一，减速等右车通过了再过。  ",
     * }
     *
     * {
     "id" : "9",
     "subject_number" : "9",
     "subject_type" : "2",
     "subject_title" : "林某驾车以110公里\/小时的速度在城市道路行驶，与一辆机动车追尾后弃车逃离被群众拦下。经鉴定，事发时林某血液中的酒精浓度为135.8毫克\/百毫升。林某的主要违法行为是什么？",
     "subject_img" : "",
     "answer_a" : "醉酒驾驶",
     "answer_b" : "超速驾驶",
     "answer_c" : "疲劳驾驶"
     "answer_d" : "肇事逃逸",
     "answer_true" : "A\/B\/D",
     "subject_analysis" : "标准答案：ABD。违法行为一、以110公里\/小时的速度在城市道路行驶违法行为；二、与一辆机动车追尾后弃车逃离违法行为；三、林某血液中的酒精浓度为135.8毫克\/百毫升。  ",
     }
     */

    /** id */
    public String id;
    /** 题目计数 */
    public String subject_number;
    /** 类型:"0"-判断题  "1"-选择题, 判断题只有answer_a和answei_b "2"-科目四多选题 */
    public String subject_type;
    /** 题目 */
    public String subject_title;
    /** 图片 */
    public String subject_img;
    /** 答案A */
    public String answer_a;
    /** 答案B */
    public String answer_b;
    /** 答案C */
    public String answer_c;
    /** 答案D */
    public String answer_d;
    /** 正确答案 */
    public String answer_true;
    /** 问题分析 */
    public String subject_analysis;
}
