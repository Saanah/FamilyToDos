package com.example.familytodos

sealed class Screens(val route: String) {

    object LoginScreen : Screens("login_screen")
    object MainScreen : Screens("main_screen")
    object RegisterScreen : Screens("register_screen")
    object CreateGroupScreen : Screens("create_group_screen")
    object GroupDetailScreen : Screens("group_detail_screen")
    object AddGroupMembersScreen : Screens("add_group_members_screen")
    object CreateTaskScreen : Screens("create_task_screen")
    object GroupInfoScreen : Screens("group_info_screen")
    object HighscoreScreen : Screens("highscore_screen")
    object UserPointsScreen : Screens("user_points_screen")
    object AccountScreen : Screens("account_screen")
}
