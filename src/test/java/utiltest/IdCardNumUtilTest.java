package utiltest;

import com.autumn.tool.IdCardNumUtil;

public class IdCardNumUtilTest {
    public static void main(String[] args) {

        String idcard = "320324199509265179";
        if(IdCardNumUtil.checkIdCard(idcard)){
            IdCardNumUtil id = new IdCardNumUtil(idcard);
            System.out.println(id.toString());
        }else {
            System.out.println("身份证不正确！");
        }

    }
}
