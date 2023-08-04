package com.example.familytodos

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.familytodos.components.AddGroupMembersScreen
import com.example.familytodos.components.CreateGroupScreen
import com.example.familytodos.components.CreateTaskScreen
import com.example.familytodos.components.GroupDetailScreen
import com.example.familytodos.components.LoginScreen
import com.example.familytodos.components.MainScreen
import com.example.familytodos.components.RegisterScreen

@Composable
fun Navigation(
    authViewModel: AuthViewModel,
    groupViewModel: GroupViewModel,
    groupDetailViewModel: GroupDetailViewModel,
    createGroupViewModel: CreateGroupViewModel,
    searchBarViewModel: SearchBarViewModel,
    addGroupMembersViewModel: AddGroupMembersViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.LoginScreen.route){
        composable(route = Screens.LoginScreen.route){
            LoginScreen(viewModel = authViewModel, navController = navController)
        }
        composable(route = Screens.MainScreen.route){
            MainScreen(navController = navController, viewModel = authViewModel, groupViewModel = groupViewModel)
        }

        composable(route = Screens.RegisterScreen.route)
        {
            RegisterScreen(viewModel= authViewModel, navController = navController)
        }

        composable(route = Screens.CreateGroupScreen.route)
        {
            CreateGroupScreen(createGroupViewModel = createGroupViewModel, navController = navController)
        }

        composable(route = Screens.CreateTaskScreen.route)
        {
            CreateTaskScreen(groupViewModel = groupViewModel, navController = navController)
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
            GroupDetailScreen(groupId = groupId, groupDetailViewModel = groupDetailViewModel, navController = navController)
        }
    }
}
