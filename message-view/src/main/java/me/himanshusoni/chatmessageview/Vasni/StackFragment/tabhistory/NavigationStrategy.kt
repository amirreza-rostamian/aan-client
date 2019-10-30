package me.himanshusoni.chatmessageview.Vasni.StackFragment.tabhistory

import me.himanshusoni.chatmessageview.Vasni.StackFragment.FragNavSwitchController


sealed class NavigationStrategy

class CurrentTabStrategy : NavigationStrategy()

class UnlimitedTabHistoryStrategy(val fragNavSwitchController: FragNavSwitchController) : NavigationStrategy()

class UniqueTabHistoryStrategy(val fragNavSwitchController: FragNavSwitchController) : NavigationStrategy()