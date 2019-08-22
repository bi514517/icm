package app.creativestudio.dtu.truyenlalala.Ref;

public class UserInfo {
    public final static boolean IS_LOGGED = false;
    public final static int ID = 0;
    public final static String NAME = "Truyện Là Lá La";
    public final static String EMAIL = "xiaoyinyin142@gmail.com";
    public final static String AVATAR = "https://cdn6.aptoide.com/imgs/d/b/6/db62c2ecba7175794cbb7be32ecdd850_icon.png?w=240";
    public static boolean isLogged = false;
    public static int id = 0;
    public static String name = "Truyện Là Lá La";
    public static String email = "xiaoyinyin142@gmail.com";
    public static String avatar = "https://cdn6.aptoide.com/imgs/d/b/6/db62c2ecba7175794cbb7be32ecdd850_icon.png?w=240";
    public static void saveUserInfo(boolean isLogged1,int id1,String email1,String name1,String avatar1){
        isLogged = isLogged1;
        id = id1;
        email = email1;
        name = name1;
        avatar = avatar1;
    }
}
