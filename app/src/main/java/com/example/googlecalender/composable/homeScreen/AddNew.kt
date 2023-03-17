package com.example.googlecalender.composable.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.googlecalender.CreateReminderActivity
import com.example.googlecalender.R

@Composable
fun AddNew() {

    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 18.dp, bottom = 18.dp)
            .clickable {
                menuExpanded = !menuExpanded
            }
    ) {
        AnimatedVisibility(visible = menuExpanded) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                ItemEvent(title = "Reminder", drawable = R.drawable.ic_alarm)
                ItemEvent(title = "Task", drawable = R.drawable.ic_alarm)
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (menuExpanded) "Event" else "",
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painterResource(id = if (menuExpanded) R.drawable.ic_alarm else R.drawable.ic_round_add_box),
                "add",
                Modifier
                    .size(size = if (menuExpanded) 44.dp else 48.dp)
                    .padding(if (menuExpanded) 8.dp else 0.dp)

            )
        }

    }
}

@Composable
fun ItemEvent(title: String, drawable: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            when(title){
                "Reminder" -> {
                    CreateReminderActivity.startActivity(this)
                }
            }
        }
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(6.dp))
        Image(
            painterResource(id = drawable),
            title,
            Modifier
                .size(44.dp)
                .padding(8.dp)
        )
    }

}