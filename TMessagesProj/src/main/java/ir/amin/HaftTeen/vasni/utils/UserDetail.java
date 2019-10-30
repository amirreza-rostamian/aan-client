package ir.amin.HaftTeen.vasni.utils;

import android.content.Context;

import ir.amin.HaftTeen.messenger.UserConfig;
import ir.amin.HaftTeen.tgnet.TLRPC;
import ir.amin.HaftTeen.vasni.model.telegram.ModelUserDetail;


public class UserDetail {
    public static ModelUserDetail getUserDetail(final Context context) {
        int currentAccount = UserConfig.selectedAccount;
        TLRPC.User user = UserConfig.getInstance(currentAccount).getCurrentUser();
        ModelUserDetail modelUserDetail = new ModelUserDetail();
        try {
            modelUserDetail.id = user.id;
            modelUserDetail.first_name = user.first_name;
            modelUserDetail.last_name = user.last_name;
            modelUserDetail.username = user.username;
            modelUserDetail.access_hash = user.access_hash;
            modelUserDetail.phone = user.phone;
            modelUserDetail.photo = user.photo;
            modelUserDetail.status = user.status;
            modelUserDetail.flags = user.flags;
            modelUserDetail.self = user.self;
            modelUserDetail.contact = user.contact;
            modelUserDetail.mutual_contact = user.mutual_contact;
            modelUserDetail.deleted = user.deleted;
            modelUserDetail.bot = user.bot;
            modelUserDetail.bot_chat_history = user.bot_chat_history;
            modelUserDetail.bot_nochats = user.bot_nochats;
            modelUserDetail.verified = user.verified;
            modelUserDetail.restricted = user.restricted;
            modelUserDetail.min = user.min;
            modelUserDetail.bot_inline_geo = user.bot_inline_geo;
            modelUserDetail.bot_info_version = user.bot_info_version;
            modelUserDetail.restriction_reason = user.restriction_reason;
            modelUserDetail.bot_inline_placeholder = user.bot_inline_placeholder;
            modelUserDetail.lang_code = user.lang_code;
            modelUserDetail.inactive = user.inactive;
            modelUserDetail.explicit_content = user.explicit_content;
            modelUserDetail.province_code = MSharePk.getString(context, "PROVINCE_CODE", "");
            modelUserDetail.province_name = MSharePk.getString(context, "PROVINCE_NAME", "");
        } catch (Exception e) {
        }
        return modelUserDetail;
    }

    public static TLRPC.User getObjectUser(Context context) {
        int currentAccount = UserConfig.selectedAccount;
        return UserConfig.getInstance(currentAccount).getCurrentUser();
    }

}
