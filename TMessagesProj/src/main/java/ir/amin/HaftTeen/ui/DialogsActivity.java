/*
 * This is the source code of aan for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package ir.amin.HaftTeen.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import me.himanshusoni.chatmessageview.Vasni.VasniSchema;
import me.himanshusoni.chatmessageview.util.AppSchema;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ir.amin.HaftTeen.PhoneFormat.PhoneFormat;
import ir.amin.HaftTeen.messenger.AndroidUtilities;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.messenger.BuildVars;
import ir.amin.HaftTeen.messenger.ChatObject;
import ir.amin.HaftTeen.messenger.ContactsController;
import ir.amin.HaftTeen.messenger.DataQuery;
import ir.amin.HaftTeen.messenger.DialogObject;
import ir.amin.HaftTeen.messenger.FileLog;
import ir.amin.HaftTeen.messenger.ImageLoader;
import ir.amin.HaftTeen.messenger.LocaleController;
import ir.amin.HaftTeen.messenger.MessageObject;
import ir.amin.HaftTeen.messenger.MessagesController;
import ir.amin.HaftTeen.messenger.NotificationCenter;
import ir.amin.HaftTeen.messenger.NotificationsController;
import ir.amin.HaftTeen.messenger.SharedConfig;
import ir.amin.HaftTeen.messenger.UserConfig;
import ir.amin.HaftTeen.messenger.UserObject;
import ir.amin.HaftTeen.messenger.support.widget.LinearLayoutManager;
import ir.amin.HaftTeen.messenger.support.widget.LinearSmoothScrollerMiddle;
import ir.amin.HaftTeen.messenger.support.widget.RecyclerView;
import ir.amin.HaftTeen.tgnet.ConnectionsManager;
import ir.amin.HaftTeen.tgnet.TLObject;
import ir.amin.HaftTeen.tgnet.TLRPC;
import ir.amin.HaftTeen.ui.ActionBar.ActionBar;
import ir.amin.HaftTeen.ui.ActionBar.ActionBarMenu;
import ir.amin.HaftTeen.ui.ActionBar.ActionBarMenuItem;
import ir.amin.HaftTeen.ui.ActionBar.AlertDialog;
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment;
import ir.amin.HaftTeen.ui.ActionBar.BottomSheet;
import ir.amin.HaftTeen.ui.ActionBar.MenuDrawable;
import ir.amin.HaftTeen.ui.ActionBar.Theme;
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription;
import ir.amin.HaftTeen.ui.Adapters.DialogsAdapter;
import ir.amin.HaftTeen.ui.Adapters.DialogsSearchAdapter;
import ir.amin.HaftTeen.ui.Cells.AccountSelectCell;
import ir.amin.HaftTeen.ui.Cells.DialogCell;
import ir.amin.HaftTeen.ui.Cells.DialogsEmptyCell;
import ir.amin.HaftTeen.ui.Cells.DividerCell;
import ir.amin.HaftTeen.ui.Cells.DrawerActionCell;
import ir.amin.HaftTeen.ui.Cells.DrawerAddCell;
import ir.amin.HaftTeen.ui.Cells.DrawerProfileCell;
import ir.amin.HaftTeen.ui.Cells.DrawerUserCell;
import ir.amin.HaftTeen.ui.Cells.GraySectionCell;
import ir.amin.HaftTeen.ui.Cells.HashtagSearchCell;
import ir.amin.HaftTeen.ui.Cells.HintDialogCell;
import ir.amin.HaftTeen.ui.Cells.LoadingCell;
import ir.amin.HaftTeen.ui.Cells.ProfileSearchCell;
import ir.amin.HaftTeen.ui.Cells.UserCell;
import ir.amin.HaftTeen.ui.Components.AlertsCreator;
import ir.amin.HaftTeen.ui.Components.AnimatedArrowDrawable;
import ir.amin.HaftTeen.ui.Components.AvatarDrawable;
import ir.amin.HaftTeen.ui.Components.BackupImageView;
import ir.amin.HaftTeen.ui.Components.ChatActivityEnterView;
import ir.amin.HaftTeen.ui.Components.CombinedDrawable;
import ir.amin.HaftTeen.ui.Components.EmptyTextProgressView;
import ir.amin.HaftTeen.ui.Components.FragmentContextView;
import ir.amin.HaftTeen.ui.Components.JoinGroupAlert;
import ir.amin.HaftTeen.ui.Components.LayoutHelper;
import ir.amin.HaftTeen.ui.Components.ProxyDrawable;
import ir.amin.HaftTeen.ui.Components.RadialProgressView;
import ir.amin.HaftTeen.ui.Components.RecyclerListView;
import ir.amin.HaftTeen.ui.Components.ScrollSlidingTextImageTabStrip;
import ir.amin.HaftTeen.ui.Components.ScrollSlidingTextTabStrip;
import ir.amin.HaftTeen.ui.Components.SizeNotifierFrameLayout;
import ir.amin.HaftTeen.ui.Components.StickersAlert;
import ir.amin.HaftTeen.vasni.adapter.DynamicViewAdapter;
import ir.amin.HaftTeen.vasni.api.ApiService;
import ir.amin.HaftTeen.vasni.model.api.ResponseGetBots;
import ir.amin.HaftTeen.vasni.model.api.profile.ResponseProfile;
import ir.amin.HaftTeen.vasni.model.teentaak.DynamicLayout;
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler;
import ir.amin.HaftTeen.vasni.teentaak.fragment.WalletFragment;
import ir.amin.HaftTeen.vasni.teentaak.fragment.scores.ScoreFragment;
import ir.amin.HaftTeen.vasni.utils.Function;
import ir.amin.HaftTeen.vasni.utils.MSharePk;
import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricGridView;
import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricGridViewAdapter;
import ir.amin.HaftTeen.BuildConfig;
import ir.amin.HaftTeen.R;

import static ir.amin.HaftTeen.vasni.extention.MFunctionsKt.getError;
import static ir.amin.HaftTeen.vasni.extention.MFunctionsKt.getSuccess;
import static ir.amin.HaftTeen.vasni.extention.MFunctionsKt.teenTaakEventClick;

public class DialogsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {
    public static boolean dialogsLoaded[] = new boolean[UserConfig.MAX_ACCOUNT_COUNT];
    private final AccelerateDecelerateInterpolator floatingInterpolator = new AccelerateDecelerateInterpolator();
    ScrollSlidingTextImageTabStrip scrollSlidingTextTabStrip;
    private RelativeLayout relativeLayout;
    private LinearLayout parentView;
    private RecyclerListView listView;
    private AsymmetricGridView asymmetricGridView;
    private DynamicViewAdapter asymetricGridAdapter;
    //private RecyclerListView gridView;
    private LinearLayoutManager layoutManager;
    //private GridLayoutManager layoutManagerForBot;
    // private BotsListAdapter botListAdapater;
    private DialogsAdapter dialogsAdapter;
    private ArrayList<DynamicLayout> itemModels = new ArrayList<>();
    private DialogsSearchAdapter dialogsSearchAdapter;
    private EmptyTextProgressView searchEmptyView;
    private RadialProgressView progressView;
    private ActionBarMenuItem passcodeItem;
    private ActionBarMenuItem proxyItem;
    private ProxyDrawable proxyDrawable;
    private ImageView floatingButton;
    private AlertDialog progressDialog;
    private int noOfColumn = 1;
    private ImageView unreadFloatingButton;
    private FrameLayout unreadFloatingButtonContainer;
    private TextView unreadFloatingButtonCounter;
    private int currentUnreadCount;
    private String phoneNumber;
    private AnimatedArrowDrawable arrowDrawable;
    private RecyclerView sideMenu;
    private ChatActivityEnterView commentView;
    private ActionBarMenuItem switchItem;
    private AlertDialog permissionDialog;
    private boolean askAboutContacts = true;
    private boolean proxyItemVisisble;
    private int prevPosition;
    private int prevTop;
    private boolean scrollUpdated;
    private boolean floatingHidden;
    private boolean checkPermission = true;
    private int currentConnectionState;
    private String selectAlertString;
    private String selectAlertStringGroup;
    private String addToGroupAlertString;
    private int dialogsType;
    private boolean searching;
    private boolean searchWas;
    private boolean onlySelect;
    private long selectedDialog;
    private String searchString;
    private long openedDialogId;
    private boolean cantSendToChannels;
    private boolean allowSwitchAccount;
    private boolean animatingForward;
    private DialogsActivityDelegate delegate;
    private ActionBarMenuItem searchItem;
    private FrameLayout profileView;
    private ResponseProfile profile;
    //Profile Item
    private TextView tv_user_name, tv_user_phone, tv_profile_balance, tv_profile_score, tv_version, tv_credit,
            tv_profile_add, tv_cloud_messege, tv_invite_friend, tv_profile_setting, tv_profile_notificationsandsounds,
            tv_profile_theme,tv_profile_language,tv_privacy_security,tv_more_setting,tv_app_version,tv_profile_update,
            tv_profile_score_title;
    private RelativeLayout rl_wallet, rl_scores;
    private BackupImageView imv_user_pic;
    private AppCompatImageView ll_top_bgr;
    private LinearLayout ll_add_account, ll_save_message, ll_invite, ll_more_setting, ll_select_language,
            ll_theme, ll_notification, ll_secure;


    public DialogsActivity(Bundle args) {
        super(args);
    }

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        if (getArguments() != null) {
            onlySelect = arguments.getBoolean("onlySelect", false);
            cantSendToChannels = arguments.getBoolean("cantSendToChannels", false);
            dialogsType = arguments.getInt("dialogsType", 0);
            selectAlertString = arguments.getString("selectAlertString");
            selectAlertStringGroup = arguments.getString("selectAlertStringGroup");
            addToGroupAlertString = arguments.getString("addToGroupAlertString");
            allowSwitchAccount = arguments.getBoolean("allowSwitchAccount");
        }

        if (dialogsType == 0) {
            askAboutContacts = MessagesController.getGlobalNotificationsSettings().getBoolean("askAboutContacts", true);
            SharedConfig.loadProxyList();
        }

        if (searchString == null) {
            currentConnectionState = ConnectionsManager.getInstance(currentAccount).getConnectionState();
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.dialogsNeedReload);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoaded);
            if (!onlySelect) {
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeSearchByActiveAction);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxySettingsChanged);
            }
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.encryptedChatUpdated);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.contactsDidLoaded);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.openedChatChanged);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.messageReceivedByAck);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.messageReceivedByServer);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.messageSendError);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.needReloadRecentDialogsSearch);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.didLoadedReplyMessages);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.reloadHints);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.didUpdatedConnectionState);
            NotificationCenter.getInstance(currentAccount).addObserver(this, NotificationCenter.dialogsUnreadCounterChanged);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        }

        if (!dialogsLoaded[currentAccount]) {
            MessagesController.getInstance(currentAccount).loadGlobalNotificationsSettings();
            MessagesController.getInstance(currentAccount).loadDialogs(0, 100, true);
            MessagesController.getInstance(currentAccount).loadHintDialogs();
            ContactsController.getInstance(currentAccount).checkInviteText();
            MessagesController.getInstance(currentAccount).loadPinnedDialogs(0, null);
            DataQuery.getInstance(currentAccount).loadRecents(DataQuery.TYPE_FAVE, false, true, false);
            DataQuery.getInstance(currentAccount).checkFeaturedStickers();
            dialogsLoaded[currentAccount] = true;
        }


        return true;
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (searchString == null) {
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
            if (!onlySelect) {
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeSearchByActiveAction);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
            }
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.encryptedChatUpdated);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.contactsDidLoaded);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.openedChatChanged);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.messageReceivedByAck);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.messageReceivedByServer);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.messageSendError);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.needReloadRecentDialogsSearch);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.didLoadedReplyMessages);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.reloadHints);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.didUpdatedConnectionState);
            NotificationCenter.getInstance(currentAccount).removeObserver(this, NotificationCenter.dialogsUnreadCounterChanged);

            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
        }
        if (commentView != null) {
            commentView.onDestroy();
        }
        delegate = null;
    }

    @Override
    public View createView(final Context context) {
        searching = false;
        searchWas = false;

        AndroidUtilities.runOnUIThread(() -> Theme.createChatResources(context, false));
        phoneNumber = MSharePk.getString(ApplicationLoader.applicationContext, AppSchema.phoneNumber, "").replace("98", "0");
        ActionBarMenu menu = actionBar.createMenu();
        if (!onlySelect && searchString == null) {
            proxyDrawable = new ProxyDrawable(context);
            proxyItem = menu.addItem(2, proxyDrawable);
            passcodeItem = menu.addItem(1, R.drawable.lock_close);
            updatePasscodeButton();
            updateProxyButton(false);
        }
        searchItem = menu.addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchExpand() {

                searching = true;
                if (switchItem != null) {
                    switchItem.setVisibility(View.GONE);
                }
                if (proxyItem != null && proxyItemVisisble) {
                    proxyItem.setVisibility(View.GONE);
                }
                if (listView != null) {
                    setVisibleDialogList(VasniSchema.Companion.getInstance().getChat());
                    if (searchString != null) {
                        listView.setEmptyView(searchEmptyView);
                        progressView.setVisibility(View.GONE);
                    }
                    if (!onlySelect) {
                        floatingButton.setVisibility(View.GONE);
                        unreadFloatingButtonContainer.setVisibility(View.GONE);
                    }
                }
                updatePasscodeButton();
            }

            @Override
            public boolean canCollapseSearch() {
                if (switchItem != null) {
                    switchItem.setVisibility(View.VISIBLE);
                }
                if (proxyItem != null && proxyItemVisisble) {
                    proxyItem.setVisibility(View.VISIBLE);
                }
                if (searchString != null) {
                    finishFragment();
                    return false;
                }
                return true;
            }

            @Override
            public void onSearchCollapse() {
                searching = false;
                searchWas = false;

                if (listView != null) {
                    listView.setEmptyView(progressView);
                    searchEmptyView.setVisibility(View.GONE);
                    if (!onlySelect) {
                        floatingButton.setVisibility(View.VISIBLE);
                        if (currentUnreadCount != 0) {
                            unreadFloatingButtonContainer.setVisibility(View.VISIBLE);
                            unreadFloatingButtonContainer.setTranslationY(AndroidUtilities.dp(74));
                        }
                        floatingHidden = true;
                        floatingButton.setTranslationY(AndroidUtilities.dp(100));
                        hideFloatingButton(false);
                    }
                    if (listView.getAdapter() != dialogsAdapter) {
                        listView.setAdapter(dialogsAdapter);
                        dialogsAdapter.notifyDataSetChanged();
                    }
                }
                if (dialogsSearchAdapter != null) {
                    dialogsSearchAdapter.searchDialogs(null);
                }
                updatePasscodeButton();

                if (asymmetricGridView != null && listView != null && profileView != null && scrollSlidingTextTabStrip.getCurrentTabId() == 0) {
                    setVisibleDialogList(VasniSchema.Companion.getInstance().getService());
                }
            }

            @Override
            public void onTextChanged(EditText editText) {
                String text = editText.getText().toString();
                if (text.length() != 0 || dialogsSearchAdapter != null && dialogsSearchAdapter.hasRecentRearch()) {
                    searchWas = true;
                    if (dialogsSearchAdapter != null && listView.getAdapter() != dialogsSearchAdapter) {
                        listView.setAdapter(dialogsSearchAdapter);
                        dialogsSearchAdapter.notifyDataSetChanged();
                    }
                    if (searchEmptyView != null && listView.getEmptyView() != searchEmptyView) {
                        progressView.setVisibility(View.GONE);
                        searchEmptyView.showTextView();
                        listView.setEmptyView(searchEmptyView);
                    }
                }
                if (dialogsSearchAdapter != null) {
                    dialogsSearchAdapter.searchDialogs(text);
                }
            }
        });
        searchItem.getSearchField().setHint(LocaleController.getString("Search", R.string.Search));
        if (onlySelect) {
            actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            if (dialogsType == 3 && selectAlertString == null) {
                actionBar.setTitle(LocaleController.getString("ForwardTo", R.string.ForwardTo));
            } else {
                actionBar.setTitle(LocaleController.getString("SelectChat", R.string.SelectChat));
            }
        } else {
            if (searchString != null) {
                actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            } else {
                //TODO remove NavigationDrawer Icon
                //actionBar.setBackButtonDrawable(new MenuDrawable());
            }
            if (BuildVars.DEBUG_VERSION) {
                actionBar.setTitle(LocaleController.getString("AppName", R.string.AppName));
            } else {
                actionBar.setTitle(LocaleController.getString("AppName", R.string.AppName));
            }
            actionBar.setSupportsHolidayImage(true);
        }
        actionBar.setTitleActionRunnable(() -> {
            hideFloatingButton(false);
            listView.smoothScrollToPosition(0);
        });

        if (allowSwitchAccount && UserConfig.getActivatedAccountsCount() > 1) {
            switchItem = menu.addItemWithWidth(1, 0, AndroidUtilities.dp(56));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setTextSize(AndroidUtilities.dp(12));

            BackupImageView imageView = new BackupImageView(context);
            imageView.setRoundRadius(AndroidUtilities.dp(18));
            switchItem.addView(imageView, LayoutHelper.createFrame(36, 36, Gravity.CENTER));

            TLRPC.User user = UserConfig.getInstance(currentAccount).getCurrentUser();
            avatarDrawable.setInfo(user);
            TLRPC.FileLocation avatar;
            if (user.photo != null && user.photo.photo_small != null && user.photo.photo_small.volume_id != 0 && user.photo.photo_small.local_id != 0) {
                avatar = user.photo.photo_small;
            } else {
                avatar = null;
            }
            imageView.getImageReceiver().setCurrentAccount(currentAccount);
            imageView.setImage(avatar, "50_50", avatarDrawable);

            for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
                TLRPC.User u = UserConfig.getInstance(a).getCurrentUser();
                if (u != null) {
                    AccountSelectCell cell = new AccountSelectCell(context);
                    cell.setAccount(a, true);
                    switchItem.addSubItem(10 + a, cell, AndroidUtilities.dp(230), AndroidUtilities.dp(48));
                }
            }
        }
        actionBar.setAllowOverlayTitle(true);

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    if (onlySelect) {
                        finishFragment();
                    } else if (parentLayout != null) {
                        parentLayout.getDrawerLayoutContainer().openDrawer(false);
                    }
                } else if (id == 1) {
                    SharedConfig.appLocked = !SharedConfig.appLocked;
                    SharedConfig.saveConfig();
                    updatePasscodeButton();
                } else if (id == 2) {
                    presentFragment(new ProxyListActivity());
                } else if (id >= 10 && id < 10 + UserConfig.MAX_ACCOUNT_COUNT) {
                    if (getParentActivity() == null) {
                        return;
                    }
                    DialogsActivityDelegate oldDelegate = delegate;
                    LaunchActivity launchActivity = (LaunchActivity) getParentActivity();
                    launchActivity.switchToAccount(id - 10, true);

                    DialogsActivity dialogsActivity = new DialogsActivity(arguments);
                    dialogsActivity.setDelegate(oldDelegate);
                    launchActivity.presentFragment(dialogsActivity, false, true);
                }
            }
        });

        if (sideMenu != null) {
            sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
            sideMenu.setGlowColor(Theme.getColor(Theme.key_chats_menuBackground));
            sideMenu.getAdapter().notifyDataSetChanged();
        }

        SizeNotifierFrameLayout contentView = new SizeNotifierFrameLayout(context) {

            int inputFieldHeight = 0;

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightSize = MeasureSpec.getSize(heightMeasureSpec);

                setMeasuredDimension(widthSize, heightSize);
                heightSize -= getPaddingTop();

                measureChildWithMargins(actionBar, widthMeasureSpec, 0, heightMeasureSpec, 0);

                int keyboardSize = getKeyboardHeight();
                int childCount = getChildCount();

                if (commentView != null) {
                    measureChildWithMargins(commentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                    Object tag = commentView.getTag();
                    if (tag != null && tag.equals(2)) {
                        if (keyboardSize <= AndroidUtilities.dp(20) && !AndroidUtilities.isInMultiwindow) {
                            heightSize -= commentView.getEmojiPadding();
                        }
                        inputFieldHeight = commentView.getMeasuredHeight();
                    } else {
                        inputFieldHeight = 0;
                    }
                }

                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    if (child == null || child.getVisibility() == GONE || child == commentView || child == actionBar) {
                        continue;
                    }
                    if (child == asymmetricGridView || child == listView || child == progressView || child == searchEmptyView || child == profileView) {
                        int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
                        int contentHeightSpec = MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10), heightSize - inputFieldHeight + AndroidUtilities.dp(2)), MeasureSpec.EXACTLY);
                        child.measure(contentWidthSpec, contentHeightSpec);
                    } else if (commentView != null && commentView.isPopupView(child)) {
                        if (AndroidUtilities.isInMultiwindow) {
                            if (AndroidUtilities.isTablet()) {
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320), heightSize - inputFieldHeight - AndroidUtilities.statusBarHeight + getPaddingTop()), MeasureSpec.EXACTLY));
                            } else {
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - inputFieldHeight - AndroidUtilities.statusBarHeight + getPaddingTop(), MeasureSpec.EXACTLY));
                            }
                        } else {
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, MeasureSpec.EXACTLY));
                        }
                    } else {
                        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                    }
                }
            }

            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                final int count = getChildCount();

                int paddingBottom;
                Object tag = commentView != null ? commentView.getTag() : null;
                if (tag != null && tag.equals(2)) {
                    paddingBottom = getKeyboardHeight() <= AndroidUtilities.dp(20) && !AndroidUtilities.isInMultiwindow ? commentView.getEmojiPadding() : 0;
                } else {
                    paddingBottom = 0;
                }
                setBottomClip(paddingBottom);

                for (int i = 0; i < count; i++) {
                    final View child = getChildAt(i);
                    if (child.getVisibility() == GONE) {
                        continue;
                    }
                    final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                    final int width = child.getMeasuredWidth();
                    final int height = child.getMeasuredHeight();

                    int childLeft;
                    int childTop;

                    int gravity = lp.gravity;
                    if (gravity == -1) {
                        gravity = Gravity.TOP | Gravity.LEFT;
                    }

                    final int absoluteGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                    final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                    switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                        case Gravity.CENTER_HORIZONTAL:
                            childLeft = (r - l - width) / 2 + lp.leftMargin - lp.rightMargin;
                            break;
                        case Gravity.RIGHT:
                            childLeft = r - width - lp.rightMargin;
                            break;
                        case Gravity.LEFT:
                        default:
                            childLeft = lp.leftMargin;
                    }

                    switch (verticalGravity) {
                        case Gravity.TOP:
                            childTop = lp.topMargin + getPaddingTop();
                            break;
                        case Gravity.CENTER_VERTICAL:
                            childTop = ((b - paddingBottom) - t - height) / 2 + lp.topMargin - lp.bottomMargin;
                            break;
                        case Gravity.BOTTOM:
                            childTop = ((b - paddingBottom) - t) - height - lp.bottomMargin;
                            break;
                        default:
                            childTop = lp.topMargin;
                    }

                    if (commentView != null && commentView.isPopupView(child)) {
                        if (AndroidUtilities.isInMultiwindow) {
                            childTop = commentView.getTop() - child.getMeasuredHeight() + AndroidUtilities.dp(1);
                        } else {
                            childTop = commentView.getBottom();
                        }
                    }
                    child.layout(childLeft, childTop, childLeft + width, childTop + height);
                }

                notifyHeightChanged();
            }
        };
        /*
         * Add Bottom Tab
         * */
        relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        contentView.addView(relativeLayout, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER, 0, 0, 0, 0));
        parentView = new LinearLayout(context);
        parentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        parentView.setOrientation(LinearLayout.VERTICAL);
        relativeLayout.addView(parentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER, 0, 0, 0, 48));
        //contentView.addView(parentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER, 0, 0, 0, 0));
        scrollSlidingTextTabStrip = new ScrollSlidingTextImageTabStrip(context);
        // scrollSlidingTextTabStrip.isNeedSetBadge = true;
        scrollSlidingTextTabStrip.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefault));
        scrollSlidingTextTabStrip.removeTabs();
        scrollSlidingTextTabStrip.addTextTab(0, LocaleController.getString("matches", R.string.matches), context.getResources().getDrawable(R.drawable.btm_service));
        scrollSlidingTextTabStrip.addTextTab(1, LocaleController.getString("chat", R.string.chat), context.getResources().getDrawable(R.drawable.btm_social));
        scrollSlidingTextTabStrip.addTextTab(2, LocaleController.getString("profile", R.string.profile), context.getResources().getDrawable(R.drawable.btm_account));
        relativeLayout.addView(scrollSlidingTextTabStrip, LayoutHelper.createRelative(-1, 48, RelativeLayout.ALIGN_PARENT_BOTTOM));
        //parentView.addView(scrollSlidingTextTabStrip, LayoutHelper.createRelative(-1, 48));

        if (phoneNumber.equals("") || phoneNumber == null) {
            logoutUser();
        } else {
            asymetricGridAdapter = new DynamicViewAdapter(context, itemModels);
            ApiService.INSTANCE.getApiInterface().getPages(0).enqueue(DialogsActivity.this);
            getProfileInfo();
        }

        scrollSlidingTextTabStrip.setDelegate(new ScrollSlidingTextImageTabStrip.ScrollSlidingTabStripDelegate() {
            @Override
            public void onPageSelected(int id, boolean forward) {
                switch (id) {
                    case 0:
                        setVisibleDialogList(VasniSchema.Companion.getInstance().getService());
                        showUnreadFloatingButton(false, true);
                        if (actionBar != null) {
                            actionBar.closeSearchField();
                        }
                        if (phoneNumber.equals("") || phoneNumber == null) {
                            logoutUser();
                        } else {
                            ApiService.INSTANCE.getApiInterface().getPages(0).enqueue(DialogsActivity.this);
                        }

                        break;
                    case 1:
                        if (actionBar != null) {
                            actionBar.closeSearchField();
                        }
                        setVisibleDialogList(VasniSchema.Companion.getInstance().getChat());
                        dialogsAdapter = new DialogsAdapter(context, dialogsType, true);
                        if (AndroidUtilities.isTablet() && openedDialogId != 0) {
                            dialogsAdapter.setOpenedDialogId(openedDialogId);
                        }
                        listView.setAdapter(dialogsAdapter);
                        break;
                    case 2:
                        if (actionBar != null) {
                            actionBar.closeSearchField();
                        }
                        setVisibleDialogList(VasniSchema.Companion.getInstance().getProfile());
                        showUnreadFloatingButton(false, false);
                        break;

                }
            }

            @Override
            public void onPageScrolled(float progress) {

            }
        });

        fragmentView = contentView;


        listView = new RecyclerListView(context);
        listView.setVerticalScrollBarEnabled(true);
        listView.setItemAnimator(null);
        listView.setInstantClick(true);
        listView.setLayoutAnimation(null);
        listView.setTag(4);
        layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScrollerMiddle linearSmoothScroller = new LinearSmoothScrollerMiddle(recyclerView.getContext());
                linearSmoothScroller.setTargetPosition(position);
                startSmoothScroll(linearSmoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        listView.setVerticalScrollbarPosition(LocaleController.isRTL ? RecyclerListView.SCROLLBAR_POSITION_LEFT : RecyclerListView.SCROLLBAR_POSITION_RIGHT);
//        contentView.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        parentView.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER, 0, 0, 0, 0));
        listView.setOnItemClickListener((view, position) -> {
            if (listView == null || listView.getAdapter() == null || getParentActivity() == null) {
                return;
            }
            long dialog_id = 0;
            int message_id = 0;
            boolean isGlobalSearch = false;
            RecyclerView.Adapter adapter = listView.getAdapter();
            if (adapter == dialogsAdapter) {
                TLObject object = dialogsAdapter.getItem(position);
                if (object instanceof TLRPC.User) {
                    dialog_id = ((TLRPC.User) object).id;
                } else if (object instanceof TLRPC.TL_dialog) {
                    dialog_id = ((TLRPC.TL_dialog) object).id;
                } else if (object instanceof TLRPC.TL_recentMeUrlChat) {
                    dialog_id = -((TLRPC.TL_recentMeUrlChat) object).chat_id;
                } else if (object instanceof TLRPC.TL_recentMeUrlUser) {
                    dialog_id = ((TLRPC.TL_recentMeUrlUser) object).user_id;
                } else if (object instanceof TLRPC.TL_recentMeUrlChatInvite) {
                    TLRPC.TL_recentMeUrlChatInvite chatInvite = (TLRPC.TL_recentMeUrlChatInvite) object;
                    TLRPC.ChatInvite invite = chatInvite.chat_invite;
                    if (invite.chat == null && (!invite.channel || invite.megagroup) || invite.chat != null && (!ChatObject.isChannel(invite.chat) || invite.chat.megagroup)) {
                        String hash = chatInvite.url;
                        int index = hash.indexOf('/');
                        if (index > 0) {
                            hash = hash.substring(index + 1);
                        }
                        showDialog(new JoinGroupAlert(getParentActivity(), invite, hash, DialogsActivity.this));
                        return;
                    } else {
                        if (invite.chat != null) {
                            dialog_id = -invite.chat.id;
                        } else {
                            return;
                        }
                    }
                } else if (object instanceof TLRPC.TL_recentMeUrlStickerSet) {
                    TLRPC.StickerSet stickerSet = ((TLRPC.TL_recentMeUrlStickerSet) object).set.set;
                    TLRPC.TL_inputStickerSetID set = new TLRPC.TL_inputStickerSetID();
                    set.id = stickerSet.id;
                    set.access_hash = stickerSet.access_hash;
                    showDialog(new StickersAlert(getParentActivity(), DialogsActivity.this, set, null, null));
                    return;
                } else if (object instanceof TLRPC.TL_recentMeUrlUnknown) {
                    return;
                } else {
                    return;
                }
            } else if (adapter == dialogsSearchAdapter) {
                Object obj = dialogsSearchAdapter.getItem(position);
                isGlobalSearch = dialogsSearchAdapter.isGlobalSearch(position);
                if (obj instanceof TLRPC.User) {
                    dialog_id = ((TLRPC.User) obj).id;
                    if (!onlySelect) {
                        dialogsSearchAdapter.putRecentSearch(dialog_id, (TLRPC.User) obj);
                    }
                } else if (obj instanceof TLRPC.Chat) {
                    if (((TLRPC.Chat) obj).id > 0) {
                        dialog_id = -((TLRPC.Chat) obj).id;
                    } else {
                        dialog_id = AndroidUtilities.makeBroadcastId(((TLRPC.Chat) obj).id);
                    }
                    if (!onlySelect) {
                        dialogsSearchAdapter.putRecentSearch(dialog_id, (TLRPC.Chat) obj);
                    }
                } else if (obj instanceof TLRPC.EncryptedChat) {
                    dialog_id = ((long) ((TLRPC.EncryptedChat) obj).id) << 32;
                    if (!onlySelect) {
                        dialogsSearchAdapter.putRecentSearch(dialog_id, (TLRPC.EncryptedChat) obj);
                    }
                } else if (obj instanceof MessageObject) {
                    MessageObject messageObject = (MessageObject) obj;
                    dialog_id = messageObject.getDialogId();
                    message_id = messageObject.getId();
                    dialogsSearchAdapter.addHashtagsFromMessage(dialogsSearchAdapter.getLastSearchString());
                } else if (obj instanceof String) {
                    actionBar.openSearchField((String) obj);
                }
            }

            if (dialog_id == 0) {
                return;
            }

            if (onlySelect) {
                if (dialogsAdapter.hasSelectedDialogs()) {
                    dialogsAdapter.addOrRemoveSelectedDialog(dialog_id, view);
                    updateSelectedCount();
                } else {
                    didSelectResult(dialog_id, true, false);
                }
            } else {
                Bundle args = new Bundle();
                int lower_part = (int) dialog_id;
                int high_id = (int) (dialog_id >> 32);
                if (lower_part != 0) {
                    if (high_id == 1) {
                        args.putInt("chat_id", lower_part);
                    } else {
                        if (lower_part > 0) {
                            args.putInt("user_id", lower_part);
                        } else if (lower_part < 0) {
                            if (message_id != 0) {
                                TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-lower_part);
                                if (chat != null && chat.migrated_to != null) {
                                    args.putInt("migrated_to", lower_part);
                                    lower_part = -chat.migrated_to.channel_id;
                                }
                            }
                            args.putInt("chat_id", -lower_part);
                        }
                    }
                } else {
                    args.putInt("enc_id", high_id);
                }
                if (message_id != 0) {
                    args.putInt("message_id", message_id);
                } else if (!isGlobalSearch) {
                    if (actionBar != null) {
                        actionBar.closeSearchField();
                    }
                }
                if (AndroidUtilities.isTablet()) {
                    if (openedDialogId == dialog_id && adapter != dialogsSearchAdapter) {
                        return;
                    }
                    if (dialogsAdapter != null) {
                        dialogsAdapter.setOpenedDialogId(openedDialogId = dialog_id);
                        updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
                    }
                }
                if (searchString != null) {
                    if (MessagesController.getInstance(currentAccount).checkCanOpenChat(args, DialogsActivity.this)) {
                        NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats);
                        presentFragment(new ChatActivity(args));
                    }
                } else {
                    if (MessagesController.getInstance(currentAccount).checkCanOpenChat(args, DialogsActivity.this)) {
                        presentFragment(new ChatActivity(args));
                    }
                }
            }
        });
        listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() {
            @Override
            public boolean onItemClick(View view, int position, float x, float y) {
                if (getParentActivity() == null) {
                    return false;
                }
                if (!AndroidUtilities.isTablet() && !onlySelect && view instanceof DialogCell) {
                    DialogCell cell = (DialogCell) view;
                    if (cell.isPointInsideAvatar(x, y)) {
                        long dialog_id = cell.getDialogId();
                        Bundle args = new Bundle();
                        int lower_part = (int) dialog_id;
                        int high_id = (int) (dialog_id >> 32);
                        int message_id = cell.getMessageId();
                        if (lower_part != 0) {
                            if (high_id == 1) {
                                args.putInt("chat_id", lower_part);
                            } else {
                                if (lower_part > 0) {
                                    args.putInt("user_id", lower_part);
                                } else if (lower_part < 0) {
                                    if (message_id != 0) {
                                        TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-lower_part);
                                        if (chat != null && chat.migrated_to != null) {
                                            args.putInt("migrated_to", lower_part);
                                            lower_part = -chat.migrated_to.channel_id;
                                        }
                                    }
                                    args.putInt("chat_id", -lower_part);
                                }
                            }
                        } else {
                            return false;
                        }

                        if (message_id != 0) {
                            args.putInt("message_id", message_id);
                        }
                        if (searchString != null) {
                            if (MessagesController.getInstance(currentAccount).checkCanOpenChat(args, DialogsActivity.this)) {
                                NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats);
                                presentFragmentAsPreview(new ChatActivity(args));
                            }
                        } else {
                            if (MessagesController.getInstance(currentAccount).checkCanOpenChat(args, DialogsActivity.this)) {
                                presentFragmentAsPreview(new ChatActivity(args));
                            }
                        }
                        return true;
                    }
                }
                RecyclerView.Adapter adapter = listView.getAdapter();
                if (adapter == dialogsSearchAdapter) {
                    Object item = dialogsSearchAdapter.getItem(position);
                    if (item instanceof String || dialogsSearchAdapter.isRecentSearchDisplayed()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                        builder.setMessage(LocaleController.getString("ClearSearch", R.string.ClearSearch));
                        builder.setPositiveButton(LocaleController.getString("ClearButton", R.string.ClearButton).toUpperCase(), (dialogInterface, i) -> {
                            if (dialogsSearchAdapter.isRecentSearchDisplayed()) {
                                dialogsSearchAdapter.clearRecentSearch();
                            } else {
                                dialogsSearchAdapter.clearRecentHashtags();
                            }
                        });
                        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        showDialog(builder.create());
                        return true;
                    }
                    return false;
                }
                final TLRPC.TL_dialog dialog;
                ArrayList<TLRPC.TL_dialog> dialogs = getDialogsArray();
                if (position < 0 || position >= dialogs.size()) {
                    return false;
                }
                dialog = dialogs.get(position);
                if (onlySelect) {
                    if (dialogsType != 3 || selectAlertString != null) {
                        return false;
                    }
                    dialogsAdapter.addOrRemoveSelectedDialog(dialog.id, view);
                    updateSelectedCount();
                } else {
                    selectedDialog = dialog.id;
                    final boolean pinned = dialog.pinned;

                    BottomSheet.Builder builder = new BottomSheet.Builder(getParentActivity());
                    int lower_id = (int) selectedDialog;
                    int high_id = (int) (selectedDialog >> 32);

                    final boolean hasUnread = dialog.unread_count != 0 || dialog.unread_mark;

                    if (DialogObject.isChannel(dialog)) {
                        final TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-lower_id);
                        CharSequence items[];
                        int icons[] = new int[]{
                                dialog.pinned ? R.drawable.chats_unpin : R.drawable.chats_pin,
                                R.drawable.chats_clear,
                                hasUnread ? R.drawable.menu_read : R.drawable.menu_unread,
                                R.drawable.chats_leave
                        };
                        if (MessagesController.getInstance(currentAccount).isProxyDialog(dialog.id)) {
                            items = new CharSequence[]{
                                    null,
                                    LocaleController.getString("ClearHistoryCache", R.string.ClearHistoryCache),
                                    hasUnread ? LocaleController.getString("MarkAsRead", R.string.MarkAsRead) : LocaleController.getString("MarkAsUnread", R.string.MarkAsUnread),
                                    null};
                        } else if (chat != null && chat.megagroup) {
                            items = new CharSequence[]{
                                    null,//dialog.pinned || MessagesController.getInstance(currentAccount).canPinDialog(false) ? (dialog.pinned ? LocaleController.getString("UnpinFromTop", R.string.UnpinFromTop) : LocaleController.getString("PinToTop", R.string.PinToTop)) : null,
                                    TextUtils.isEmpty(chat.username) ? LocaleController.getString("ClearHistory", R.string.ClearHistory) : LocaleController.getString("ClearHistoryCache", R.string.ClearHistoryCache),
                                    hasUnread ? LocaleController.getString("MarkAsRead", R.string.MarkAsRead) : LocaleController.getString("MarkAsUnread", R.string.MarkAsUnread),
                                    LocaleController.getString("LeaveMegaMenu", R.string.LeaveMegaMenu)};
                        } else {
                            items = new CharSequence[]{
                                    null, //dialog.pinned || MessagesController.getInstance(currentAccount).canPinDialog(false) ? (dialog.pinned ? LocaleController.getString("UnpinFromTop", R.string.UnpinFromTop) : LocaleController.getString("PinToTop", R.string.PinToTop)) : null,
                                    LocaleController.getString("ClearHistoryCache", R.string.ClearHistoryCache),
                                    hasUnread ? LocaleController.getString("MarkAsRead", R.string.MarkAsRead) : LocaleController.getString("MarkAsUnread", R.string.MarkAsUnread),
                                    LocaleController.getString("LeaveChannelMenu", R.string.LeaveChannelMenu)};
                        }
                        builder.setItems(items, icons, (d, which) -> {
                            if (which == 0) {
                                if (MessagesController.getInstance(currentAccount).pinDialog(selectedDialog, !pinned, null, 0) && !pinned) {
                                    hideFloatingButton(false);
                                    listView.smoothScrollToPosition(0);
                                }
                            } else if (which == 2) {
                                if (hasUnread) {
                                    MessagesController.getInstance(currentAccount).markMentionsAsRead(selectedDialog);
                                    MessagesController.getInstance(currentAccount).markDialogAsRead(selectedDialog, dialog.top_message, dialog.top_message, dialog.last_message_date, false, 0, true);
                                } else {
                                    MessagesController.getInstance(currentAccount).markDialogAsUnread(selectedDialog, null, 0);
                                }
                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getParentActivity());
                                builder1.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                if (which == 1) {
                                    if (chat != null && chat.megagroup) {
                                        if (TextUtils.isEmpty(chat.username)) {
                                            builder1.setMessage(LocaleController.getString("AreYouSureClearHistory", R.string.AreYouSureClearHistory));
                                        } else {
                                            builder1.setMessage(LocaleController.getString("AreYouSureClearHistoryGroup", R.string.AreYouSureClearHistoryGroup));
                                        }
                                    } else {
                                        builder1.setMessage(LocaleController.getString("AreYouSureClearHistoryChannel", R.string.AreYouSureClearHistoryChannel));
                                    }
                                    builder1.setPositiveButton(LocaleController.getString("OK", R.string.OK), (dialogInterface, i) -> {
                                        if (chat != null && chat.megagroup && TextUtils.isEmpty(chat.username)) {
                                            MessagesController.getInstance(currentAccount).deleteDialog(selectedDialog, 1);
                                        } else {
                                            MessagesController.getInstance(currentAccount).deleteDialog(selectedDialog, 2);
                                        }
                                    });
                                } else {
                                    if (chat != null && chat.megagroup) {
                                        builder1.setMessage(LocaleController.getString("MegaLeaveAlert", R.string.MegaLeaveAlert));
                                    } else {
                                        builder1.setMessage(LocaleController.getString("ChannelLeaveAlert", R.string.ChannelLeaveAlert));
                                    }
                                    builder1.setPositiveButton(LocaleController.getString("OK", R.string.OK), (dialogInterface, i) -> {
                                        MessagesController.getInstance(currentAccount).deleteUserFromChat((int) -selectedDialog, UserConfig.getInstance(currentAccount).getCurrentUser(), null);
                                        if (AndroidUtilities.isTablet()) {
                                            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats, selectedDialog);
                                        }
                                    });
                                }
                                builder1.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                showDialog(builder1.create());
                            }
                        });
                        showDialog(builder.create());
                    } else {
                        final boolean isChat = lower_id < 0 && high_id != 1;
                        TLRPC.User user = null;
                        if (!isChat && lower_id > 0 && high_id != 1) {
                            user = MessagesController.getInstance(currentAccount).getUser(lower_id);
                        }
                        final boolean isBot = user != null && user.bot;

                        builder.setItems(new CharSequence[]{
                                null,//dialog.pinned || MessagesController.getInstance(currentAccount).canPinDialog(lower_id == 0) ? (dialog.pinned ? LocaleController.getString("UnpinFromTop", R.string.UnpinFromTop) : LocaleController.getString("PinToTop", R.string.PinToTop)) : null,
                                LocaleController.getString("ClearHistory", R.string.ClearHistory),
                                hasUnread ? LocaleController.getString("MarkAsRead", R.string.MarkAsRead) : LocaleController.getString("MarkAsUnread", R.string.MarkAsUnread),
                                isChat ? LocaleController.getString("DeleteChat", R.string.DeleteChat) : isBot ? LocaleController.getString("DeleteAndStop", R.string.DeleteAndStop) : LocaleController.getString("Delete", R.string.Delete)
                        }, new int[]{
                                dialog.pinned ? R.drawable.chats_unpin : R.drawable.chats_pin,
                                R.drawable.chats_clear,
                                hasUnread ? R.drawable.menu_read : R.drawable.menu_unread,
                                isChat ? R.drawable.chats_leave : R.drawable.chats_delete
                        }, (d, which) -> {
                            if (which == 0) {
                                if (MessagesController.getInstance(currentAccount).pinDialog(selectedDialog, !pinned, null, 0) && !pinned) {
                                    hideFloatingButton(false);
                                    listView.smoothScrollToPosition(0);
                                }
                            } else if (which == 2) {
                                if (hasUnread) {
                                    MessagesController.getInstance(currentAccount).markMentionsAsRead(selectedDialog);
                                    MessagesController.getInstance(currentAccount).markDialogAsRead(selectedDialog, dialog.top_message, dialog.top_message, dialog.last_message_date, false, 0, true);
                                } else {
                                    MessagesController.getInstance(currentAccount).markDialogAsUnread(selectedDialog, null, 0);
                                }
                            } else {
                                AlertDialog.Builder builder12 = new AlertDialog.Builder(getParentActivity());
                                builder12.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                if (which == 1) {
                                    builder12.setMessage(LocaleController.getString("AreYouSureClearHistory", R.string.AreYouSureClearHistory));
                                } else {
                                    if (isChat) {
                                        builder12.setMessage(LocaleController.getString("AreYouSureDeleteAndExit", R.string.AreYouSureDeleteAndExit));
                                    } else {
                                        builder12.setMessage(LocaleController.getString("AreYouSureDeleteThisChat", R.string.AreYouSureDeleteThisChat));
                                    }
                                }
                                builder12.setPositiveButton(LocaleController.getString("OK", R.string.OK), (dialogInterface, i) -> {
                                    if (which != 1) {
                                        if (isChat) {
                                            TLRPC.Chat currentChat = MessagesController.getInstance(currentAccount).getChat((int) -selectedDialog);
                                            if (currentChat != null && ChatObject.isNotInChat(currentChat)) {
                                                MessagesController.getInstance(currentAccount).deleteDialog(selectedDialog, 0);
                                            } else {
                                                MessagesController.getInstance(currentAccount).deleteUserFromChat((int) -selectedDialog, MessagesController.getInstance(currentAccount).getUser(UserConfig.getInstance(currentAccount).getClientUserId()), null);
                                            }
                                        } else {
                                            MessagesController.getInstance(currentAccount).deleteDialog(selectedDialog, 0);
                                        }
                                        if (isBot) {
                                            MessagesController.getInstance(currentAccount).blockUser((int) selectedDialog);
                                        }
                                        if (AndroidUtilities.isTablet()) {
                                            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats, selectedDialog);
                                        }
                                    } else {
                                        MessagesController.getInstance(currentAccount).deleteDialog(selectedDialog, 1);
                                    }
                                });
                                builder12.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                showDialog(builder12.create());
                            }
                        });
                        showDialog(builder.create());
                    }
                }
                return true;
            }

            @Override
            public void onLongClickRelease() {
                finishPreviewFragment();
            }

            @Override
            public void onMove(float dx, float dy) {
                movePreviewFragment(dy);
            }
        });

        asymmetricGridView = new AsymmetricGridView(context);
        asymmetricGridView.setLayoutAnimation(null);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        profileView = (FrameLayout) inflater.inflate(R.layout.frg_profile, null);
        parentView.addView(profileView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER, 0, 0, 0, 0));

        /*
         * find profile Item
         * */
        tv_user_name = profileView.findViewById(R.id.tv_user_name);
        tv_user_phone = profileView.findViewById(R.id.tv_user_phone);
        tv_profile_balance = profileView.findViewById(R.id.tv_profile_balance);
        tv_profile_setting = profileView.findViewById(R.id.tv_profile_setting);
        tv_profile_score = profileView.findViewById(R.id.tv_profile_score);
        imv_user_pic = profileView.findViewById(R.id.imv_user_pic);
        rl_wallet = profileView.findViewById(R.id.rl_wallet);
        rl_scores = profileView.findViewById(R.id.rl_scores);
        ll_add_account = profileView.findViewById(R.id.ll_add_account);
        ll_save_message = profileView.findViewById(R.id.ll_save_message);
        ll_invite = profileView.findViewById(R.id.ll_invite);
        ll_top_bgr = profileView.findViewById(R.id.ll_top_bgr);
        tv_version = profileView.findViewById(R.id.tv_version);
        ll_more_setting = profileView.findViewById(R.id.ll_more_setting);
        ll_select_language = profileView.findViewById(R.id.ll_select_language);
        ll_theme = profileView.findViewById(R.id.ll_theme);
        ll_notification = profileView.findViewById(R.id.ll_notification);
        ll_secure = profileView.findViewById(R.id.ll_secure);
        tv_credit = profileView.findViewById(R.id.tv_credit);
        tv_profile_add = profileView.findViewById(R.id.tv_profile_add);
        tv_cloud_messege = profileView.findViewById(R.id.tv_cloud_messege);
        tv_invite_friend = profileView.findViewById(R.id.tv_invite_friend);
        tv_profile_notificationsandsounds = profileView.findViewById(R.id.tv_profile_notificationsandsounds);
        tv_profile_theme = profileView.findViewById(R.id.tv_profile_theme);
        tv_profile_language = profileView.findViewById(R.id.tv_profile_language);
        tv_privacy_security = profileView.findViewById(R.id.tv_privacy);
        tv_more_setting = profileView.findViewById(R.id.tv_more_setting);
        tv_app_version = profileView.findViewById(R.id.tv_app_version);
        tv_profile_update = profileView.findViewById(R.id.tv_profile_update);
        tv_profile_score_title = profileView.findViewById(R.id.tv_profile_score_title);
        // Handle Click and Set Data
        tv_credit.setText(LocaleController.getString("credit", R.string.credit));
        tv_profile_add.setText(LocaleController.getString("add_profile", R.string.add));
        tv_cloud_messege.setText(LocaleController.getString("cloud_msg", R.string.cloudmsg));
        tv_invite_friend.setText(LocaleController.getString("invited_friends", R.string.invitefriend));
        tv_profile_setting.setText(LocaleController.getString("settings", R.string.VoipOfflineOpenSettings));
        tv_profile_notificationsandsounds.setText(LocaleController.getString("notifandsound", R.string.NotificationsAndSounds));
        tv_profile_theme.setText(LocaleController.getString("themeApp", R.string.Theme));
        tv_profile_language.setText(LocaleController.getString("languageApp", R.string.Language));
        tv_privacy_security.setText(LocaleController.getString("PrivacySettingsApp", R.string.PrivacySettings));
        tv_more_setting.setText(LocaleController.getString("moreSetting", R.string.more_setting));
        tv_app_version.setText(LocaleController.getString("profileVersion", R.string.profile_version));
        tv_profile_update.setText(LocaleController.getString("profileUpdate", R.string.profile_update));
        tv_profile_score_title.setText(LocaleController.getString("scoreProfile", R.string.score));
        tv_version.setText(" " + BuildConfig.VERSION_NAME);
        setUser(MessagesController.getInstance(currentAccount).getUser(UserConfig.getInstance(currentAccount).getClientUserId()));
        rl_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new ScoreFragment(fragmentView.getContext().getString(R.string.total_rate)));
            }
        });

        rl_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new WalletFragment(fragmentView.getContext().getString(R.string.balance)));
            }
        });
        ll_add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new ContactsActivity(null));
            }
        });

        ll_save_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("user_id", UserConfig.getInstance(currentAccount).getClientUserId());
                presentFragment(new ChatActivity(args));
            }
        });
        ll_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new InviteContactsActivity());
            }
        });
        ll_more_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new SettingsActivity());
            }
        });

        ll_select_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new LanguageSelectActivity());
            }
        });
        ll_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new ThemeActivity(ThemeActivity.THEME_TYPE_BASIC));
            }
        });

        ll_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new NotificationsSettingsActivity());
            }
        });
        ll_secure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentFragment(new PrivacySettingsActivity());
            }
        });
        /*gridView = new RecyclerListView(context);
        gridView.setVerticalScrollBarEnabled(true);
        asymmetricGridView.setItemAnimator(null);
        asymmetricGridView.setInstantClick(true);

        gridView.setTag(555);
        layoutManagerForBot = new GridLayoutManager(context, 2);

//        layoutManager = new LinearLayoutManager(context) {
//            @Override
//            public boolean supportsPredictiveItemAnimations() {
//                return false;
//            }
//
//            @Override
//            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//                LinearSmoothScrollerMiddle linearSmoothScroller = new LinearSmoothScrollerMiddle(recyclerView.getContext());
//                linearSmoothScroller.setTargetPosition(position);
//                startSmoothScroll(linearSmoothScroller);
//            }
//        };*/
        /*layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridView.setLayoutManager(layoutManagerForBot);
        gridView.setVerticalScrollbarPosition(LocaleController.isRTL ? RecyclerListView.SCROLLBAR_POSITION_LEFT : RecyclerListView.SCROLLBAR_POSITION_RIGHT);*/
        parentView.addView(asymmetricGridView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
//        parentView.addView(gridView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        /*botListAdapater = new BotsListAdapter(getParentActivity(), mBots);

        gridView.setAdapter(botListAdapater);
        gridView.setPadding(10, 10, 10, 10);
        gridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mBots != null && mBots.size() > 0) {
                    String botName = mBots.get(position);
//                    parentLayout.isNeedsDispDialogs = true;
//                    parentLayout.addFragmentToStack(DialogsActivity.this, parentLayout.fragmentsStack.size());
//                    parentLayout.addFragmentToStack(DialogsActivity.this);
//                    MessagesController.getInstance(currentAccount).isFromBot = true;
                    MessagesController.getInstance(currentAccount).openByBotName(botName, DialogsActivity.this, 0);
                }

            }
        });
        botListAdapater.notifyDataSetChanged();*/

        searchEmptyView = new EmptyTextProgressView(context);
        searchEmptyView.setVisibility(View.GONE);
        searchEmptyView.setShowAtCenter(true);
        searchEmptyView.setText(LocaleController.getString("NoResult", R.string.NoResult));
        contentView.addView(searchEmptyView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        progressView = new RadialProgressView(context);
        progressView.setVisibility(View.GONE);
        contentView.addView(progressView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

        floatingButton = new ImageView(context);
        floatingButton.setVisibility(onlySelect ? View.GONE : View.VISIBLE);
        floatingButton.setScaleType(ImageView.ScaleType.CENTER);

        Drawable drawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
        if (Build.VERSION.SDK_INT < 21) {
            Drawable shadowDrawable = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
            shadowDrawable.setColorFilter(new PorterDuffColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(shadowDrawable, drawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56), AndroidUtilities.dp(56));
            drawable = combinedDrawable;
        }
        floatingButton.setBackgroundDrawable(drawable);
        floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
        floatingButton.setImageResource(R.drawable.floating_pencil);
        if (Build.VERSION.SDK_INT >= 21) {
            StateListAnimator animator = new StateListAnimator();
            animator.addState(new int[]{android.R.attr.state_pressed}, ObjectAnimator.ofFloat(floatingButton, "translationZ", AndroidUtilities.dp(2), AndroidUtilities.dp(4)).setDuration(200));
            animator.addState(new int[]{}, ObjectAnimator.ofFloat(floatingButton, "translationZ", AndroidUtilities.dp(4), AndroidUtilities.dp(2)).setDuration(200));
            floatingButton.setStateListAnimator(animator);
            floatingButton.setOutlineProvider(new ViewOutlineProvider() {
                @SuppressLint("NewApi")
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56), AndroidUtilities.dp(56));
                }
            });
        }
        contentView.addView(floatingButton, LayoutHelper.createFrame(Build.VERSION.SDK_INT >= 21 ? 56 : 60, Build.VERSION.SDK_INT >= 21 ? 56 : 60, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.BOTTOM, LocaleController.isRTL ? 14 : 0, 0, LocaleController.isRTL ? 0 : 14, 14 + 48));
        floatingButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putBoolean("destroyAfterSelect", true);
            presentFragment(new ContactsActivity(args));
        });

        unreadFloatingButtonContainer = new FrameLayout(context);
        if (onlySelect) {
            unreadFloatingButtonContainer.setVisibility(View.GONE);
        } else {
            unreadFloatingButtonContainer.setVisibility(currentUnreadCount != 0 ? View.VISIBLE : View.INVISIBLE);
            unreadFloatingButtonContainer.setTag(currentUnreadCount != 0 ? 1 : null);
        }
        contentView.addView(unreadFloatingButtonContainer, LayoutHelper.createFrame((Build.VERSION.SDK_INT >= 21 ? 56 : 60) + 20, (Build.VERSION.SDK_INT >= 21 ? 56 : 60) + 20, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.BOTTOM, LocaleController.isRTL ? 4 : 0, 0, LocaleController.isRTL ? 0 : 4, 14 + 60 + 7 + 48));
        unreadFloatingButtonContainer.setOnClickListener(view -> {
            if (listView.getAdapter() == dialogsAdapter) {
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem == 0) {
                    ArrayList<TLRPC.TL_dialog> array = getDialogsArray();
                    for (int a = array.size() - 1; a >= 0; a--) {
                        TLRPC.TL_dialog dialog = array.get(a);
                        if ((dialog.unread_count != 0 || dialog.unread_mark) && !MessagesController.getInstance(currentAccount).isDialogMuted(dialog.id)) {
                            listView.smoothScrollToPosition(a);
                            break;
                        }
                    }
                } else {
                    int middle = listView.getMeasuredHeight() / 2;
                    boolean found = false;
                    for (int b = 0, count = listView.getChildCount(); b < count; b++) {
                        View child = listView.getChildAt(b);
                        if (child instanceof DialogCell) {
                            if (child.getTop() <= middle && child.getBottom() >= middle) {
                                RecyclerListView.Holder holder = (RecyclerListView.Holder) listView.findContainingViewHolder(child);
                                if (holder != null) {
                                    ArrayList<TLRPC.TL_dialog> array = getDialogsArray();
                                    for (int a = Math.min(holder.getAdapterPosition(), array.size()) - 1; a >= 0; a--) {
                                        TLRPC.TL_dialog dialog = array.get(a);
                                        if ((dialog.unread_count != 0 || dialog.unread_mark) && !MessagesController.getInstance(currentAccount).isDialogMuted(dialog.id)) {
                                            found = true;
                                            listView.smoothScrollToPosition(a);
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (!found) {
                        hideFloatingButton(false);
                        listView.smoothScrollToPosition(0);
                    }
                }
            }
        });

        unreadFloatingButton = new ImageView(context);
        unreadFloatingButton.setScaleType(ImageView.ScaleType.CENTER);

        drawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56), Theme.getColor(Theme.key_chats_actionUnreadBackground), Theme.getColor(Theme.key_chats_actionUnreadPressedBackground));
        if (Build.VERSION.SDK_INT < 21) {
            Drawable shadowDrawable = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
            shadowDrawable.setColorFilter(new PorterDuffColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(shadowDrawable, drawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56), AndroidUtilities.dp(56));
            drawable = combinedDrawable;
        }
        unreadFloatingButton.setBackgroundDrawable(drawable);
        unreadFloatingButton.setImageDrawable(arrowDrawable = new AnimatedArrowDrawable(0xffffffff));
        unreadFloatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionUnreadIcon), PorterDuff.Mode.MULTIPLY));
        unreadFloatingButton.setPadding(0, AndroidUtilities.dp(4), 0, 0);
        arrowDrawable.setAnimationProgress(1.0f);
        if (Build.VERSION.SDK_INT >= 21) {
            StateListAnimator animator = new StateListAnimator();
            animator.addState(new int[]{android.R.attr.state_pressed}, ObjectAnimator.ofFloat(unreadFloatingButton, "translationZ", AndroidUtilities.dp(2), AndroidUtilities.dp(4)).setDuration(200));
            animator.addState(new int[]{}, ObjectAnimator.ofFloat(unreadFloatingButton, "translationZ", AndroidUtilities.dp(4), AndroidUtilities.dp(2)).setDuration(200));
            unreadFloatingButton.setStateListAnimator(animator);
            unreadFloatingButton.setOutlineProvider(new ViewOutlineProvider() {
                @SuppressLint("NewApi")
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56), AndroidUtilities.dp(56));
                }
            });
        }
        unreadFloatingButtonContainer.addView(unreadFloatingButton, LayoutHelper.createFrame(Build.VERSION.SDK_INT >= 21 ? 56 : 60, Build.VERSION.SDK_INT >= 21 ? 56 : 60, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.TOP, 10, 13, 10, 0));

        unreadFloatingButtonCounter = new TextView(context);
        unreadFloatingButtonCounter.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        unreadFloatingButtonCounter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        if (currentUnreadCount > 0) {
            unreadFloatingButtonCounter.setText(String.format("%d", currentUnreadCount));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            unreadFloatingButtonCounter.setElevation(AndroidUtilities.dp(5));
            unreadFloatingButtonCounter.setOutlineProvider(new ViewOutlineProvider() {
                @SuppressLint("NewApi")
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setEmpty();
                }
            });
        }
        unreadFloatingButtonCounter.setTextColor(Theme.getColor(Theme.key_chat_goDownButtonCounter));
        unreadFloatingButtonCounter.setGravity(Gravity.CENTER);
        unreadFloatingButtonCounter.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(11.5f), Theme.getColor(Theme.key_chat_goDownButtonCounterBackground)));
        unreadFloatingButtonCounter.setMinWidth(AndroidUtilities.dp(23));
        unreadFloatingButtonCounter.setPadding(AndroidUtilities.dp(8), 0, AndroidUtilities.dp(8), AndroidUtilities.dp(1));
        unreadFloatingButtonContainer.addView(unreadFloatingButtonCounter, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 23, Gravity.TOP | Gravity.CENTER_HORIZONTAL));

        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean scrollingManually;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (searching && searchWas) {
                        AndroidUtilities.hideKeyboard(getParentActivity().getCurrentFocus());
                    }
                    scrollingManually = true;
                } else {
                    scrollingManually = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = Math.abs(layoutManager.findLastVisibleItemPosition() - firstVisibleItem) + 1;
                int totalItemCount = recyclerView.getAdapter().getItemCount();

                if (searching && searchWas) {
                    if (visibleItemCount > 0 && layoutManager.findLastVisibleItemPosition() == totalItemCount - 1 && !dialogsSearchAdapter.isMessagesSearchEndReached()) {
                        dialogsSearchAdapter.loadMoreSearchMessages();
                    }
                    return;
                }
                if (visibleItemCount > 0) {
                    if (layoutManager.findLastVisibleItemPosition() >= getDialogsArray().size() - 10) {
                        boolean fromCache = !MessagesController.getInstance(currentAccount).dialogsEndReached;
                        if (fromCache || !MessagesController.getInstance(currentAccount).serverDialogsEndReached) {
                            MessagesController.getInstance(currentAccount).loadDialogs(-1, 100, fromCache);
                        }
                    }
                }

                checkUnreadButton(true);

                if (floatingButton.getVisibility() != View.GONE) {
                    final View topChild = recyclerView.getChildAt(0);
                    int firstViewTop = 0;
                    if (topChild != null) {
                        firstViewTop = topChild.getTop();
                    }
                    boolean goingDown;
                    boolean changed = true;
                    if (prevPosition == firstVisibleItem) {
                        final int topDelta = prevTop - firstViewTop;
                        goingDown = firstViewTop < prevTop;
                        changed = Math.abs(topDelta) > 1;
                    } else {
                        goingDown = firstVisibleItem > prevPosition;
                    }
                    if (changed && scrollUpdated && (goingDown || !goingDown && scrollingManually)) {
                        hideFloatingButton(goingDown);
                    }
                    prevPosition = firstVisibleItem;
                    prevTop = firstViewTop;
                    scrollUpdated = true;
                }
            }
        });

        if (searchString == null) {
//            dialogsAdapter = new DialogsAdapter(context, dialogsType, onlySelect);
//            if (AndroidUtilities.isTablet() && openedDialogId != 0) {
//                dialogsAdapter.setOpenedDialogId(openedDialogId);
//            }
//            listView.setAdapter(dialogsAdapter);
            listView.setAdapter(null);
            floatingButton.setVisibility(View.GONE);
            listView.setAdapter(null);
            setVisibleDialogList(VasniSchema.Companion.getInstance().getService());
        }
        int type = 0;
        if (searchString != null) {
            type = 2;
        } else if (!onlySelect) {
            type = 1;
        }
        dialogsSearchAdapter = new DialogsSearchAdapter(context, type, dialogsType);
        dialogsSearchAdapter.setDelegate(new DialogsSearchAdapter.DialogsSearchAdapterDelegate() {
            @Override
            public void searchStateChanged(boolean search) {
                if (searching && searchWas && searchEmptyView != null) {
                    if (search) {
                        searchEmptyView.showProgress();
                    } else {
                        searchEmptyView.showTextView();
                    }
                }
            }

            @Override
            public void didPressedOnSubDialog(long did) {
                if (onlySelect) {
                    if (dialogsAdapter.hasSelectedDialogs()) {
                        dialogsAdapter.addOrRemoveSelectedDialog(did, null);
                        updateSelectedCount();
                        actionBar.closeSearchField();
                    } else {
                        didSelectResult(did, true, false);
                    }
                } else {
                    int lower_id = (int) did;
                    Bundle args = new Bundle();
                    if (lower_id > 0) {
                        args.putInt("user_id", lower_id);
                    } else {
                        args.putInt("chat_id", -lower_id);
                    }
                    if (actionBar != null) {
                        actionBar.closeSearchField();
                    }
                    if (AndroidUtilities.isTablet()) {
                        if (dialogsAdapter != null) {
                            dialogsAdapter.setOpenedDialogId(openedDialogId = did);
                            updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
                        }
                    }
                    if (searchString != null) {
                        if (MessagesController.getInstance(currentAccount).checkCanOpenChat(args, DialogsActivity.this)) {
                            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats);
                            presentFragment(new ChatActivity(args));
                        }
                    } else {
                        if (MessagesController.getInstance(currentAccount).checkCanOpenChat(args, DialogsActivity.this)) {
                            presentFragment(new ChatActivity(args));
                        }
                    }
                }
            }

            @Override
            public void needRemoveHint(final int did) {
                if (getParentActivity() == null) {
                    return;
                }
                TLRPC.User user = MessagesController.getInstance(currentAccount).getUser(did);
                if (user == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setMessage(LocaleController.formatString("ChatHintsDelete", R.string.ChatHintsDelete, ContactsController.formatName(user.first_name, user.last_name)));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), (dialogInterface, i) -> DataQuery.getInstance(currentAccount).removePeer(did));
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                showDialog(builder.create());
            }
        });

        listView.setEmptyView(progressView);
        if (searchString != null) {
            actionBar.openSearchField(searchString);
        }

        if (!onlySelect && dialogsType == 0) {
            FragmentContextView fragmentLocationContextView = new FragmentContextView(context, this, true);
            contentView.addView(fragmentLocationContextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 39, Gravity.TOP | Gravity.LEFT, 0, -36, 0, 0));

            FragmentContextView fragmentContextView = new FragmentContextView(context, this, false);
            contentView.addView(fragmentContextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 39, Gravity.TOP | Gravity.LEFT, 0, -36, 0, 0));

            fragmentContextView.setAdditionalContextView(fragmentLocationContextView);
            fragmentLocationContextView.setAdditionalContextView(fragmentContextView);
        } else if (dialogsType == 3 && selectAlertString == null) {
            if (commentView != null) {
                commentView.onDestroy();
            }
            commentView = new ChatActivityEnterView(getParentActivity(), contentView, null, false);
            commentView.setAllowStickersAndGifs(false, false);
            commentView.setForceShowSendButton(true, false);
            commentView.setVisibility(View.GONE);
            contentView.addView(commentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM));
            commentView.setDelegate(new ChatActivityEnterView.ChatActivityEnterViewDelegate() {
                @Override
                public void onMessageSend(CharSequence message) {
                    if (delegate == null) {
                        return;
                    }
                    ArrayList<Long> selectedDialogs = dialogsAdapter.getSelectedDialogs();
                    if (selectedDialogs.isEmpty()) {
                        return;
                    }
                    delegate.didSelectDialogs(DialogsActivity.this, selectedDialogs, message, false);
                }

                @Override
                public void onSwitchRecordMode(boolean video) {

                }

                @Override
                public void onTextSelectionChanged(int start, int end) {

                }

                @Override
                public void onStickersExpandedChange() {

                }

                @Override
                public void onPreAudioVideoRecord() {

                }

                @Override
                public void onTextChanged(final CharSequence text, boolean bigChange) {

                }

                @Override
                public void onTextSpansChanged(CharSequence text) {

                }

                @Override
                public void needSendTyping() {

                }

                @Override
                public void onAttachButtonHidden() {

                }

                @Override
                public void onAttachButtonShow() {

                }

                @Override
                public void onMessageEditEnd(boolean loading) {

                }

                @Override
                public void onWindowSizeChanged(int size) {

                }

                @Override
                public void onStickersTab(boolean opened) {

                }

                @Override
                public void didPressedAttachButton() {

                }

                @Override
                public void needStartRecordVideo(int state) {

                }

                @Override
                public void needChangeVideoPreviewState(int state, float seekProgress) {

                }

                @Override
                public void needStartRecordAudio(int state) {

                }

                @Override
                public void needShowMediaBanHint() {

                }
            });
        }

        if (!onlySelect) {
            checkUnreadCount(false);
        }
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialogsAdapter != null) {
            dialogsAdapter.notifyDataSetChanged();
        }
        if (commentView != null) {
            commentView.onResume();
        }
        if (dialogsSearchAdapter != null) {
            dialogsSearchAdapter.notifyDataSetChanged();
        }
        if (checkPermission && !onlySelect && Build.VERSION.SDK_INT >= 23) {
            Activity activity = getParentActivity();
            if (activity != null) {
                checkPermission = false;
                if (activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (UserConfig.getInstance(currentAccount).syncContacts && activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                        AlertDialog.Builder builder = AlertsCreator.createContactsPermissionDialog(activity, param -> {
                            askAboutContacts = param != 0;
                            MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", askAboutContacts).commit();
                            askForPermissons(false);
                        });
                        showDialog(permissionDialog = builder.create());
                    } else if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                        builder.setMessage(LocaleController.getString("PermissionStorage", R.string.PermissionStorage));
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                        showDialog(permissionDialog = builder.create());
                    } else {
                        askForPermissons(true);
                    }
                }
            }
        }

        if (scrollSlidingTextTabStrip.getCurrentTabId() == 0) {
            setVisibleDialogList(VasniSchema.Companion.getInstance().getService());
            showUnreadFloatingButton(false, false);
        } else if (scrollSlidingTextTabStrip.getCurrentTabId() == 1) {
            setVisibleDialogList(VasniSchema.Companion.getInstance().getChat());
            showUnreadFloatingButton(NotificationsController.getInstance(currentAccount).getTotalUnreadCount() > 0, true);
        } else if (scrollSlidingTextTabStrip.getCurrentTabId() == 2) {
            setVisibleDialogList(VasniSchema.Companion.getInstance().getProfile());
            showUnreadFloatingButton(false, false);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (commentView != null) {
            commentView.onResume();
        }
    }

    public void setVisibleDialogList(String type) {
        if (listView == null || asymmetricGridView == null || profileView == null)
            return;
        if (type.equals(VasniSchema.Companion.getInstance().getChat())) {
            searchItem.setVisibility(View.VISIBLE);
            floatingButton.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            asymmetricGridView.setVisibility(View.GONE);
            profileView.setVisibility(View.GONE);
        } else if (type.equals(VasniSchema.Companion.getInstance().getService())) {
            searchItem.setVisibility(View.GONE);
            floatingButton.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            asymmetricGridView.setVisibility(View.VISIBLE);
            profileView.setVisibility(View.GONE);
        } else if (type.equals(VasniSchema.Companion.getInstance().getProfile())) {
            searchItem.setVisibility(View.GONE);
            floatingButton.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            asymmetricGridView.setVisibility(View.GONE);
            profileView.setVisibility(View.VISIBLE);
        }
    }

    private void checkUnreadCount(boolean animated) {
        if (!BuildVars.DEBUG_PRIVATE_VERSION) {
            return;
        }

        if (scrollSlidingTextTabStrip != null &&
                scrollSlidingTextTabStrip.getCurrentPosition() == 0) {
            return;
        }

        int newCount = MessagesController.getInstance(currentAccount).unreadUnmutedDialogs;
        if (newCount != currentUnreadCount) {
            currentUnreadCount = newCount;
            if (unreadFloatingButtonContainer != null) {
                if (currentUnreadCount > 0) {
                    unreadFloatingButtonCounter.setText(String.format("%d", currentUnreadCount));
                }
                checkUnreadButton(animated);
            }
        }
    }

    private void checkUnreadButton(boolean animated) {
        if (scrollSlidingTextTabStrip.getCurrentTabId() == 0) {
            return;
        }
        if (!onlySelect && listView.getAdapter() == dialogsAdapter) {
            boolean found = false;
            if (currentUnreadCount > 0) {
                int middle = listView.getMeasuredHeight() / 2;
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                int count = listView.getChildCount();
                int unreadOnScreen = 0;
                for (int b = 0; b < count; b++) {
                    View child = listView.getChildAt(b);
                    if (child instanceof DialogCell) {
                        if (((DialogCell) child).isUnread()) {
                            unreadOnScreen++;
                        }
                    }
                }
                for (int b = 0; b < count; b++) {
                    View child = listView.getChildAt(b);
                    if (child instanceof DialogCell) {
                        if (child.getTop() <= middle && child.getBottom() >= middle) {
                            RecyclerListView.Holder holder = (RecyclerListView.Holder) listView.findContainingViewHolder(child);
                            if (holder != null) {
                                ArrayList<TLRPC.TL_dialog> array = getDialogsArray();
                                if (firstVisibleItem == 0) {
                                    if (unreadOnScreen != currentUnreadCount) {
                                        for (int a = holder.getAdapterPosition() + 1, size = array.size(); a < size; a++) {
                                            TLRPC.TL_dialog dialog = array.get(a);
                                            if ((dialog.unread_count != 0 || dialog.unread_mark) && !MessagesController.getInstance(currentAccount).isDialogMuted(dialog.id)) {
                                                arrowDrawable.setAnimationProgressAnimated(1.0f);
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    found = true;
                                    arrowDrawable.setAnimationProgressAnimated(0.0f);
                                }
                            }
                            break;
                        }
                    }
                }
            }
            if (found) {
                if (unreadFloatingButtonContainer.getTag() == null) {
                    unreadFloatingButtonContainer.setTag(1);
                    unreadFloatingButtonContainer.setVisibility(View.VISIBLE);
                    if (animated) {
                        unreadFloatingButtonContainer.animate().alpha(1.0f).setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(null).start();
                    } else {
                        unreadFloatingButtonContainer.setAlpha(1.0f);
                    }
                }
            } else {
                if (unreadFloatingButtonContainer.getTag() != null) {
                    unreadFloatingButtonContainer.setTag(null);
                    if (animated) {
                        unreadFloatingButtonContainer.animate().alpha(0.0f).setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                unreadFloatingButtonContainer.setVisibility(View.INVISIBLE);
                            }
                        }).start();
                    } else {
                        unreadFloatingButtonContainer.setAlpha(0.0f);
                        unreadFloatingButtonContainer.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    private void showUnreadFloatingButton(boolean isShow, boolean isAnimated) {
        if (isShow) {
            if (unreadFloatingButtonContainer.getTag() == null) {
                unreadFloatingButtonContainer.setTag(1);
                unreadFloatingButtonContainer.setVisibility(View.VISIBLE);
                if (isAnimated) {
                    unreadFloatingButtonContainer.animate().alpha(1.0f).setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(null).start();
                } else {
                    unreadFloatingButtonContainer.setAlpha(1.0f);
                }
            }
        } else {
//            if (unreadFloatingButtonContainer.getTag() != null) {
            unreadFloatingButtonContainer.setTag(null);
            if (isAnimated) {
                unreadFloatingButtonContainer.animate().alpha(0.0f).setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        unreadFloatingButtonContainer.setVisibility(View.INVISIBLE);
                    }
                }).start();
            } else {
                unreadFloatingButtonContainer.setAlpha(0.0f);
                unreadFloatingButtonContainer.setVisibility(View.INVISIBLE);
            }
//            }
        }
    }

    private void updateProxyButton(boolean animated) {
        if (proxyDrawable == null) {
            return;
        }
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
        String proxyAddress = preferences.getString("proxy_ip", "");
        boolean proxyEnabled;
        if ((proxyEnabled = preferences.getBoolean("proxy_enabled", false) && !TextUtils.isEmpty(proxyAddress)) || MessagesController.getInstance(currentAccount).blockedCountry && !SharedConfig.proxyList.isEmpty()) {
            if (!actionBar.isSearchFieldVisible()) {
                proxyItem.setVisibility(View.VISIBLE);
            }
            proxyDrawable.setConnected(proxyEnabled, currentConnectionState == ConnectionsManager.ConnectionStateConnected || currentConnectionState == ConnectionsManager.ConnectionStateUpdating, animated);
            proxyItemVisisble = true;
        } else {
            proxyItem.setVisibility(View.GONE);
            proxyItemVisisble = false;
        }
    }

    private void updateSelectedCount() {
        if (commentView == null) {
            return;
        }
        if (!dialogsAdapter.hasSelectedDialogs()) {
            if (dialogsType == 3 && selectAlertString == null) {
                actionBar.setTitle(LocaleController.getString("ForwardTo", R.string.ForwardTo));
            } else {
                actionBar.setTitle(LocaleController.getString("SelectChat", R.string.SelectChat));
            }
            if (commentView.getTag() != null) {
                commentView.hidePopup(false);
                commentView.closeKeyboard();
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(commentView, "translationY", 0, commentView.getMeasuredHeight()));
                animatorSet.setDuration(180);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        commentView.setVisibility(View.GONE);
                    }
                });
                animatorSet.start();
                commentView.setTag(null);
                listView.requestLayout();
            }
        } else {
            if (commentView.getTag() == null) {
                commentView.setFieldText("");
                commentView.setVisibility(View.VISIBLE);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(commentView, "translationY", commentView.getMeasuredHeight(), 0));
                animatorSet.setDuration(180);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        commentView.setTag(2);
                    }
                });
                animatorSet.start();
                commentView.setTag(1);
            }
            actionBar.setTitle(LocaleController.formatPluralString("Recipient", dialogsAdapter.getSelectedDialogs().size()));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForPermissons(boolean alert) {
        Activity activity = getParentActivity();
        if (activity == null) {
            return;
        }
        ArrayList<String> permissons = new ArrayList<>();
        if (UserConfig.getInstance(currentAccount).syncContacts && askAboutContacts && activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (alert) {
                AlertDialog.Builder builder = AlertsCreator.createContactsPermissionDialog(activity, param -> {
                    askAboutContacts = param != 0;
                    MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", askAboutContacts).commit();
                    askForPermissons(false);
                });
                showDialog(permissionDialog = builder.create());
                return;
            }
            permissons.add(Manifest.permission.READ_CONTACTS);
            permissons.add(Manifest.permission.WRITE_CONTACTS);
            permissons.add(Manifest.permission.GET_ACCOUNTS);
        }
        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissons.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissons.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissons.isEmpty()) {
            return;
        }
        String[] items = permissons.toArray(new String[permissons.size()]);
        try {
            activity.requestPermissions(items, 1);
        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onDialogDismiss(Dialog dialog) {
        super.onDialogDismiss(dialog);
        if (permissionDialog != null && dialog == permissionDialog && getParentActivity() != null) {
            askForPermissons(false);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!onlySelect && floatingButton != null) {
            floatingButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    floatingButton.setTranslationY(floatingHidden ? AndroidUtilities.dp(100) : 0);
                    unreadFloatingButtonContainer.setTranslationY(floatingHidden ? AndroidUtilities.dp(74) : 0);
                    floatingButton.setClickable(!floatingHidden);
                    if (floatingButton != null) {
                        floatingButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int a = 0; a < permissions.length; a++) {
                if (grantResults.length <= a) {
                    continue;
                }
                switch (permissions[a]) {
                    case Manifest.permission.READ_CONTACTS:
                        if (grantResults[a] == PackageManager.PERMISSION_GRANTED) {
                            ContactsController.getInstance(currentAccount).forceImportContacts();
                        } else {
                            MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", askAboutContacts = false).commit();
                        }
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (grantResults[a] == PackageManager.PERMISSION_GRANTED) {
                            ImageLoader.getInstance().checkMediaPaths();
                        }
                        break;
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.dialogsNeedReload) {
            checkUnreadCount(true);
            if (dialogsAdapter != null) {
                if (dialogsAdapter.isDataSetChanged() || args.length > 0) {
                    dialogsAdapter.notifyDataSetChanged();
                } else {
                    updateVisibleRows(MessagesController.UPDATE_MASK_NEW_MESSAGE);
                }
            }
            if (listView != null) {
                try {
                    if (listView.getAdapter() == dialogsAdapter) {
                        searchEmptyView.setVisibility(View.GONE);
                        listView.setEmptyView(progressView);
                    } else {
                        if (searching && searchWas) {
                            listView.setEmptyView(searchEmptyView);
                        } else {
                            searchEmptyView.setVisibility(View.GONE);
                            listView.setEmptyView(null);
                        }
                        progressView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            if (scrollSlidingTextTabStrip != null)
                scrollSlidingTextTabStrip.checkUnreadCountAndShow(currentAccount);
        } else if (id == NotificationCenter.emojiDidLoaded) {
            updateVisibleRows(0);
        } else if (id == NotificationCenter.closeSearchByActiveAction) {
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
        } else if (id == NotificationCenter.proxySettingsChanged) {
            updateProxyButton(false);
        } else if (id == NotificationCenter.updateInterfaces) {
            Integer mask = (Integer) args[0];
            updateVisibleRows(mask);
            if ((mask & MessagesController.UPDATE_MASK_NEW_MESSAGE) != 0 || (mask & MessagesController.UPDATE_MASK_READ_DIALOG_MESSAGE) != 0) {
                checkUnreadCount(true);
            }
            if (scrollSlidingTextTabStrip != null)
                scrollSlidingTextTabStrip.checkUnreadCountAndShow(currentAccount);
        } else if (id == NotificationCenter.appDidLogout) {
            dialogsLoaded[currentAccount] = false;
        } else if (id == NotificationCenter.encryptedChatUpdated) {
            updateVisibleRows(0);
        } else if (id == NotificationCenter.contactsDidLoaded) {
            if (dialogsType == 0 && MessagesController.getInstance(currentAccount).dialogs.isEmpty()) {
                if (dialogsAdapter != null) {
                    dialogsAdapter.notifyDataSetChanged();
                }
            } else {
                updateVisibleRows(0);
            }
        } else if (id == NotificationCenter.openedChatChanged) {
            if (dialogsType == 0 && AndroidUtilities.isTablet()) {
                boolean close = (Boolean) args[1];
                long dialog_id = (Long) args[0];
                if (close) {
                    if (dialog_id == openedDialogId) {
                        openedDialogId = 0;
                    }
                } else {
                    openedDialogId = dialog_id;
                }
                if (dialogsAdapter != null) {
                    dialogsAdapter.setOpenedDialogId(openedDialogId);
                }
                updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
            }
        } else if (id == NotificationCenter.notificationsSettingsUpdated) {
            updateVisibleRows(0);
        } else if (id == NotificationCenter.messageReceivedByAck || id == NotificationCenter.messageReceivedByServer || id == NotificationCenter.messageSendError) {
            updateVisibleRows(MessagesController.UPDATE_MASK_SEND_STATE);
        } else if (id == NotificationCenter.didSetPasscode) {
            updatePasscodeButton();
        } else if (id == NotificationCenter.needReloadRecentDialogsSearch) {
            if (dialogsSearchAdapter != null) {
                dialogsSearchAdapter.loadRecentSearch();
            }
        } else if (id == NotificationCenter.didLoadedReplyMessages) {
            updateVisibleRows(MessagesController.UPDATE_MASK_MESSAGE_TEXT);
        } else if (id == NotificationCenter.reloadHints) {
            if (dialogsSearchAdapter != null) {
                dialogsSearchAdapter.notifyDataSetChanged();
            }
        } else if (id == NotificationCenter.didUpdatedConnectionState) {
            int state = ConnectionsManager.getInstance(account).getConnectionState();
            if (currentConnectionState != state) {
                currentConnectionState = state;
                updateProxyButton(true);
            }
        } else if (id == NotificationCenter.dialogsUnreadCounterChanged) {
            if (scrollSlidingTextTabStrip != null)
                scrollSlidingTextTabStrip.checkUnreadCountAndShow(currentAccount);
            /*if (!onlySelect) {
                int count = (Integer) args[0];
                currentUnreadCount = count;
                if (count != 0) {
                    unreadFloatingButtonCounter.setText(String.format("%d", count));
                    unreadFloatingButtonContainer.setVisibility(View.VISIBLE);
                    unreadFloatingButtonContainer.setTag(1);
                    unreadFloatingButtonContainer.animate().alpha(1.0f).setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(null).start();
                } else {
                    unreadFloatingButtonContainer.setTag(null);
                    unreadFloatingButtonContainer.animate().alpha(0.0f).setDuration(200).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            unreadFloatingButtonContainer.setVisibility(View.INVISIBLE);
                        }
                    }).start();
                }
            }*/
        }
    }

    private ArrayList<TLRPC.TL_dialog> getDialogsArray() {
        if (dialogsType == 0) {
            return MessagesController.getInstance(currentAccount).dialogs;
        } else if (dialogsType == 1) {
            return MessagesController.getInstance(currentAccount).dialogsServerOnly;
        } else if (dialogsType == 2) {
            return MessagesController.getInstance(currentAccount).dialogsGroupsOnly;
        } else if (dialogsType == 3) {
            return MessagesController.getInstance(currentAccount).dialogsForward;
        }
        return null;
    }

    public void setSideMenu(RecyclerView recyclerView) {
        sideMenu = recyclerView;
        sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
        sideMenu.setGlowColor(Theme.getColor(Theme.key_chats_menuBackground));
    }

    private void updatePasscodeButton() {
        if (passcodeItem == null) {
            return;
        }
        if (SharedConfig.passcodeHash.length() != 0 && !searching) {
            passcodeItem.setVisibility(View.VISIBLE);
            if (SharedConfig.appLocked) {
                passcodeItem.setIcon(R.drawable.lock_close);
            } else {
                passcodeItem.setIcon(R.drawable.lock_open);
            }
        } else {
            passcodeItem.setVisibility(View.GONE);
        }
    }

    private void hideFloatingButton(boolean hide) {
        if (floatingHidden == hide) {
            return;
        }
        floatingHidden = hide;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(floatingButton, "translationY", floatingHidden ? AndroidUtilities.dp(100) : 0),
                ObjectAnimator.ofFloat(unreadFloatingButtonContainer, "translationY", floatingHidden ? AndroidUtilities.dp(74) : 0));
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(floatingInterpolator);
        floatingButton.setClickable(!hide);
        animatorSet.start();
    }

    private void updateVisibleRows(int mask) {
        if (listView == null) {
            return;
        }
        int count = listView.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = listView.getChildAt(a);
            if (child instanceof DialogCell) {
                if (listView.getAdapter() != dialogsSearchAdapter) {
                    DialogCell cell = (DialogCell) child;
                    if ((mask & MessagesController.UPDATE_MASK_NEW_MESSAGE) != 0) {
                        cell.checkCurrentDialogIndex();
                        if (dialogsType == 0 && AndroidUtilities.isTablet()) {
                            cell.setDialogSelected(cell.getDialogId() == openedDialogId);
                        }
                    } else if ((mask & MessagesController.UPDATE_MASK_SELECT_DIALOG) != 0) {
                        if (dialogsType == 0 && AndroidUtilities.isTablet()) {
                            cell.setDialogSelected(cell.getDialogId() == openedDialogId);
                        }
                    } else {
                        cell.update(mask);
                    }
                }
            } else if (child instanceof UserCell) {
                ((UserCell) child).update(mask);
            } else if (child instanceof ProfileSearchCell) {
                ((ProfileSearchCell) child).update(mask);
            } else if (child instanceof RecyclerListView) {
                RecyclerListView innerListView = (RecyclerListView) child;
                int count2 = innerListView.getChildCount();
                for (int b = 0; b < count2; b++) {
                    View child2 = innerListView.getChildAt(b);
                    if (child2 instanceof HintDialogCell) {
                        ((HintDialogCell) child2).checkUnreadCounter(mask);
                    }
                }
            }
        }
    }

    public void setDelegate(DialogsActivityDelegate dialogsActivityDelegate) {
        delegate = dialogsActivityDelegate;
    }

    public void setSearchString(String string) {
        searchString = string;
    }

    public boolean isMainDialogList() {
        return delegate == null && searchString == null;
    }

    private void didSelectResult(final long dialog_id, boolean useAlert, final boolean param) {
        if (addToGroupAlertString == null) {
            if ((int) dialog_id < 0) {
                TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-(int) dialog_id);
                if (ChatObject.isChannel(chat) && !chat.megagroup && (cantSendToChannels || !ChatObject.isCanWriteToChannel(-(int) dialog_id, currentAccount))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    builder.setMessage(LocaleController.getString("ChannelCantSendMessage", R.string.ChannelCantSendMessage));
                    builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
                    showDialog(builder.create());
                    return;
                }
            }
        }
        if (useAlert && (selectAlertString != null && selectAlertStringGroup != null || addToGroupAlertString != null)) {
            if (getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            int lower_part = (int) dialog_id;
            int high_id = (int) (dialog_id >> 32);
            if (lower_part != 0) {
                if (high_id == 1) {
                    TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(lower_part);
                    if (chat == null) {
                        return;
                    }
                    builder.setMessage(LocaleController.formatStringSimple(selectAlertStringGroup, chat.title));
                } else {
                    if (lower_part == UserConfig.getInstance(currentAccount).getClientUserId()) {
                        builder.setMessage(LocaleController.formatStringSimple(selectAlertStringGroup, LocaleController.getString("SavedMessages", R.string.SavedMessages)));
                    } else if (lower_part > 0) {
                        TLRPC.User user = MessagesController.getInstance(currentAccount).getUser(lower_part);
                        if (user == null) {
                            return;
                        }
                        builder.setMessage(LocaleController.formatStringSimple(selectAlertString, UserObject.getUserName(user)));
                    } else if (lower_part < 0) {
                        TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-lower_part);
                        if (chat == null) {
                            return;
                        }
                        if (addToGroupAlertString != null) {
                            builder.setMessage(LocaleController.formatStringSimple(addToGroupAlertString, chat.title));
                        } else {
                            builder.setMessage(LocaleController.formatStringSimple(selectAlertStringGroup, chat.title));
                        }
                    }
                }
            } else {
                TLRPC.EncryptedChat chat = MessagesController.getInstance(currentAccount).getEncryptedChat(high_id);
                TLRPC.User user = MessagesController.getInstance(currentAccount).getUser(chat.user_id);
                if (user == null) {
                    return;
                }
                builder.setMessage(LocaleController.formatStringSimple(selectAlertString, UserObject.getUserName(user)));
            }

            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), (dialogInterface, i) -> didSelectResult(dialog_id, false, false));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder.create());
        } else {
            if (delegate != null) {
                ArrayList<Long> dids = new ArrayList<>();
                dids.add(dialog_id);
                delegate.didSelectDialogs(DialogsActivity.this, dids, null, param);
                delegate = null;
            } else {
                finishFragment();
            }
        }
    }

    @Override
    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate cellDelegate = () -> {
            if (listView != null) {
                int count = listView.getChildCount();
                for (int a = 0; a < count; a++) {
                    View child = listView.getChildAt(a);
                    if (child instanceof ProfileSearchCell) {
                        ((ProfileSearchCell) child).update(0);
                    } else if (child instanceof DialogCell) {
                        ((DialogCell) child).update(0);
                    }
                }
            }
            if (dialogsSearchAdapter != null) {
                RecyclerListView recyclerListView = dialogsSearchAdapter.getInnerListView();
                if (recyclerListView != null) {
                    int count = recyclerListView.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = recyclerListView.getChildAt(a);
                        if (child instanceof HintDialogCell) {
                            ((HintDialogCell) child).update();
                        }
                    }
                }
            }
        };
        return new ThemeDescription[]{
                new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite),

                new ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault),
                new ThemeDescription(listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault),
                new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon),
                new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle),
                new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector),
                new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch),
                new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder),

                new ThemeDescription(listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector),

                new ThemeDescription(listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider),

                new ThemeDescription(searchEmptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder),
                new ThemeDescription(searchEmptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle),

                new ThemeDescription(listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DialogsEmptyCell.class}, new String[]{"emptyTextView1"}, null, null, null, Theme.key_emptyListPlaceholder),
                new ThemeDescription(listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DialogsEmptyCell.class}, new String[]{"emptyTextView2"}, null, null, null, Theme.key_emptyListPlaceholder),

                new ThemeDescription(floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionIcon),
                new ThemeDescription(floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionBackground),
                new ThemeDescription(floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chats_actionPressedBackground),

                new ThemeDescription(unreadFloatingButtonCounter, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_goDownButtonCounterBackground),
                new ThemeDescription(unreadFloatingButtonCounter, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_goDownButtonCounter),
                new ThemeDescription(unreadFloatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionUnreadIcon),
                new ThemeDescription(unreadFloatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionUnreadBackground),
                new ThemeDescription(unreadFloatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chats_actionUnreadPressedBackground),

                new ThemeDescription(listView, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.avatar_photoDrawable, Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_avatar_backgroundRed),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_avatar_backgroundOrange),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_avatar_backgroundViolet),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_avatar_backgroundGreen),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_avatar_backgroundCyan),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_avatar_backgroundBlue),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_avatar_backgroundPink),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_avatar_backgroundSaved),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_countPaint, null, null, Theme.key_chats_unreadCounter),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_countGrayPaint, null, null, Theme.key_chats_unreadCounterMuted),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_countTextPaint, null, null, Theme.key_chats_unreadCounterText),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, Theme.dialogs_namePaint, null, null, Theme.key_chats_name),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, Theme.dialogs_nameEncryptedPaint, null, null, Theme.key_chats_secretName),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_lockDrawable}, null, Theme.key_chats_secretIcon),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_groupDrawable, Theme.dialogs_broadcastDrawable, Theme.dialogs_botDrawable}, null, Theme.key_chats_nameIcon),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_pinnedDrawable}, null, Theme.key_chats_pinnedIcon),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_messagePaint, null, null, Theme.key_chats_message),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_chats_nameMessage),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_chats_draft),
                new ThemeDescription(null, 0, null, null, null, cellDelegate, Theme.key_chats_attachMessage),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_messagePrintingPaint, null, null, Theme.key_chats_actionMessage),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_timePaint, null, null, Theme.key_chats_date),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_pinnedPaint, null, null, Theme.key_chats_pinnedOverlay),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_tabletSeletedPaint, null, null, Theme.key_chats_tabletSelectedOverlay),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_checkDrawable, Theme.dialogs_halfCheckDrawable}, null, Theme.key_chats_sentCheck),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_clockDrawable}, null, Theme.key_chats_sentClock),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, Theme.dialogs_errorPaint, null, null, Theme.key_chats_sentError),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_errorDrawable}, null, Theme.key_chats_sentErrorIcon),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedCheckDrawable}, null, Theme.key_chats_verifiedCheck),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedDrawable}, null, Theme.key_chats_verifiedBackground),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_muteDrawable}, null, Theme.key_chats_muteIcon),
                new ThemeDescription(listView, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_mentionDrawable}, null, Theme.key_chats_mentionIcon),

                new ThemeDescription(sideMenu, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_chats_menuBackground),
                new ThemeDescription(sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuName),
                new ThemeDescription(sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuPhone),
                new ThemeDescription(sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuPhoneCats),
                new ThemeDescription(sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuCloudBackgroundCats),
                new ThemeDescription(sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chat_serviceBackground),
                new ThemeDescription(sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_chats_menuTopShadow),
                new ThemeDescription(sideMenu, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{DrawerProfileCell.class}, null, null, null, Theme.key_avatar_backgroundActionBarBlue),

                new ThemeDescription(sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerActionCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chats_menuItemIcon),
                new ThemeDescription(sideMenu, 0, new Class[]{DrawerActionCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chats_menuItemText),

                new ThemeDescription(sideMenu, 0, new Class[]{DrawerUserCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chats_menuItemText),
                new ThemeDescription(sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_chats_unreadCounterText),
                new ThemeDescription(sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_chats_unreadCounter),
                new ThemeDescription(sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_chats_menuBackground),
                new ThemeDescription(sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerAddCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chats_menuItemIcon),
                new ThemeDescription(sideMenu, 0, new Class[]{DrawerAddCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chats_menuItemText),

                new ThemeDescription(sideMenu, 0, new Class[]{DividerCell.class}, Theme.dividerPaint, null, null, Theme.key_divider),

                new ThemeDescription(listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, null, null, null, Theme.key_progressCircle),

                new ThemeDescription(listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_offlinePaint, null, null, Theme.key_windowBackgroundWhiteGrayText3),
                new ThemeDescription(listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_onlinePaint, null, null, Theme.key_windowBackgroundWhiteBlueText3),

                new ThemeDescription(listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, null, null, null, Theme.key_graySectionText),
                new ThemeDescription(listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, Theme.key_graySection),

                new ThemeDescription(listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{HashtagSearchCell.class}, null, null, null, Theme.key_windowBackgroundWhiteBlackText),

                new ThemeDescription(progressView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle),

                new ThemeDescription(dialogsSearchAdapter != null ? dialogsSearchAdapter.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countPaint, null, null, Theme.key_chats_unreadCounter),
                new ThemeDescription(dialogsSearchAdapter != null ? dialogsSearchAdapter.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countGrayPaint, null, null, Theme.key_chats_unreadCounterMuted),
                new ThemeDescription(dialogsSearchAdapter != null ? dialogsSearchAdapter.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countTextPaint, null, null, Theme.key_chats_unreadCounterText),
                new ThemeDescription(dialogsSearchAdapter != null ? dialogsSearchAdapter.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText),

                new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, null, null, null, Theme.key_inappPlayerBackground),
                new ThemeDescription(fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"playButton"}, null, null, null, Theme.key_inappPlayerPlayPause),
                new ThemeDescription(fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, null, null, null, Theme.key_inappPlayerTitle),
                new ThemeDescription(fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_FASTSCROLL, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, null, null, null, Theme.key_inappPlayerPerformer),
                new ThemeDescription(fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"closeButton"}, null, null, null, Theme.key_inappPlayerClose),

                new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, null, null, null, Theme.key_returnToCallBackground),
                new ThemeDescription(fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, null, null, null, Theme.key_returnToCallText),

                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogBackground),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogBackgroundGray),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlack),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextLink),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogLinkSelection),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlue),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlue2),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlue3),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextBlue4),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextRed),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextGray),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextGray2),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextGray3),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextGray4),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogIcon),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogTextHint),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogInputField),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogInputFieldActivated),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogCheckboxSquareBackground),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogCheckboxSquareCheck),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogCheckboxSquareUnchecked),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogCheckboxSquareDisabled),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogRadioBackground),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogRadioBackgroundChecked),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogProgressCircle),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogButton),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogButtonSelector),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogScrollGlow),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogRoundCheckBox),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogRoundCheckBoxCheck),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogBadgeBackground),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogBadgeText),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogLineProgress),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogLineProgressBackground),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialogGrayLine),

                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBar),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarSelector),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarTitle),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarTop),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarSubtitle),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_actionBarItems),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_background),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_time),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_progressBackground),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_progressCachedBackground),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_progress),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_placeholder),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_placeholderBackground),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_button),
                new ThemeDescription(null, 0, null, null, null, null, Theme.key_player_buttonActive),
        };
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        setData(response.body());

    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {

    }

    private void setData(JsonObject results) {
        try {
            JsonObject jsonData = results.get("data").getAsJsonObject();
            noOfColumn = jsonData.get("columns").getAsInt();
            VasniSchema.Companion.getInstance().setFaq(jsonData.get("faq").getAsString());
            itemModels.clear();
            JsonArray items = jsonData.get("items").getAsJsonArray();
            for (int i = 0; i < items.size(); i++) {
                JsonObject jsonObject = items.get(i).getAsJsonObject();
                DynamicLayout dynamicData = new DynamicLayout(
                        jsonObject.get("columnSpan").getAsInt(),
                        jsonObject.get("rowSpan").getAsInt(),
                        jsonObject.get("position").getAsInt(),
                        jsonObject.get("background").getAsString(),
                        noOfColumn,
                        jsonData.get("rows").getAsInt(),
                        jsonObject.get("itemId").getAsInt(),
                        jsonObject.get("event").getAsString(),
                        jsonObject.get("eventData").getAsJsonObject().toString(),
                        jsonObject.get("clickable").getAsInt(),
                        jsonObject.get("need_profile").getAsInt(),
                        jsonObject.get("is_free").getAsInt(),
                        jsonObject.get("title").getAsString(),
                        jsonData.get("faq").toString()
                );
                itemModels.add(dynamicData);
            }
            asymetricGridAdapter.notifyDataSetChanged();
            asymmetricGridView.setRequestedColumnCount(noOfColumn);
            asymmetricGridView.determineColumns();
            asymmetricGridView.setRequestedHorizontalSpacing(VasniSchema.Companion.getInstance().dpToPx(ApplicationLoader.applicationContext, ((float) noOfColumn)));
            asymmetricGridView.setDividerHeight(VasniSchema.Companion.getInstance().dpToPx(ApplicationLoader.applicationContext, ((float) noOfColumn)));
            asymmetricGridView.setAdapter(new AsymmetricGridViewAdapter(ApplicationLoader.applicationContext, asymmetricGridView, asymetricGridAdapter));
            asymmetricGridView.setPadding(VasniSchema.Companion.getInstance().dpToPx(ApplicationLoader.applicationContext, ((float) noOfColumn)),
                    VasniSchema.Companion.getInstance().dpToPx(ApplicationLoader.applicationContext, ((float) noOfColumn)),
                    VasniSchema.Companion.getInstance().dpToPx(ApplicationLoader.applicationContext, ((float) noOfColumn)),
                    VasniSchema.Companion.getInstance().dpToPx(ApplicationLoader.applicationContext, ((float) noOfColumn)));
            asymmetricGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        DynamicLayout dynamicLayout = itemModels.get(position);
                        String event = dynamicLayout.getEvent();
                        EventHandler eventData = new Gson().fromJson(dynamicLayout.getEventData(), EventHandler.class);
                        if (event.equals(VasniSchema.Companion.getInstance().getEv_channel()) && eventData.getType().equals(VasniSchema.Companion.getInstance().getDirect())) {
                            getChannelInfo(eventData.getChannel());
                        } else {
                            teenTaakEventClick(dynamicLayout, getParentActivity());
                        }


                    } catch (Exception e) {

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getChannelInfo(String id) {
        ApiService.INSTANCE.getApiInterface().getChannelInfo(id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (getSuccess(response.body()).getSuccess() == VasniSchema.Companion.getInstance().getSuccess()) {
                    ResponseGetBots getBotsArrayList = new Gson().fromJson(response.body(), ResponseGetBots.class);
                    MessagesController.getInstance(currentAccount).openByBotName(getBotsArrayList.getData().get(0).getChannel_id(), DialogsActivity.this, 0);
                } else {
                    VasniSchema.Companion.getInstance().showMessage(
                            getParentActivity(),
                            getError(response.body()).getMessage().toString(),
                            "",
                            getParentActivity().getString(R.string.ok));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                VasniSchema.Companion.getInstance().showMessage(
                        getParentActivity(),
                        getParentActivity().getString(R.string.server_error),
                        "",
                        getParentActivity().getString(R.string.ok));
            }
        });
    }

    private void getProfileInfo() {
        ApiService.INSTANCE.getApiInterface().getProfile().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (getSuccess(response.body()).getSuccess() == VasniSchema.Companion.getInstance().getSuccess()) {
                    profile = new Gson().fromJson(response.body(), ResponseProfile.class);
                    Function.setUserId(getParentActivity(), "" + profile.getData().getId());
                    tv_profile_balance.setText(profile.getData().getBalance() + " " + LocaleController.getString("rial", R.string.rial));
                    tv_profile_score.setText("" + profile.getData().getUserPoint());
                } else {
                    VasniSchema.Companion.getInstance().showMessage(
                            getParentActivity(),
                            getError(response.body()).getMessage().toString(),
                            "",
                            getParentActivity().getString(R.string.ok));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                VasniSchema.Companion.getInstance().showMessage(
                        getParentActivity(),
                        getParentActivity().getString(R.string.server_error),
                        "",
                        getParentActivity().getString(R.string.ok));
            }
        });
    }

    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("service_logout", R.string.service_logout));
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), (dialogInterface, i) -> MessagesController.getInstance(currentAccount).performLogout(1));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    public interface DialogsActivityDelegate {
        void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param);
    }

    public void setUser(TLRPC.User user) {
        if (user == null) {
            return;
        }
        TLRPC.FileLocation photo = null;
        if (user.photo != null) {
            photo = user.photo.photo_small;
        }
        tv_user_name.setText(UserObject.getUserName(user));
        if (user.phone != null)
            tv_user_phone.setText(PhoneFormat.getInstance().format("+" + user.phone));
        AvatarDrawable avatarDrawable = new AvatarDrawable(user);
        avatarDrawable.setColor(Theme.getColor(Theme.key_avatar_backgroundInProfileBlue));
        imv_user_pic.getImageReceiver().setRoundRadius(AndroidUtilities.dp(37));
        imv_user_pic.setImage(photo, "50_50", avatarDrawable);
        tv_profile_setting.setTextColor(Theme.getColor(Theme.key_actionBarDefault));
        /*ll_top_bgr.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefault));
        ll_top_bgr.setAlpha((float) 0.5);*/
    }
}
