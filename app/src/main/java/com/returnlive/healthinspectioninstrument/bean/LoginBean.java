package com.returnlive.healthinspectioninstrument.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/22 0022
 * 时间： 下午 2:55
 * 描述： 用户登录
 */

public class LoginBean {

    /**
     * status : success
     * info : {"id":"1744","e_id":"3","phone":"13182992165","password":"123456","phone_contacts":null,"card_img":"/default.jpg","name":null,"sex":"9","birth":"","nation":null,"card_id":null,"address":null,"province":null,"city":null,"district":null,"resident":"0","work":null,"blood":"5","blood_hr":"3","edu":"6","occ":"8","marr":"5","test":null,"status":"1","addtime":null,"lasttime":null}
     */

    private String status;
    private LoginMessage info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoginMessage getInfo() {
        return info;
    }

    public void setInfo(LoginMessage info) {
        this.info = info;
    }

    public static class LoginMessage {
        /**
         * id : 1744
         * e_id : 3
         * phone : 13182992165
         * password : 123456
         * phone_contacts : null
         * card_img : /default.jpg
         * name : null
         * sex : 9
         * birth :
         * nation : null
         * card_id : null
         * address : null
         * province : null
         * city : null
         * district : null
         * resident : 0
         * work : null
         * blood : 5
         * blood_hr : 3
         * edu : 6
         * occ : 8
         * marr : 5
         * test : null
         * status : 1
         * addtime : null
         * lasttime : null
         */

        private String id;
        private String e_id;
        private String phone;
        private String password;
        private Object phone_contacts;
        private String card_img;
        private Object name;
        private String sex;
        private String birth;
        private Object nation;
        private Object card_id;
        private Object address;
        private Object province;
        private Object city;
        private Object district;
        private String resident;
        private Object work;
        private String blood;
        private String blood_hr;
        private String edu;
        private String occ;
        private String marr;
        private Object test;
        private String status;
        private Object addtime;
        private Object lasttime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getE_id() {
            return e_id;
        }

        public void setE_id(String e_id) {
            this.e_id = e_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Object getPhone_contacts() {
            return phone_contacts;
        }

        public void setPhone_contacts(Object phone_contacts) {
            this.phone_contacts = phone_contacts;
        }

        public String getCard_img() {
            return card_img;
        }

        public void setCard_img(String card_img) {
            this.card_img = card_img;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public Object getNation() {
            return nation;
        }

        public void setNation(Object nation) {
            this.nation = nation;
        }

        public Object getCard_id() {
            return card_id;
        }

        public void setCard_id(Object card_id) {
            this.card_id = card_id;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getProvince() {
            return province;
        }

        public void setProvince(Object province) {
            this.province = province;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getDistrict() {
            return district;
        }

        public void setDistrict(Object district) {
            this.district = district;
        }

        public String getResident() {
            return resident;
        }

        public void setResident(String resident) {
            this.resident = resident;
        }

        public Object getWork() {
            return work;
        }

        public void setWork(Object work) {
            this.work = work;
        }

        public String getBlood() {
            return blood;
        }

        public void setBlood(String blood) {
            this.blood = blood;
        }

        public String getBlood_hr() {
            return blood_hr;
        }

        public void setBlood_hr(String blood_hr) {
            this.blood_hr = blood_hr;
        }

        public String getEdu() {
            return edu;
        }

        public void setEdu(String edu) {
            this.edu = edu;
        }

        public String getOcc() {
            return occ;
        }

        public void setOcc(String occ) {
            this.occ = occ;
        }

        public String getMarr() {
            return marr;
        }

        public void setMarr(String marr) {
            this.marr = marr;
        }

        public Object getTest() {
            return test;
        }

        public void setTest(Object test) {
            this.test = test;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getAddtime() {
            return addtime;
        }

        public void setAddtime(Object addtime) {
            this.addtime = addtime;
        }

        public Object getLasttime() {
            return lasttime;
        }

        public void setLasttime(Object lasttime) {
            this.lasttime = lasttime;
        }
    }
}
