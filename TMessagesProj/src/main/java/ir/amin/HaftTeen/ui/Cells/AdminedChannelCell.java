/*
 * This is the source code of aan for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package ir.amin.HaftTeen.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import me.himanshusoni.chatmessageview.util.ViewUtil;
import ir.amin.HaftTeen.messenger.AndroidUtilities;
import ir.amin.HaftTeen.messenger.LocaleController;
import ir.amin.HaftTeen.messenger.MessagesController;
import ir.amin.HaftTeen.messenger.UserConfig;
import ir.amin.HaftTeen.tgnet.TLRPC;
import ir.amin.HaftTeen.ui.ActionBar.SimpleTextView;
import ir.amin.HaftTeen.ui.ActionBar.Theme;
import ir.amin.HaftTeen.ui.Components.AvatarDrawable;
import ir.amin.HaftTeen.ui.Components.BackupImageView;
import ir.amin.HaftTeen.ui.Components.LayoutHelper;
import ir.amin.HaftTeen.ui.Components.URLSpanNoUnderline;
import ir.amin.HaftTeen.R;

public class AdminedChannelCell extends FrameLayout {

    private BackupImageView avatarImageView;
    private SimpleTextView nameTextView;
    private SimpleTextView statusTextView;
    private AvatarDrawable avatarDrawable;
    private ImageView deleteButton;
    private TLRPC.Chat currentChannel;
    private boolean isLast;
    private int currentAccount = UserConfig.selectedAccount;

    public AdminedChannelCell(Context context, View.OnClickListener onClickListener) {
        super(context);

        avatarDrawable = new AvatarDrawable();

        avatarImageView = new BackupImageView(context);
        avatarImageView.setRoundRadius(AndroidUtilities.dp(24));
        addView(avatarImageView, LayoutHelper.createFrame(48, 48, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, LocaleController.isRTL ? 0 : 12, 12, LocaleController.isRTL ? 12 : 0, 0));

        nameTextView = new SimpleTextView(context);
        nameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        nameTextView.setTextSize(14);
        nameTextView.setTypeface(ViewUtil.getFont(context));
        nameTextView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP);
        addView(nameTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 20, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, LocaleController.isRTL ? 62 : 73, 15.5f, LocaleController.isRTL ? 73 : 62, 0));

        statusTextView = new SimpleTextView(context);
        statusTextView.setTextSize(10);
        statusTextView.setTypeface(ViewUtil.getFont(getContext()));
        statusTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        statusTextView.setLinkTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText));
        statusTextView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP);
        addView(statusTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 20, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, LocaleController.isRTL ? 62 : 73, 38.5f, LocaleController.isRTL ? 73 : 62, 0));

        deleteButton = new ImageView(context);
        deleteButton.setScaleType(ImageView.ScaleType.CENTER);
        deleteButton.setImageResource(R.drawable.msg_panel_clear);
        deleteButton.setOnClickListener(onClickListener);
        deleteButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText), PorterDuff.Mode.MULTIPLY));
        addView(deleteButton, LayoutHelper.createFrame(48, 48, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.TOP, LocaleController.isRTL ? 7 : 0, 12, LocaleController.isRTL ? 0 : 7, 0));
    }

    public void setChannel(TLRPC.Chat channel, boolean last) {
        TLRPC.FileLocation photo = null;
        if (channel.photo != null) {
            photo = channel.photo.photo_small;
        }
        final String url = MessagesController.getInstance(currentAccount).linkPrefix + "/";
        currentChannel = channel;
        avatarDrawable.setInfo(channel);
        nameTextView.setText(channel.title);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(url + channel.username);
        stringBuilder.setSpan(new URLSpanNoUnderline(""), url.length(), stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        statusTextView.setText(stringBuilder);
        avatarImageView.setImage(photo, "50_50", avatarDrawable);
        isLast = last;
    }

    public void update() {
        avatarDrawable.setInfo(currentChannel);
        avatarImageView.invalidate();
    }

    public TLRPC.Chat getCurrentChannel() {
        return currentChannel;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60 + (isLast ? 12 : 0)), MeasureSpec.EXACTLY));
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public SimpleTextView getNameTextView() {
        return nameTextView;
    }

    public SimpleTextView getStatusTextView() {
        return statusTextView;
    }

    public ImageView getDeleteButton() {
        return deleteButton;
    }
}