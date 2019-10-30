package me.himanshusoni.chatmessageview.ui.DatePicker;


import me.himanshusoni.chatmessageview.ui.DatePicker.util.PersianCalendar;

/**
 * Created by aliabdolahi on 1/23/17.
 */

public interface Listener {
    void onDateSelected(PersianCalendar persianCalendar);

    void onDismissed();
}
