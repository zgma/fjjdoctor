package com.qingeng.apilibrary.http;

import android.text.TextUtils;

import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.ImageBean;
import com.qingeng.apilibrary.contact.MainConstant;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class HttpClient {

    public static void addFriends(String userId, String msg, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().addFriends(userId, msg, ""), httpInterface, requestCode);
    }

    public static void addFriends(String userId, String msg, String alias, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().addFriends(userId, msg, alias), httpInterface, requestCode);
    }

    public static void delFriends(String id, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().delFriends(id), httpInterface, requestCode);
    }

    public static void queryFriendTodoList(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryFriendTodoList(), httpInterface, requestCode);
    }

    public static void queryMyGroupList(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryMyGroupList(), httpInterface, requestCode);
    }

    public static void handleFriendApply(String id, String type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().handleFriendApply(id, type), httpInterface, requestCode);
    }

    public static void queryUsers(String name, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryUsers(name), httpInterface, requestCode);
    }

    public static void queryGroupList(String name, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryGroupList(name), httpInterface, requestCode);
    }

    public static void getOtherUserInfo(String userId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getOtherUserInfo(userId), httpInterface, requestCode);
    }

    public static void createTeam(String userIds, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().createTeam(userIds), httpInterface, requestCode);
    }

    public static void queryMyFriends(String name, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryMyFriends(name), httpInterface, requestCode);
    }

    public static void updateBlackList(String userId, int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateBlackList(userId, type), httpInterface, requestCode);
    }

    public static void setDelMsg(String accid, int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().setDelMsg(accid, type), httpInterface, requestCode);
    }

    public static void getActiveUser(String groupId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getActiveUser(groupId), httpInterface, requestCode);
    }

    public static void resetUserActive(String groupId, String userId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().resetUserActive(groupId, userId), httpInterface, requestCode);
    }


    public static void add2BlackList(String groupId, String userId, String type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().add2BlackList(groupId, userId, type), httpInterface, requestCode);
    }


    public static void queryExitUserList(String groupId, String name, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryExitUserList(groupId, name), httpInterface, requestCode);
    }

    public static void autoAddFriend(int count, String clientIp, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().autoAddFriend(count, clientIp, 0), httpInterface, requestCode);
    }

    public static void queryBlackList(String query, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryBlackList(query), httpInterface, requestCode);
    }

    public static void updateAlias(int id, String alias, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateAlias(id, alias), httpInterface, requestCode);
    }

    public static void queryTopOrNotice(String id, int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryTopOrNotice(id, type), httpInterface, requestCode);
    }


    public static void userInfoByAccId(String accId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().userInfoByAccId(accId), httpInterface, requestCode);
    }


    public static void setUserTop(String accId, int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().setUserTop(accId, type), httpInterface, requestCode);
    }

    public static void setGroupTop(String tId, int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().setGroupTop(tId, type), httpInterface, requestCode);
    }

    public static void setUserNotice(String accId, int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().setUserNotice(accId, type), httpInterface, requestCode);
    }

/*    public static void addReport(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().addReport(baseRequestBean.getBody()), httpInterface, requestCode);
    }*/

    public static void groupDetail(String groupId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().groupDetail(groupId), httpInterface, requestCode);
    }


    public static void updateGroupInfo(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateGroupInfo(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void queryNoneAcceptRedPacket(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryNoneAcceptRedPacket(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void updateClearMsg(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateClearMsg(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void hasExpire(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().hasExpire(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void pendingUserList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().pendingUserList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void handlePending(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().handlePending(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void likeActive(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().likeActive(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void getVipList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getVipList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void publishVip(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().publishVip(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void sharePageTitle(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().sharePageTitle(), httpInterface, requestCode);
    }


    public static void createWallet(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().createWallet(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void uPayPrepareOrder(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().uPayPrepareOrder(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void uPayWithdrewCreate(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().withdrewCreate(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void transformCreate(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().transformCreate(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void transformConfirm(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().transformConfirm(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void uPayCreateToken(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().uPayCreateToken(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void redPackageCreate(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().redPackageCreate(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void redPackageQuery(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().redPackageQuery(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void packetStatus(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().packetStatus(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void grabPacket(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().grabPacket(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void orderPayCreate(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().orderPayCreate(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void queryOrderPayStatus(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryOrderPayStatus(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void updateUpayMobile(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateUpayMobile(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void modifyTalkName(String groupId, String talkName, String userId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().modifyTalkName(groupId, talkName, userId), httpInterface, requestCode);
    }

    public static void inviteNewUser(String groupId, String userId, String scan, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().inviteNewUser(groupId, userId, scan), httpInterface, requestCode);
    }

    public static void groupAnnouncementList(String groupId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().groupAnnouncementList(groupId), httpInterface, requestCode);
    }

    public static void groupSetNotice(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().groupSetNotice(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void addBankInfo(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().addBankInfo(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void getNewsList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getNewsList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void msgList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().msgList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void getMyNewsList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getMyNewsList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void deleteMyNew(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().deleteMyNew(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void rePublishMyNew(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().rePublishMyNew(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void getMyBill(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getMyBill(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void sharePage(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().sharePage(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void getPartnerData(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getPartnerData(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void createRedPackage(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().createRedPackage(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void tran2Wallet(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().tran2Wallet(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void grabPackage(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().grabPackage(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void queryTranByOrderNo(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryTranByOrderNo(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void packageDetail(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().packageDetail(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void deleteGroup(String groupId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().deleteGroup(groupId), httpInterface, requestCode);
    }

    public static void getOwnerAndAdmin(String groupId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getOwnerAndAdmin(groupId), httpInterface, requestCode);
    }

    public static void removeAdmin(String groupId, String userId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().removeAdmin(groupId, userId), httpInterface, requestCode);
    }

    public static void setAdmin(String groupId, String userId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().setAdmin(groupId, userId), httpInterface, requestCode);
    }

    public static void queryPayWay(String tId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryPayWay(tId), httpInterface, requestCode);
    }


    public static void forbiddenUser(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().forbiddenUser(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void updateShowName(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateShowName(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void cancelMute(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().cancelMute(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void updateScreenNotify(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateScreenNotify(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void queryPhoneExists(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().queryPhoneExists(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void getPayOrderInfo_ali(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getPayOrderInfo_ali(baseRequestBean.getBody()), httpInterface, requestCode);
    }

//    public static void addFavoriteMsg(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
//        request(HttpServiceManager.getInstance().getRetrofit().addFavoriteMsg(baseRequestBean.getBody()), httpInterface, requestCode);
//    }

    public static void myFavorite(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().myFavorite(), httpInterface, requestCode);
    }

    public static void removeFavorite(String id, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().removeFavorite(id), httpInterface, requestCode);
    }

    public static void updateHeadImage(String url, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateHeadImage(url), httpInterface, requestCode);
    }

    public static void modifyNickname(String username, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().modifyNickname(username), httpInterface, requestCode);
    }

    public static void changeUserId(String id, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().changeUserId(id), httpInterface, requestCode);
    }

    public static void changePhoneNumber(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().changePhoneNumber(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void modifyPassword(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().modifyPassword(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void modifyPayPassword(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().modifyPayPassword(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void getServiceInfo(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getServiceInfo(), httpInterface, requestCode);
    }

    public static void helpCenter(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().helpCenter(), httpInterface, requestCode);
    }


    public static void privacySetting(int number, int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().privacySetting(number, type), httpInterface, requestCode);
    }

    public static void updateNotifyType(int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateNotifyType(type), httpInterface, requestCode);
    }

    public static void getSystemUser(int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getSystemUser(type), httpInterface, requestCode);
    }

    public static void getReportItem(int type, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getReportItem(type), httpInterface, requestCode);
    }

    public static void changeOwner(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().changeOwner(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void kickUser(String gId, String uId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().kickUser(gId, uId), httpInterface, requestCode);
    }


    public static void themeList(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().themeList(), httpInterface, requestCode);
    }

    public static void setTheme(int id, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().setTheme(id), httpInterface, requestCode);
    }


    public static void getTheme(int id, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getTheme(id), httpInterface, requestCode);
    }


    public static void removeFriendAddList(String id, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().removeFriendAddList(id), httpInterface, requestCode);
    }

    public static void getLastVersion(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getLastVersion(), httpInterface, requestCode);
    }


    public static void removeAnno(String groupId, String anno, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().removeAnno(groupId, anno), httpInterface, requestCode);
    }


    public static void exitGroup(String groupId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().exitGroup(groupId), httpInterface, requestCode);
    }


    private static void request(Observable<BaseResponseData> observable, final HttpInterface httpInterface, final int requestCode) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<BaseResponseData>() {
                    @Override
                    public void onNext(BaseResponseData o) {
                        if (null != o.getCode() && o.getCode().equals("409")) {
                            EventBus.getDefault().post("409");
                        } else if (null != o.getCode() && o.getCode().equals("-901")) {
                            this.onComplete();
                            EventBus.getDefault().post(o.getCode());
                        } else {
                            if (null == o.getCode() || o.getCode().equals("200")) {
                                httpInterface.onSuccess(requestCode, o);
                            } else {
                                httpInterface.onFailure(requestCode, o.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        httpInterface.onFailure(requestCode, throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        httpInterface.onComplete();
                    }
                });
    }

    public static void uploadHeadPic(File file, final HttpInterface httpInterface, final int requestCode) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        RequestBody photoRequestBody1 = RequestBody.create(MediaType.parse("image/jpeg"), file);
        builder.addFormDataPart("file", file.getName(), photoRequestBody1);
        MultipartBody.Part part = builder.build().part(0);
        request(HttpServiceManager.getInstance().getRetrofit().uploadHeadPic(part), httpInterface, requestCode);
    }

    public static void updateGroupInfoHead(int type, String gId, File file, final HttpInterface httpInterface, final int requestCode) {
        RequestBody fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("type", type + "")
                .addFormDataPart("groupId", gId)
                .addFormDataPart("file", file.getName(), fileRQ)
                .build();
        request(HttpServiceManager.getInstance().getRetrofit().updateGroupInfoHead(body), httpInterface, requestCode);
    }


    public static void CollectMessage(String type, String msgId, String content, String targetUserId, File file, final HttpInterface httpInterface, final int requestCode) {

        RequestBody body = null;
        if (type.equals("1")) {
            body = new MultipartBody.Builder()
                    .addFormDataPart("type", type + "")
                    .addFormDataPart("msgId", msgId)
                    .addFormDataPart("content", content)
                    .addFormDataPart("targetUserId", targetUserId + "")
                    .build();
        } else {
            RequestBody fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = new MultipartBody.Builder()
                    .addFormDataPart("type", type + "")
                    .addFormDataPart("msgId", msgId)
                    .addFormDataPart("targetUserId", targetUserId + "")
                    .addFormDataPart("file", file.getName(), fileRQ)
                    .build();
        }
        request(HttpServiceManager.getInstance().getRetrofit().addFavoriteMsg(body), httpInterface, requestCode);
    }


    public static void publishNews(String outUrl, String longitude, String latitude, String title, List<ImageBean> imageBeans, String clientIp, final HttpInterface httpInterface, final int requestCode) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .addFormDataPart("outUrl", outUrl)
                .addFormDataPart("longitude", longitude)
                .addFormDataPart("latitude", latitude)
                .addFormDataPart("clientIp", clientIp)
                .addFormDataPart("payStatus", "0")
                .addFormDataPart("title", title);
        for (int i = 0; i < imageBeans.size(); i++) {
            if (!TextUtils.isEmpty(imageBeans.get(i).getImage())) {
                File file = new File(imageBeans.get(i).getImage());
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart("files", file.getName(), requestBody);
            }
        }

        RequestBody body = builder.build();
        request(HttpServiceManager.getInstance().getRetrofit().publishNews(body), httpInterface, requestCode);
    }


    public static void chatReport(String content, int reportedId, int type, String targetId, String reportContent, List<File> files, final HttpInterface httpInterface, final int requestCode) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .addFormDataPart("content", content)
                .addFormDataPart("reportedId", reportedId + "")
                .addFormDataPart("type", type + "")
                .addFormDataPart("targetId", targetId)
                .addFormDataPart("reportContent", reportContent);
        for (int i = 0; i < files.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));
            builder.addFormDataPart("files", files.get(i).getName(), requestBody);
        }

        RequestBody body = builder.build();
        request(HttpServiceManager.getInstance().getRetrofit().addReport(body), httpInterface, requestCode);
    }


    public static void addSuggest(String content, List<File> files, HttpInterface httpInterface, int requestCode) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .addFormDataPart("content", content);
        for (int i = 0; i < files.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));
            builder.addFormDataPart("files", files.get(i).getName(), requestBody);
        }

        RequestBody body = builder.build();
        request(HttpServiceManager.getInstance().getRetrofit().addSuggest(body), httpInterface, requestCode);
    }


    public static void updateMyNew(int id, String outUrl, String longitude, String latitude, String title, String oldImageUrls,
                                   List<ImageBean> imageBeans, final HttpInterface httpInterface, final int requestCode) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .addFormDataPart("id", id + "")
                .addFormDataPart("outUrl", outUrl)
                .addFormDataPart("longitude", longitude)
                .addFormDataPart("latitude", latitude)
                .addFormDataPart("images", oldImageUrls)
                .addFormDataPart("title", title);
        for (int i = 0; i < imageBeans.size(); i++) {
            if (!TextUtils.isEmpty(imageBeans.get(i).getImage())) {
                File file = new File(imageBeans.get(i).getImage());
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart("files", file.getName(), requestBody);
            }
        }

        RequestBody body = builder.build();
        request(HttpServiceManager.getInstance().getRetrofit().updateMyNew(body), httpInterface, requestCode);
    }


    public static void download(String url, final String savePath, final DownloadInterface downloadInterface) {
        HttpServiceManager.getInstance().getRetrofit().downloadFileWithDynamicUrlSync(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody o) {
                        downloadInterface.onSuccess(writeResponseBodyToDisk(o, savePath));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        downloadInterface.onFailure(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        downloadInterface.onComplete();
                    }
                });

    }

    /**
     * 下载到本地
     *
     * @param body 内容
     * @return 成功或者失败
     */
    private static boolean writeResponseBodyToDisk(ResponseBody body, String path) {
        try {
            //判断文件夹是否存在

            //创建一个文件
            File futureStudioIconFile = new File(path);
            if (!futureStudioIconFile.exists()) futureStudioIconFile.createNewFile();
            //初始化输入流
            InputStream inputStream = null;
            //初始化输出流
            OutputStream outputStream = null;
            try {
                //设置每次读写的字节
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                //请求返回的字节流
                inputStream = body.byteStream();
                //创建输出流
                outputStream = new FileOutputStream(futureStudioIconFile);
                //进行读取操作
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    //进行写入操作
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                //刷新
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    //关闭输入流
                    inputStream.close();
                }
                if (outputStream != null) {
                    //关闭输出流
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    //***************
    public static void sendCode(String event, String phone, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().sendCode(event, phone, MainConstant.USER_TYPE), httpInterface, requestCode);
    }

    public static void register(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit(false).register(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void loginByPhone(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().loginByPhone(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void loginBySmsCode(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().loginBySmsCode(baseRequestBean.getBody()), httpInterface, requestCode);
    }


    public static void bindPhone(String code, String phone, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().bindPhone(code, phone, MainConstant.USER_TYPE), httpInterface, requestCode);
    }

    public static void qqAppLogin(String accessToken, String openId, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().qqAppLogin(accessToken, openId, MainConstant.USER_TYPE), httpInterface, requestCode);
    }

    public static void wxAppLogin(String code, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().wxAppLogin(code, MainConstant.USER_TYPE), httpInterface, requestCode);
    }

    public static void aliAppAuth(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().aliAppAuth(), httpInterface, requestCode);
    }

    public static void aliAppLogin(String result, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().aliAppLogin("android", result, MainConstant.USER_TYPE), httpInterface, requestCode);
    }

    public static void resetPassword(String code, String phone, String password, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().resetPassword(code, phone, password, MainConstant.USER_TYPE), httpInterface, requestCode);
    }

    public static void getUserInfo(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getUserInfo(), httpInterface, requestCode);
    }

    public static void updateWYIMToken(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateWYIMToken(), httpInterface, requestCode);
    }

    public static void lookSystemNotice(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().lookSystemNotice(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void updatePhone(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updatePhone(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void verifyCode(String code, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().verifyCode(code, MainConstant.USER_TYPE), httpInterface, requestCode);
    }

    public static void getLoginUserInfo(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getUserInfo(), httpInterface, requestCode);
    }

    public static void getPropEducationList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getPropEducationList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void addNewPropEduction(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().addNewPropEduction(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void addBankCard(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().addBankCard(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void userWithdrawal(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().userWithdrawal(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void updateSurveySubject(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateSurveySubject(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void addSurveySubject(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().addSurveySubject(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void updateSurveyModel(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateSurveyModel(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void addNewSurveyModel(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().addNewSurveyModel(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void delBankCard(int cardId, String userType, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().delBankCard(cardId, userType), httpInterface, requestCode);
    }

    public static void updateUserInfo(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().updateUserInfo(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void doctorSetSelfOrderTime(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().doctorSetSelfOrderTime(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void getDoctorUploadUrl(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getDoctorUploadUrl(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void selectSurveyModelList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().selectSurveyModelList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void selectSubjectByModelId(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().selectSubjectByModelId(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void lookSurveySubjectDetilById(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().lookSurveySubjectDetilById(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void findReviewList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().findReviewList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void findOrdersDoctor(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().findOrdersDoctor(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void selectUserBankList(BaseRequestBean baseRequestBean, HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().selectUserBankList(baseRequestBean.getBody()), httpInterface, requestCode);
    }

    public static void getVisitOrderTime(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getVisitOrderTime(), httpInterface, requestCode);
    }

    public static void getDoctorInfo(HttpInterface httpInterface, int requestCode) {
        request(HttpServiceManager.getInstance().getRetrofit().getDoctorInfo(), httpInterface, requestCode);
    }


    public static void uploadFile(File file, final HttpInterface httpInterface, final int requestCode) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        RequestBody photoRequestBody1 = RequestBody.create(MediaType.parse("image/*"), file);
        builder.addFormDataPart("file", file.getName(), photoRequestBody1);
        MultipartBody.Part part = builder.build().part(0);
        HttpServiceManager.getInstance().getRetrofit().uploadFile(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<BaseResponseData>() {
                    @Override
                    public void onNext(BaseResponseData o) {
                        if (null != o.getCode() && o.getCode().equals("409")) {
                            EventBus.getDefault().post("409");
                        } else {
                            if (null == o.getCode() || o.getCode().equals("200")) {
                                httpInterface.onSuccess(requestCode, o);
                            } else {
                                httpInterface.onFailure(requestCode, o.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        httpInterface.onFailure(requestCode, throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        httpInterface.onComplete();
                    }
                });
    }

}
