package com.qingeng.apilibrary.http;


import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.URLConstant;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RetrofitInterface {


    //上传头像
    @Multipart
    @POST(URLConstant.URL_UPLOAD_HEAD_PIC)
    Observable<BaseResponseData> uploadHeadPic(@Part MultipartBody.Part pic);

    //登录
    @FormUrlEncoded
    @POST(URLConstant.LOGIN)
    Observable<BaseResponseData> login(@FieldMap Map<String, Object> map);

    //搜索用户
    @FormUrlEncoded
    @POST(URLConstant.QUERY_USERS)
    Observable<BaseResponseData> queryUsers(@Field("name") String name);

    //搜索用户
    @FormUrlEncoded
    @POST(URLConstant.QUERY_GROUP_LIST)
    Observable<BaseResponseData> queryGroupList(@Field("name") String name);

    //添加好友
    @FormUrlEncoded
    @POST(URLConstant.ADD_FRIENDS)
    Observable<BaseResponseData> addFriends(@Field("userId") String userId, @Field("msg") String msg, @Field("alias") String alias);

    //添加好友
    @FormUrlEncoded
    @POST(URLConstant.HANDLE_FRIEND_APPLY)
    Observable<BaseResponseData> handleFriendApply(@Field("id") String id, @Field("type") String type);

    //添加好友
    @POST(URLConstant.QUERY_FRIEND_TODO_LIST)
    Observable<BaseResponseData> queryFriendTodoList();

    //删除好友
    @FormUrlEncoded
    @POST(URLConstant.DEL_FRIENDS)
    Observable<BaseResponseData> delFriends(@Field("id") String id);

    //创建群组
    @FormUrlEncoded
    @POST(URLConstant.CREATE_TEAM)
    Observable<BaseResponseData> createTeam(@Field("userIds") String userIds);

    //获取其他用户信息
    @FormUrlEncoded
    @POST(URLConstant.GET_OTHER_USER_INFO)
    Observable<BaseResponseData> getOtherUserInfo(@Field("userId") String userId);


    //查询我的好友
    @FormUrlEncoded
    @POST(URLConstant.QUERY_MY_FRIENDS)
    Observable<BaseResponseData> queryMyFriends(@Field("name") String name);

    //查询我的群组
    @POST(URLConstant.MY_GROUP_LIST)
    Observable<BaseResponseData> queryMyGroupList();

    //查询黑名单
    @FormUrlEncoded
    @POST(URLConstant.QUERY_BLACK_LIST)
    Observable<BaseResponseData> queryBlackList(@Field("query") String query);

    //移除/加入 黑名单
    @FormUrlEncoded
    @POST(URLConstant.UPDATE_BLACK_LIST)
    Observable<BaseResponseData> updateBlackList(@Field("userId") String userId, @Field("type") int type);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.SET_DEL_MSG)
    Observable<BaseResponseData> setDelMsg(@Field("accid") String accid, @Field("type") int type);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.GET_ACTIVE_USER)
    Observable<BaseResponseData> getActiveUser(@Field("groupId") String groupId);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.RESET_USER_ACTIVE)
    Observable<BaseResponseData> resetUserActive(@Field("groupId") String groupId, @Field("userId") String userId);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.QUERY_EXIT_USER_LIST)
    Observable<BaseResponseData> queryExitUserList(@Field("groupId") String groupId, @Field("name") String name);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.AUTO_ADD_FRIEND)
    Observable<BaseResponseData> autoAddFriend(@Field("num") int num, @Field("clientIp") String clientIp, @Field("payStatus") int payStatus);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.QUERY_NONE_ACCEPT_RED_PACKET)
    Observable<BaseResponseData> queryNoneAcceptRedPacket(@FieldMap Map<String, Object> map);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPDATE_CLEAR_MSG)
    Observable<BaseResponseData> updateClearMsg(@FieldMap Map<String, Object> map);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.GROUP_HAS_EXPIRE)
    Observable<BaseResponseData> hasExpire(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.PENDING_USER_LIST)
    Observable<BaseResponseData> pendingUserList(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.HANDLE_PENDING)
    Observable<BaseResponseData> handlePending(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.LIKE_ACTIVE)
    Observable<BaseResponseData> likeActive(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.VIP_LIST)
    Observable<BaseResponseData> getVipList(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.PUBLISH_VIP)
    Observable<BaseResponseData> publishVip(@FieldMap Map<String, Object> map);

    //我的账单
    @POST(URLConstant.SHARE_PAGE_TITLE)
    Observable<BaseResponseData> sharePageTitle();


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.CREATE_WALLET)
    Observable<BaseResponseData> createWallet(@FieldMap Map<String, Object> map);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_PREPARE_ORDER)
    Observable<BaseResponseData> uPayPrepareOrder(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_WITHDREW_CREATE)
    Observable<BaseResponseData> withdrewCreate(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_TRANSFORM_CREATE)
    Observable<BaseResponseData> transformCreate(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_TRANSFORM_CONFIRM)
    Observable<BaseResponseData> transformConfirm(@FieldMap Map<String, Object> map);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_CREATE_TOKEN)
    Observable<BaseResponseData> uPayCreateToken(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_REDPACKAGE_CREATE)
    Observable<BaseResponseData> redPackageCreate(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_RED_PACKAGE_QUERY)
    Observable<BaseResponseData> redPackageQuery(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_RED_PACKET_STATUS)
    Observable<BaseResponseData> packetStatus(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_GRAB_PACKET)
    Observable<BaseResponseData> grabPacket(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_ORDER_PAY_CREATE)
    Observable<BaseResponseData> orderPayCreate(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_QUERY_ORDER_PAY_STATUS)
    Observable<BaseResponseData> queryOrderPayStatus(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPAY_UPDATE_MOBILE)
    Observable<BaseResponseData> updateUpayMobile(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.ADD2BLACKLIST)
    Observable<BaseResponseData> add2BlackList(@Field("groupId") String groupId, @Field("userId") String userId, @Field("type") String type);

    //设置备注名称
    @FormUrlEncoded
    @POST(URLConstant.UPDATE_ALIAS)
    Observable<BaseResponseData> updateAlias(@Field("id") int id, @Field("alias") String alias);


    //查询用户是否置顶 静音 拉黑
    @FormUrlEncoded
    @POST(URLConstant.QUERY_TOP_OR_NOTICE)
    Observable<BaseResponseData> queryTopOrNotice(@Field("id") String id, @Field("type") int type);

    //查询用户是否置顶 静音 拉黑
    @FormUrlEncoded
    @POST(URLConstant.GET_OTHER_USER_INFO_BY_ACCID)
    Observable<BaseResponseData> userInfoByAccId(@Field("accId") String accId);

    //设置用户置顶
    @FormUrlEncoded
    @POST(URLConstant.SET_USER_TOP)
    Observable<BaseResponseData> setUserTop(@Field("userId") String accId, @Field("type") int type);

    //设置用户置顶
    @FormUrlEncoded
    @POST(URLConstant.SET_GROUP_TOP)
    Observable<BaseResponseData> setGroupTop(@Field("id") String tId, @Field("type") int type);

    //设置免打扰
    @FormUrlEncoded
    @POST(URLConstant.SET_USER_NOTICE)
    Observable<BaseResponseData> setUserNotice(@Field("userId") String accId, @Field("type") int type);

    //举报
    @POST(URLConstant.ADD_REPORT)
    Observable<BaseResponseData> addReport(@Body RequestBody body);


    //群详情
    @FormUrlEncoded
    @POST(URLConstant.GROUP_DETAIL)
    Observable<BaseResponseData> groupDetail(@Field("tId") String groupId);


    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.MODIFY_TALK_NAME)
    Observable<BaseResponseData> modifyTalkName(@Field("groupId") String groupId, @Field("talkName") String talkName, @Field("userId") String userId);


    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.GROUP_ANNOUNCEMENT_LIST)
    Observable<BaseResponseData> groupAnnouncementList(@Field("groupId") String groupId);


    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.UPDATE_GROUP_INFO)
    Observable<BaseResponseData> updateGroupInfo(@FieldMap Map<String, Object> map);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.DELETE_GROUP)
    Observable<BaseResponseData> deleteGroup(@Field("groupId") String groupId);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.GET_OWNER_AND_ADMIN)
    Observable<BaseResponseData> getOwnerAndAdmin(@Field("groupId") String groupId);


    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.REMOVE_ADMIN)
    Observable<BaseResponseData> removeAdmin(@Field("groupId") String groupId, @Field("userId") String userId);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.SET_ADMIN)
    Observable<BaseResponseData> setAdmin(@Field("groupId") String groupId, @Field("userId") String userId);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.QUERY_PAY_WAY)
    Observable<BaseResponseData> queryPayWay(@Field("tId") String tid);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.FORBIDDEN_USER)
    Observable<BaseResponseData> forbiddenUser(@FieldMap Map<String, Object> map);


    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.UPDATE_SHOW_NAME)
    Observable<BaseResponseData> updateShowName(@FieldMap Map<String, Object> map);


    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.CANCEL_MUTE)
    Observable<BaseResponseData> cancelMute(@FieldMap Map<String, Object> map);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.UPDATE_SCREEN_NOTIFY)
    Observable<BaseResponseData> updateScreenNotify(@FieldMap Map<String, Object> map);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.QUERY_PHONE_EXISTS)
    Observable<BaseResponseData> queryPhoneExists(@FieldMap Map<String, Object> map);


    //获取支付信息-阿里支付
    @POST(URLConstant.GET_ALI_PAY_ORDER_INFO)
    Observable<BaseResponseData> getPayOrderInfo_ali(@QueryMap Map<String, Object> map);


    //群名称设置
//    @FormUrlEncoded
//    @POST(URLConstant.ADD_FAVORITE_MSG)
//    Observable<BaseResponseData> addFavoriteMsg(@FieldMap Map<String, Object> map);

    @POST(URLConstant.ADD_FAVORITE_MSG)
    Observable<BaseResponseData> addFavoriteMsg(@Body RequestBody body);

    //群名称设置
    @POST(URLConstant.MY_FAVORITE)
    Observable<BaseResponseData> myFavorite();

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.REMOVE_FA)
    Observable<BaseResponseData> removeFavorite(@Field("id") String id);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.UPDATE_HEAD_IMAGE)
    Observable<BaseResponseData> updateHeadImage(@Field("url") String url);

    //群名称设置
    @FormUrlEncoded
    @POST(URLConstant.MODIFY_NICK_NAME)
    Observable<BaseResponseData> modifyNickname(@Field("username") String username);

    @FormUrlEncoded
    @POST(URLConstant.CHANGE_USER_ID)
    Observable<BaseResponseData> changeUserId(@Field("uniqueId") String uniqueId);

    @FormUrlEncoded
    @POST(URLConstant.RESET_PASSWORD)
    Observable<BaseResponseData> resetPassword(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(URLConstant.CHANGE_PHONE_NUMBER)
    Observable<BaseResponseData> changePhoneNumber(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(URLConstant.MODIFY_PASSWORD)
    Observable<BaseResponseData> modifyPassword(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(URLConstant.MODIFY_PAY_PASSWORD)
    Observable<BaseResponseData> modifyPayPassword(@FieldMap Map<String, Object> map);


    @POST(URLConstant.ADD_SUGGEST)
    Observable<BaseResponseData> addSuggest(@Body RequestBody body);

    @POST(URLConstant.GET_SERVICE_INFO)
    Observable<BaseResponseData> getServiceInfo();

    @POST(URLConstant.HELP_CENTER)
    Observable<BaseResponseData> helpCenter();

    @FormUrlEncoded
    @POST(URLConstant.PRIVACY_SETTING)
    Observable<BaseResponseData> privacySetting(@Field("number") int number, @Field("type") int type);

    @FormUrlEncoded
    @POST(URLConstant.GET_REPORT_ITEM)
    Observable<BaseResponseData> getReportItem(@Field("type") int type);

    @POST(URLConstant.UPDATE_GROUP_INFO_HEAD)
    Observable<BaseResponseData> updateGroupInfoHead(@Body RequestBody body);

    @FormUrlEncoded
    @POST(URLConstant.CHANGE_OWNER)
    Observable<BaseResponseData> changeOwner(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(URLConstant.KICK_USER)
    Observable<BaseResponseData> kickUser(@Field("groupId") String groupId, @Field("userId") String userId);

    @POST(URLConstant.THEME_LIST)
    Observable<BaseResponseData> themeList();


    @FormUrlEncoded
    @POST(URLConstant.SET_THEME)
    Observable<BaseResponseData> setTheme(@Field("themeId") int themeId);

    @FormUrlEncoded
    @POST(URLConstant.GET_ONE_THEME)
    Observable<BaseResponseData> getTheme(@Field("themeId") int themeId);

    @FormUrlEncoded
    @POST(URLConstant.REMOVE_FRIEND_ADD_LIST)
    Observable<BaseResponseData> removeFriendAddList(@Field("id") String id);

    @FormUrlEncoded
    @POST(URLConstant.REMOVE_ANNO)
    Observable<BaseResponseData> removeAnno(@Field("groupId") String groupId, @Field("anno") String anno);

    @FormUrlEncoded
    @POST(URLConstant.EXIT_GROUP)
    Observable<BaseResponseData> exitGroup(@Field("groupId") String groupId);

    @FormUrlEncoded
    @POST(URLConstant.INVITE_NEW_USER)
    Observable<BaseResponseData> inviteNewUser(@Field("groupId") String groupId, @Field("userId") String userId, @Field("scan") String scan);

    @FormUrlEncoded
    @POST(URLConstant.GROUP_SETNOTICE)
    Observable<BaseResponseData> groupSetNotice(@FieldMap Map<String, Object> map);

    @GET(URLConstant.GET_LAST_VERSION)
    Observable<BaseResponseData> getLastVersion();


    @GET
    Observable<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


    //添加银行卡
    @FormUrlEncoded
    @POST(URLConstant.ADD_BANK_INFO)
    Observable<BaseResponseData> addBankInfo(@FieldMap Map<String, Object> map);

    //发布创业动态
    @POST(URLConstant.PUBLISH_NEWS)
    Observable<BaseResponseData> publishNews(@Body RequestBody body);

    //修改
    @POST(URLConstant.UPDATE_MY_NEW)
    Observable<BaseResponseData> updateMyNew(@Body RequestBody body);

    //创业乐园信息
    @FormUrlEncoded
    @POST(URLConstant.GET_NEWS_LIST)
    Observable<BaseResponseData> getNewsList(@FieldMap Map<String, Object> map);

    //创业乐园信息
    @FormUrlEncoded
    @POST(URLConstant.MSG_LIST)
    Observable<BaseResponseData> msgList(@FieldMap Map<String, Object> map);

    //我的发布
    @FormUrlEncoded
    @POST(URLConstant.GET_MY_NEWS_LIST)
    Observable<BaseResponseData> getMyNewsList(@FieldMap Map<String, Object> map);

    //删除我的发布
    @FormUrlEncoded
    @POST(URLConstant.DELETE_MY_NEW)
    Observable<BaseResponseData> deleteMyNew(@FieldMap Map<String, Object> map);

    //重新上架我的发布
    @FormUrlEncoded
    @POST(URLConstant.RE_PUBLISH_MY_NEW)
    Observable<BaseResponseData> rePublishMyNew(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.GET_MY_BILL)
    Observable<BaseResponseData> getMyBill(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.SHARE_PAGE)
    Observable<BaseResponseData> sharePage(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.GET_PARTNER_DATA)
    Observable<BaseResponseData> getPartnerData(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.CREATE_RED_PACKAGE)
    Observable<BaseResponseData> createRedPackage(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.TRAN_2_WALLET)
    Observable<BaseResponseData> tran2Wallet(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(URLConstant.QUERY_TRAN_BY_ORDER_NO)
    Observable<BaseResponseData> queryTranByOrderNo(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.GRAB_PACKAGE)
    Observable<BaseResponseData> grabPackage(@FieldMap Map<String, Object> map);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.PACKAGE_DETAIL)
    Observable<BaseResponseData> packageDetail(@FieldMap Map<String, Object> map);


    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.UPDATE_NOTIFY_TYPE)
    Observable<BaseResponseData> updateNotifyType(@Field("type") int type);

    //我的账单
    @FormUrlEncoded
    @POST(URLConstant.GET_SYSTEM_USER)
    Observable<BaseResponseData> getSystemUser(@Field("type") int type);


    //****************************


    //获取验证码
    @FormUrlEncoded
    @POST(URLConstant.URL_SEND_CODE)
    Observable<BaseResponseData> sendCode(@Field("event") String event, @Field("phone") String phone, @Field("userType") String userType);

    //手机号注册
    @FormUrlEncoded
    @POST(URLConstant.URL_REGISTER_PHONE)
    Observable<BaseResponseData> register(@FieldMap Map<String, Object> map);

    //手机号密码登录
    @FormUrlEncoded
    @POST(URLConstant.URL_LOGIN_PHONE)
    Observable<BaseResponseData> loginByPhone(@FieldMap Map<String, Object> map);

    //短信验证码登录
    @FormUrlEncoded
    @POST(URLConstant.URL_LOGIN_CODE)
    Observable<BaseResponseData> loginBySmsCode(@FieldMap Map<String, Object> map);

    //短信验证码登录
    @FormUrlEncoded
    @POST(URLConstant.URL_BIND_PHONE)
    Observable<BaseResponseData> bindPhone(@Field("code") String code, @Field("phone") String phone, @Field("userType") String userType);

    //QQ登录
    @FormUrlEncoded
    @POST(URLConstant.URL_QQ_APP_LOGIN)
    Observable<BaseResponseData> qqAppLogin(@Field("accessToken") String accessToken, @Field("openId") String openId, @Field("userType") String userType);

    //wx登录
    @FormUrlEncoded
    @POST(URLConstant.URL_WX_APP_LOGIN)
    Observable<BaseResponseData> wxAppLogin(@Field("code") String code, @Field("userType") String userType);

    //ali授权
    @POST(URLConstant.URL_ALI_PAY_APP_AUTH)
    Observable<BaseResponseData> aliAppAuth();

    //ali登录
    @FormUrlEncoded
    @POST(URLConstant.URL_ALI_PAY_LOGIN)
    Observable<BaseResponseData> aliAppLogin(@Field("channelType") String channelType, @Field("result") String result, @Field("userType") String userType);

    //重置密码
    @FormUrlEncoded
    @POST(URLConstant.URL_RESET_PASSWORD)
    Observable<BaseResponseData> resetPassword(@Field("code") String code, @Field("phone") String phone, @Field("password") String password, @Field("userType") String userType);


    //获取用户信息
    @GET(URLConstant.URL_GET_USER_INFO)
    Observable<BaseResponseData> getUserInfo();

    //获取用户信息
    @GET(URLConstant.URL_UPDATE_WYIMTOKEN)
    Observable<BaseResponseData> updateWYIMToken();

    //获取用户信息
    @GET(URLConstant.URL_LOOK_SYSTEM_NOTICE)
    Observable<BaseResponseData> lookSystemNotice(@QueryMap Map<String, Object> map);

    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_UPDATE_PHONE)
    Observable<BaseResponseData> updatePhone(@FieldMap Map<String, Object> map);

    //意见反馈
    @GET(URLConstant.URL_VERIFY_CODE)
    Observable<BaseResponseData> verifyCode(@Query("code") String code, @Query("userType") String userType);

    //意见反馈
    @GET(URLConstant.URL_GET_PROPEDUCATION_LIST)
    Observable<BaseResponseData> getPropEducationList(@QueryMap Map<String, Object> map);

    //意见反馈
    @GET(URLConstant.URL_GET_DOCTOR_INFO)
    Observable<BaseResponseData> getDoctorInfo();

    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_ADD_PROPEDUCATION)
    Observable<BaseResponseData> addNewPropEduction(@FieldMap Map<String, Object> map);

    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_UPDATE_USERINFO)
    Observable<BaseResponseData> updateUserInfo(@FieldMap Map<String, Object> map);
    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_ADD_BANK_CARD)
    Observable<BaseResponseData> addBankCard(@FieldMap Map<String, Object> map);
    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_USER_WITHDRAWAL)
    Observable<BaseResponseData> userWithdrawal(@FieldMap Map<String, Object> map);

    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_UPDATE_SURVEY_SUBJECT)
    Observable<BaseResponseData> updateSurveySubject(@FieldMap Map<String, Object> map);

    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_ADD_SURVEY_SUBJECT)
    Observable<BaseResponseData> addSurveySubject(@FieldMap Map<String, Object> map);

    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_UPDATE_SURVEY_MODEL)
    Observable<BaseResponseData> updateSurveyModel(@FieldMap Map<String, Object> map);

    //意见反馈
    @FormUrlEncoded
    @POST(URLConstant.URL_ADD_SURVEY_MODEL)
    Observable<BaseResponseData> addNewSurveyModel(@FieldMap Map<String, Object> map);

    //意见反馈
    @GET(URLConstant.URL_DELETE_BANK_CARD)
    Observable<BaseResponseData> delBankCard(@Query("cardId") int cardId, @Query("userType") String userType);

    @GET(URLConstant.URL_DOCTOR_SET_SELF_ORDER_TIME)
    Observable<BaseResponseData> doctorSetSelfOrderTime(@QueryMap Map<String, Object> map);


    @GET(URLConstant.URL_GET_UPLOAD_URL)
    Observable<BaseResponseData> getDoctorUploadUrl(@QueryMap Map<String, Object> map);

    @GET(URLConstant.URL_SELECT_SURVEY_MODEL_LIST)
    Observable<BaseResponseData> selectSurveyModelList(@QueryMap Map<String, Object> map);

    @GET(URLConstant.URL_SELECT_SUBJECT_BY_MODEL_ID)
    Observable<BaseResponseData> selectSubjectByModelId(@QueryMap Map<String, Object> map);

    @GET(URLConstant.URL_LOOK_SURVEY_SUBJECT_DETIL_BY_ID)
    Observable<BaseResponseData> lookSurveySubjectDetilById(@QueryMap Map<String, Object> map);

    @GET(URLConstant.URL_FIND_REVIEW_LIST)
    Observable<BaseResponseData> findReviewList(@QueryMap Map<String, Object> map);

    @GET(URLConstant.URL_FIND_ORDERS_DOCTOR)
    Observable<BaseResponseData> findOrdersDoctor(@QueryMap Map<String, Object> map);

    @GET(URLConstant.URL_SELECT_USER_BANK_LIST)
    Observable<BaseResponseData> selectUserBankList(@QueryMap Map<String, Object> map);


    @GET(URLConstant.URL_GET_VISIT_ORDER_TIME)
    Observable<BaseResponseData> getVisitOrderTime();

    //上传文件
    @Multipart
    @POST(URLConstant.URL_FILE_UPLOAD)
    Observable<BaseResponseData> uploadFile(@Part MultipartBody.Part file);
}
