package com.example.familytodos

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.familytodos.components.AccountScreen
import com.example.familytodos.components.AddGroupMembersScreen
import com.example.familytodos.components.CreateGroupScreen
import com.example.familytodos.components.CreateTaskScreen
import com.example.familytodos.components.GroupDetailScreen
import com.example.familytodos.components.GroupInfoScreen
import com.example.familytodos.components.HighscoreScreen
import com.example.familytodos.components.LoginScreen
import com.example.familytodos.components.MainScreen
import com.example.familytodos.components.RegisterScreen
import com.example.familytodos.components.UserPointsScreen

@Composable
fun Navigation(
    groupViewModel: GroupViewModel,
    groupDetailViewModel: GroupDetailViewModel,
    createGroupViewModel: CreateGroupViewModel,
    searchBarViewModel: SearchBarViewModel,
    addGroupMembersViewModel: AddGroupMembersViewModel,
    taskViewModel: TaskViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.LoginScreen.route){
        composable(route = Screens.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(route = Screens.MainScreen.route){
            MainScreen(navController = navController, groupViewModel = groupViewModel)
        }

        composable(route = Screens.RegisterScreen.route)
        {
            RegisterScreen(navController = navController)
        }

        composable(route = Screens.CreateGroupScreen.route)
        {
            CreateGroupScreen(createGroupViewModel = createGroupViewModel, navController = navController)
        }

        composable(route = Screens.UserPointsScreen.route)
        {
            UserPointsScreen(navController = navController)
        }

        composable(route = Screens.AccountScreen.route)
        {
            AccountScreen(navController = navController)
        }

        composable(route = Screens.GroupInfoScreen.route + "/{groupId}",
            arguments = listOf(navArgument("groupId"){ type = NavType.StringType})
        )
        {
            val groupId = it.arguments?.getString("groupId")
            groupId?.let {groupId ->
                GroupInfoScreen(groupViewModel = groupViewModel, groupId = groupId, navController = navController)
            }
        }

        composable(route = Screens.HighscoreScreen.route + "/{groupId}",
            arguments = listOf(navArgument("groupId"){ type = NavType.StringType})
        )
        {
            val groupId = it.arguments?.getString("groupId")
            groupId?.let {groupId ->
                HighscoreScreen(groupDetailViewModel = groupDetailViewModel, groupId = groupId,  navController = navController)
            }
        }

        composable(route = Screens.CreateTaskScreen.route + "/{groupId}",
            arguments = listOf(navArgument("groupId"){ type = NavType.StringType}))
        {
            val groupId = it.arguments?.getString("groupId")
            groupId?.let{ groupId ->
                CreateTaskScreen(groupViewModel = groupViewModel, navController = navController, groupId = groupId)
            }
        }

        composable(route = Screens.AddGroupMembersScreen.route + "/{groupId}",
            arguments = listOf(navArgument("groupId"){ type = NavType.StringType }))
        {
            val groupId = it.arguments?.getString("groupId")
            groupId?.let { groupId ->
                AddGroupMembersScreen(searchBarViewModel = searchBarViewModel, addGroupMembersViewModel = addGroupMembersViewModel, navController = navController, groupId = groupId)
            }
        }

        composable(
            Screens.GroupDetailScreen.route + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments!!.getString("id")!!
            GroupDetailScreen(groupId = groupId, groupDetailViewModel = groupDetailViewModel, taskViewModel = taskViewModel, navController = navController)
        }
    }
}
