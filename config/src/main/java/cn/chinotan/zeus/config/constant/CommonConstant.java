package cn.chinotan.zeus.config.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 公共常量
 *
 * @author xingcheng
 * @date 2018-11-08
 */
public interface CommonConstant {

    /**
     * 默认页码为1
     */
    Long DEFAULT_PAGE_INDEX = 1L;

    /**
     * 默认页大小为10
     */
    Long DEFAULT_PAGE_SIZE = 10L;

    /**
     * 分页总行数名称
     */
    String PAGE_TOTAL_NAME = "total";

    /**
     * 分页数据列表名称
     */
    String PAGE_RECORDS_NAME = "records";

    /**
     * 分页当前页码名称
     */
    String PAGE_INDEX_NAME = "pageIndex";

    /**
     * 分页当前页大小名称
     */
    String PAGE_SIZE_NAME = "pageSize";

    /**
     * 登录用户
     */
    String LOGIN_SYS_USER = "loginSysUser";

    /**
     * 登录token
     */
    String JWT_DEFAULT_TOKEN_NAME = "token";

    /**
     * JWT用户名
     */
    String JWT_USERNAME = "username";

    /**
     * JWT刷新新token响应状态码
     */
    int JWT_REFRESH_TOKEN_CODE = 460;

    /**
     * JWT刷新新token响应状态码，
     * Redis中不存在，但jwt未过期，不生成新的token，返回361状态码
     */
    int JWT_INVALID_TOKEN_CODE = 461;

    /**
     * JWT Token默认密钥
     */
    String JWT_DEFAULT_SECRET = "666666";

    /**
     * JWT 默认过期时间，3600L，单位秒
     */
    Long JWT_DEFAULT_EXPIRE_SECOND = 3600L;

    /**
     * 默认头像
     */
    String DEFAULT_HEAD_URL = "";

    /**
     * 管理员角色名称
     */
    String ADMIN_ROLE_NAME = "管理员";

    String ADMIN_LOGIN = "adminLogin";

    /**
     * 验证码token
     */
    String VERIFY_TOKEN = "verifyToken";

    /**
     * 图片
     */
    String IMAGE = "image";

    /**
     * JPEG
     */
    String JPEG = "JPEG";

    /**
     * base64前缀
     */
    String BASE64_PREFIX = "data:image/png;base64,";

    /**
     * ..
     */
    String SPOT_SPOT = "..";

    /**
     * ../
     */
    String SPOT_SPOT_BACKSLASH = "../";

    /**
     * SpringBootAdmin登录信息
     */
    String ADMIN_LOGIN_SESSION = "adminLoginSession";

    /**
     * 用户浏览器代理
     */
    String USER_AGENT = "User-Agent";

    /**
     * 本机地址IP
     */
    String LOCALHOST_IP = "127.0.0.1";
    /**
     * 本机地址名称
     */
    String LOCALHOST_IP_NAME = "本机地址";
    /**
     * 局域网IP
     */
    String LAN_IP = "192.168";
    /**
     * 局域网名称
     */
    String LAN_IP_NAME = "局域网";

    /**
     * 登陆token front
     */
    String TOKEN = "token";

    /**
     * 默认的活动失效时间
     */
    Integer DEFAULT_TWO_DAY = 2;

    Integer DEFAULT_100 = 100;

    Integer DEFAULT_1 = 1;

    Integer DEFAULT_2 = 2;

    /**
     * des_key
     */
    byte[] DES_KEY = "--lcarus2020Guess++".getBytes();

    /**
     * 5
     */
    Integer DEFAULT_5 = 5;

    /**
     * des加密前缀
     */
    String DES_ENCRYPT_START_STRING = "+-";

    Long DEFAULT_LONG_1 = 1L;

    Long DEFAULT_LONG_NEGATIVE_1 = -1L;

    Long DEFAULT_LONG_ZERO = 0L;

    /**
     * 默认机器人id集合
     */
    List<Long> DEFAULT_ROBOT_USER_ID = Arrays.asList(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L, -9L, -10L, -11L, -12L, -13L, -14L, -15L, -16L, -17L, -18L, -19L, -20L, -21L, -22L, -23L, -14L, -25L, -26L, -27L, -28L, -29L, -30L, -31L, -32L, -33L, -34L, -35L, -36L, -37L, -38L, -39L, -40L, -41L, -42L, -43L, -44L, -45L, -46L, -47L, -48L, -49L, -50L);

    /**
     * 发放中
     */
    String SENDING = "SENDING";
    /**
     * 已发放待领取 
     */
    String SENT = "SENT";
    /**
     * 发放失败 
     */
    String FAILED = "FAILED";
    /**
     * 已领取 
     */
    String RECEIVED = "RECEIVED";
    /**
     * 退款中 
     */
    String RFUND_ING = "RFUND_ING";
    /**
     * 已退款
     */
    String REFUND = "REFUND";

    Integer DEFAULT_ZERO = 0;

    String REDIS_DELAY = "LCARUS:REDIS_DELAY";

}
