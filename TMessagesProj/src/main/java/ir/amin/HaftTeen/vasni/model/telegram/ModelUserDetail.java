package ir.amin.HaftTeen.vasni.model.telegram;


import ir.amin.HaftTeen.tgnet.TLRPC;

public class ModelUserDetail {

    public int id;
    public String first_name;
    public String last_name;
    public String username;
    public long access_hash;
    public String phone;
    public TLRPC.UserProfilePhoto photo;
    public TLRPC.UserStatus status;
    public int flags;
    public boolean self;
    public boolean contact;
    public boolean mutual_contact;
    public boolean deleted;
    public boolean bot;
    public boolean bot_chat_history;
    public boolean bot_nochats;
    public boolean verified;
    public boolean restricted;
    public boolean min;
    public boolean bot_inline_geo;
    public int bot_info_version;
    public String restriction_reason;
    public String bot_inline_placeholder;
    public String lang_code;
    public boolean inactive;
    public boolean explicit_content;
    public String province_code;
    public String province_name;
}


