package com.qingeng.apilibrary.bean;

import java.util.List;

public class GroupDetailBean extends BaseBean {

    private WaHuHighGroup waHuHighGroup;
    private GroupUser waUserHighGroup;

    public WaHuHighGroup getWaHuHighGroup() {
        return waHuHighGroup;
    }

    public void setWaHuHighGroup(WaHuHighGroup waHuHighGroup) {
        this.waHuHighGroup = waHuHighGroup;
    }

    public GroupUser getWaUserHighGroup() {
        return waUserHighGroup;
    }

    public void setWaUserHighGroup(GroupUser waUserHighGroup) {
        this.waUserHighGroup = waUserHighGroup;
    }

    public static class WaHuHighGroup{

        private int id;
        private String tname;
        private String owner;
        private String members;
        private String announcement;
        private String intro;
        private String msg;
        private int magree;
        private int joinmode;
        private int beinvitemode;
        private int invitemode;
        private int uptinfomode;
        private int upcustommode;
        private int teamMemberLimit;
        private String tid;
        private int status;
        private String groupTypeName;
        private int groupType;
        private String higherStatus;
        private int muteAll;
        private String muteDesc;
        private String privateMode;
        private String noticeMode;
        private String invitemodeDesc;
        private String icon;
        private List<GroupUser> highGroups;
        private String substractMode;
        private String  expireDate;
        private String  groupStatus;
        private int  screenNotify;
        private int  clearMsg;
        private int  time;

        public int getClearMsg() {
            return clearMsg;
        }

        public void setClearMsg(int clearMsg) {
            this.clearMsg = clearMsg;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getScreenNotify() {
            return screenNotify;
        }

        public void setScreenNotify(int screenNotify) {
            this.screenNotify = screenNotify;
        }

        public String getGroupStatus() {
            return groupStatus;
        }

        public void setGroupStatus(String groupStatus) {
            this.groupStatus = groupStatus;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public String getSubstractMode() {
            return substractMode;
        }

        public void setSubstractMode(String substractMode) {
            this.substractMode = substractMode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getMembers() {
            return members;
        }

        public void setMembers(String members) {
            this.members = members;
        }

        public String getAnnouncement() {
            return announcement;
        }

        public void setAnnouncement(String announcement) {
            this.announcement = announcement;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getMagree() {
            return magree;
        }

        public void setMagree(int magree) {
            this.magree = magree;
        }

        public int getJoinmode() {
            return joinmode;
        }

        public void setJoinmode(int joinmode) {
            this.joinmode = joinmode;
        }

        public int getBeinvitemode() {
            return beinvitemode;
        }

        public void setBeinvitemode(int beinvitemode) {
            this.beinvitemode = beinvitemode;
        }

        public int getInvitemode() {
            return invitemode;
        }

        public void setInvitemode(int invitemode) {
            this.invitemode = invitemode;
        }

        public int getUptinfomode() {
            return uptinfomode;
        }

        public void setUptinfomode(int uptinfomode) {
            this.uptinfomode = uptinfomode;
        }

        public int getUpcustommode() {
            return upcustommode;
        }

        public void setUpcustommode(int upcustommode) {
            this.upcustommode = upcustommode;
        }

        public int getTeamMemberLimit() {
            return teamMemberLimit;
        }

        public void setTeamMemberLimit(int teamMemberLimit) {
            this.teamMemberLimit = teamMemberLimit;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getGroupTypeName() {
            return groupTypeName;
        }

        public void setGroupTypeName(String groupTypeName) {
            this.groupTypeName = groupTypeName;
        }

        public int getGroupType() {
            return groupType;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
        }

        public String getHigherStatus() {
            return higherStatus;
        }

        public void setHigherStatus(String higherStatus) {
            this.higherStatus = higherStatus;
        }

        public int getMuteAll() {
            return muteAll;
        }

        public void setMuteAll(int muteAll) {
            this.muteAll = muteAll;
        }

        public String getMuteDesc() {
            return muteDesc;
        }

        public void setMuteDesc(String muteDesc) {
            this.muteDesc = muteDesc;
        }

        public String getPrivateMode() {
            return privateMode;
        }

        public void setPrivateMode(String privateMode) {
            this.privateMode = privateMode;
        }

        public String getNoticeMode() {
            return noticeMode;
        }

        public void setNoticeMode(String noticeMode) {
            this.noticeMode = noticeMode;
        }

        public String getInvitemodeDesc() {
            return invitemodeDesc;
        }

        public void setInvitemodeDesc(String invitemodeDesc) {
            this.invitemodeDesc = invitemodeDesc;
        }

        public List<GroupUser> getHighGroups() {
            return highGroups;
        }

        public void setHighGroups(List<GroupUser> highGroups) {
            this.highGroups = highGroups;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }


    public static class GroupUser{
        /**
         * "currentUserIdentity":"群主",
         *                     "id":null,
         *                     "userId":26,
         *                     "groupId":25,
         *                     "topTalk":null,
         *                     "tname":null,
         *                     "groupType":null,
         *                     "createDate":null,
         *                     "identity":1,
         *                     "headImage":"http://xulyqn.ahrjkf.com/upload/2019-11-26/77d1c4d2852243cca1df1ca015e38a66.jpg",
         *                     "username":"突突突",
         *                     "status":null,
         *                     "expireDate":null,
         *                     "icon":null,
         *                     "notice":null,
         *                     "noticeDesc":"静音",
         *                     "topDesc":"非置顶",
         *                     "tid":null,
         *                     "accid":"516637131117187072",
         *                     "expireStatus":"未到期",
         *                     "talkName":"突突突",
         *                     "firstChar":"T"
         */


        private String currentUserIdentity;
        private int userId;
        private int groupId;
        private int identity;
        private String headImage;
        private String username;
        private String talkName;
        private String expireStatus;
        private String accid;
        private String muteStatus;
        private String groupStatus;
        private String noticeDesc;
        private String level;
        private int black;

        private int  showName;

        public int getShowName() {
            return showName;
        }

        public void setShowName(int showName) {
            this.showName = showName;
        }

        public String getNoticeDesc() {
            return noticeDesc;
        }

        public void setNoticeDesc(String noticeDesc) {
            this.noticeDesc = noticeDesc;
        }

        public String getGroupStatus() {
            return groupStatus;
        }

        public void setGroupStatus(String groupStatus) {
            this.groupStatus = groupStatus;
        }

        public String getCurrentUserIdentity() {
            return currentUserIdentity;
        }

        public void setCurrentUserIdentity(String currentUserIdentity) {
            this.currentUserIdentity = currentUserIdentity;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public int getIdentity() {
            return identity;
        }

        public void setIdentity(int identity) {
            this.identity = identity;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTalkName() {
            return talkName;
        }

        public void setTalkName(String talkName) {
            this.talkName = talkName;
        }

        public String getExpireStatus() {
            return expireStatus;
        }

        public void setExpireStatus(String expireStatus) {
            this.expireStatus = expireStatus;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getMuteStatus() {
            return muteStatus;
        }

        public void setMuteStatus(String muteStatus) {
            this.muteStatus = muteStatus;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getBlack() {
            return black;
        }

        public void setBlack(int black) {
            this.black = black;
        }
    }


}
