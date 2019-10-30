package me.himanshusoni.chatmessageview.Vasni.Permission;

import java.io.Serializable;


public interface PermissionCallback extends Serializable {
    void onClose();

    void onFinish();

    void onDeny(String permission, int position);

    void onGuarantee(String permission, int position);
}
