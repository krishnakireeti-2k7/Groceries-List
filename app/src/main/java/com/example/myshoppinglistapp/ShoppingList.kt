package com.example.myshoppinglistapp

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class ShoppingIteam(var id:Int,
                         var name:String,
                         var quantity:Int,
                         var Isediting:Boolean = false )

@Preview
@Composable
fun ShoppingList(){

    var sIteams by remember { mutableStateOf(listOf<ShoppingIteam>()) }
    var showDialog by remember { mutableStateOf(false) }
    var iteamName by remember { mutableStateOf("")}
    var iteamQuantity by remember { mutableStateOf("")}

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center) {
        Button(onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(16.dp)) {
            Text(text = "Add Iteam  +", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
        }
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(sIteams){
                item ->
                if (item.Isediting){
                    ShoppingIteamEditor(iteam = item, onEditcomplete = {
                        editedName, editedQuantity ->
                        sIteams = sIteams.map { it.copy(Isediting = false) }
                        val editedItem = sIteams.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                }else{
                    ShoppingListIteam(
                        item = item,
                        onEditClick = { sIteams = sIteams.map { it.copy(Isediting = it.id == item.id) } },
                        onDeleteClick = { sIteams -= item }) {
                        
                    }
                }
            }
        }
    }

    if(showDialog){/* If show dialogue Box is true then this if statement will show an alert dialouge
    box and and all parameter are written here*/
        AlertDialog(onDismissRequest = {showDialog = false},
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(onClick = {
                                    if (iteamName.isNotBlank()){
                                        val newiteam = ShoppingIteam(
                                            id = sIteams.size + 1,
                                            name = iteamName,
                                            quantity = iteamQuantity.toInt()
                                        )
                                        sIteams += newiteam
                                        showDialog = false
                                        iteamName = ""
                                        iteamQuantity = ""//we're making the iteam name and iteam quantity into emtpy string so every time we open dialouge box it is empty
                                    }
                                }) {
                                    Text(text = "Add", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                }
                                FilledTonalButton(onClick = { showDialog = false }) {
                                    Text(text = "Cancel", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            }
            },
            title = { Text(text = "Add Iteam")},
            text = {
                Column {
                    OutlinedTextField(value = iteamName,
                        onValueChange = {iteamName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = iteamQuantity,
                        onValueChange ={iteamQuantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        )
                }
            }
            )
        }
    }
@Composable
fun ShoppingIteamEditor(iteam: ShoppingIteam,onEditcomplete: (String,Int) -> Unit){
    var editedName by remember { mutableStateOf(iteam.name)}
    var editedQuantity by remember { mutableStateOf(iteam.quantity.toString())}
    var isEditing by remember { mutableStateOf(iteam.Isediting)}
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            BasicTextField(value = editedName, onValueChange = {editedName = it}, singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))

            BasicTextField(value = editedQuantity, onValueChange = {editedQuantity = it}, singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))

        }
        Button(onClick = {
            isEditing = false
            onEditcomplete(editedName,editedQuantity.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }

    }

}

@Composable
fun ShoppingListIteam(
    item: ShoppingIteam,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    function: () -> Unit,
){
Row(modifier = Modifier
    .fillMaxWidth()
    .padding(16.dp)
    .border(
        border = BorderStroke(2.dp, Color.Gray),
        shape = RoundedCornerShape(25)

    ), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(text = item.name,modifier = Modifier.padding(25.dp), fontSize = 18.sp, fontFamily = FontFamily.Monospace)
    Text(text = "${item.quantity}",modifier = Modifier.padding(25.dp), fontSize = 18.sp, fontFamily = FontFamily.Monospace)
    Row(modifier = Modifier.padding(13.dp), horizontalArrangement = Arrangement.Absolute.Right){
        FilledTonalIconButton(onClick = onEditClick) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit option" )
        }
        FilledTonalIconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete option" )
        }
    }
}
}