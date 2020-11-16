package com.qingeng.apilibrary.bean;

public class DoctorInfoResultBean extends BaseBean {

   private int AuthState;
   private DoctorInfoBean doctorInfo;
   private UserInfoBean user;
   private UserWalletBean userWallet;

   public int getAuthState() {
      return AuthState;
   }

   public void setAuthState(int authState) {
      AuthState = authState;
   }

   public DoctorInfoBean getDoctorInfo() {
      return doctorInfo;
   }

   public void setDoctorInfo(DoctorInfoBean doctorInfo) {
      this.doctorInfo = doctorInfo;
   }

   public UserInfoBean getUser() {
      return user;
   }

   public void setUser(UserInfoBean user) {
      this.user = user;
   }

   public UserWalletBean getUserWallet() {
      return userWallet;
   }

   public void setUserWallet(UserWalletBean userWallet) {
      this.userWallet = userWallet;
   }
}
