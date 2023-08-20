package com.example.familytodos.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.familytodos.AuthViewModel
import com.example.familytodos.Screens
import com.example.familytodos.data.model.MenuItemData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    navController: NavController,
    menuItems: List<MenuItemData> = getMenuItems(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    var isMenuOpen by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            IconButton(onClick = { isMenuOpen = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Open menu"
                )
            }

            DropdownMenu(
                modifier = Modifier.width(width = 150.dp),
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false },
                offset = DpOffset(x = (-102).dp, y = (-64).dp),
                properties = PopupProperties()


            ) {

                //Creating menu items for dropdownmenu
                menuItems.forEach { menuItem ->
                    val itemText = menuItem.text
                    DropdownMenuItem(text = { Text(itemText) },
                        onClick = {
                            if (menuItem.route == Screens.LoginScreen.route) {  //If log out is clicked
                                authViewModel.logout()
                                navController.navigate(menuItem.route) {
                                    //popUpTo(Screens.LoginScreen.route)      //Remove all screens up to Login screen
                                }
                            }
                            //If groupId needs to be passed
                            else if (menuItem.groupId != null){
                                navController.navigate("${menuItem.route}/${menuItem.groupId}"){
                                }
                            }
                            else {
                                navController.navigate(menuItem.route)
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = menuItem.icon,
                                contentDescription = menuItem.text
                            )
                        }
                    )
                }
            }
        }
    )
}

//Adds menu items to the array and returns it
fun getMenuItems(): ArrayList<MenuItemData> {

    val listItems = ArrayList<MenuItemData>()

    listItems.add(
        MenuItemData(
            text = "Account",
            icon = Icons.Outlined.Person,
            route = Screens.AccountScreen.route
        )
    )
    listItems.add(
        MenuItemData(
            text = "Points",
            icon = Icons.Outlined.Star,
            route = Screens.UserPointsScreen.route
        )
    )
    listItems.add(
        MenuItemData(
            text = "Mail",
            icon = Icons.Outlined.Mail,
            route = Screens.CreateGroupScreen.route
        )
    )
    listItems.add(
        MenuItemData(
            text = "Log out",
            icon = Icons.Outlined.Logout,
            route = Screens.LoginScreen.route
        )
    )

    return listItems
}

