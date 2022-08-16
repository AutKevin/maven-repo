package utiltest;

import com.autumn.tool.IdCardNumUtil;

public class IdCardNumUtilTest {
    public static void main(String[] args) {

        String idcard = "320481199310194695";
        char verifyCode = IdCardNumUtil.calculateVerifyCode(idcard);
        System.out.println("验证码为："+verifyCode);

        if(IdCardNumUtil.checkIdCard(idcard)){
            IdCardNumUtil id = new IdCardNumUtil(idcard);
            System.out.println(id.toString());
        }else {
            System.out.println("身份证不正确！");
        }

    }
}
