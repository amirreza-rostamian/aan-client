package ir.amin.HaftTeen.vasni.model.api.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseProfileData implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("genderId")
    @Expose
    private Object genderId;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("pic")
    @Expose
    private String pic;
    @SerializedName("age")
    @Expose
    private Object age;
    @SerializedName("gradeId")
    @Expose
    private Integer gradeId;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("provinceId")
    @Expose
    private Object provinceId;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("birthday")
    @Expose
    private Object birthday;
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("voucher")
    @Expose
    private Boolean voucher;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("userChance")
    @Expose
    private Integer userChance;
    @SerializedName("userPoint")
    @Expose
    private Integer userPoint;
    @SerializedName("userTodayRank")
    @Expose
    private Integer userTodayRank;
    @SerializedName("userTotalRank")
    @Expose
    private Integer userTotalRank;
    @SerializedName("profile")
    @Expose
    private Boolean profile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getGenderId() {
        return genderId;
    }

    public void setGenderId(Object genderId) {
        this.genderId = genderId;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Object getAge() {
        return age;
    }

    public void setAge(Object age) {
        this.age = age;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Object getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Object provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Boolean getVoucher() {
        return voucher;
    }

    public void setVoucher(Boolean voucher) {
        this.voucher = voucher;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Integer getUserChance() {
        return userChance;
    }

    public void setUserChance(Integer userChance) {
        this.userChance = userChance;
    }

    public Integer getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(Integer userPoint) {
        this.userPoint = userPoint;
    }

    public Integer getUserTodayRank() {
        return userTodayRank;
    }

    public void setUserTodayRank(Integer userTodayRank) {
        this.userTodayRank = userTodayRank;
    }

    public Integer getUserTotalRank() {
        return userTotalRank;
    }

    public void setUserTotalRank(Integer userTotalRank) {
        this.userTotalRank = userTotalRank;
    }

    public Boolean getProfile() {
        return profile;
    }

    public void setProfile(Boolean profile) {
        this.profile = profile;
    }
}
