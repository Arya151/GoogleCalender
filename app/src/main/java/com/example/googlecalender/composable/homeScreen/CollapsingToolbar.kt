package com.example.googlecalender.composable.homeScreen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.googlecalender.R
import com.example.googlecalender.getFirstDayOfWeekOfMonth
import com.example.googlecalender.utils.months
import com.example.googlecalender.utils.week
import java.time.LocalDate
import java.util.*
import kotlin.math.max


@SuppressLint(
    "CoroutineCreationDuringComposition", "UnrememberedMutableState",
    "MutableCollectionMutableState"
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CreateCollapsingToolbar(toolbarData: ToolbarData, widthPixels: Float) {

    var preMonth by remember { mutableStateOf(2L) }
    var nextMonth by remember { mutableStateOf(1L) }
    var isCalenderOpen by remember { mutableStateOf(false) }


    val listState = rememberLazyListState(1,1)

    val bufferCalenderFuture = remember { mutableStateListOf<ToolbarData>(toolbarData) }
    val listStateFuture = rememberLazyListState()
    val visibleItemsFuture =
        listStateFuture.visibleItems(50f).map { bufferCalenderFuture[it.index] }

    val bufferCalenderPast = remember { mutableStateListOf<ToolbarData>(getPreviousMonth(1)) }
    val listStatePast = rememberLazyListState()
    val visibleItemsPast =
        listStatePast.visibleItems(50f).map { bufferCalenderPast[it.index] }


    Column(Modifier.background(Color.DarkGray)) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.DarkGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painterResource(id = R.drawable.ic_round_menu),
                "menu"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (!visibleItemsFuture.isNullOrEmpty()) {
                    visibleItemsFuture[0].monthName
                } else if (!visibleItemsPast.isNullOrEmpty()) {
                    visibleItemsPast[0].monthName
                } else {
                    "kop"
                },
                color = Color.White,
                fontSize = 20.sp
            )
            Image(
                painterResource(id = if (isCalenderOpen) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down),
                "drop down",
                modifier = Modifier.clickable {
                    isCalenderOpen = !isCalenderOpen
                }
            )
            Spacer(
                Modifier
                    .weight(1f)
                    .background(Color.Green)
            )
            Image(
                painterResource(id = R.drawable.ic_round_search),
                "search",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Image(
                painterResource(id = R.drawable.ic_date_range_calender),
                "go to current date",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Image(
                painterResource(id = R.drawable.ic_round_account_circle),
                "user",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                     // called when you scroll the content
                    if (listStatePast.isLastItemVisible) {
                        bufferCalenderPast.add(getPreviousMonth(preMonth))
                        preMonth++
                        Log.d("abx","i am here")
                    }
                    if (listStateFuture.isLastItemVisible) {
                        bufferCalenderFuture.add(getNextMonth(nextMonth))
                        nextMonth++
                        Log.d("abx","i am here 2")
                    }
                    return Offset.Zero
                }
            }
        }

        AnimatedVisibility(
            visible = isCalenderOpen,
            enter = expandVertically(tween(1000)) { -it },
            exit = shrinkVertically(tween(1000)) { -it }
        ) {
            Log.d("abx", "buffer size  nextMonth - $nextMonth , preMonth - $preMonth")

            LazyRow(state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                modifier = Modifier.nestedScroll(nestedScrollConnection)
            ){

                item {
                    /**
                     * Past Calender
                     */
                    LazyRow(
                        state = listStatePast,
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = listStatePast),
                        reverseLayout = true,
                        modifier = Modifier
                            .fillParentMaxWidth()
                    ) {

                        items(bufferCalenderPast) { item ->
                            Column(
                                modifier = Modifier.fillParentMaxWidth()//.width((widthPixels / 7 - 32).dp)
                            ) {
                                CreateCalenderView(item)
                            }
                        }
                    }
                }

                item{
                    /**
                     * Future Calender
                     */
                    LazyRow(
                        state = listStateFuture,
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = listStateFuture),
                        modifier = Modifier
                            .fillParentMaxWidth()
                    ) {
                        items(bufferCalenderFuture) { item ->
                            Column(
                                modifier = Modifier.fillParentMaxWidth()//.width((widthPixels / 7 - 32).dp)
                            ) {
                                CreateCalenderView(item)
                            }

                        }
                    }

                }
            }





//            if(!visibleItems.isNullOrEmpty() && visibleItems[0].monthName == bufferCalender[bufferCalender.size-1].monthName){
//                nextMonth += 1
//                bufferCalender.add(getNextMonth(nextMonth))
//            }
/*            if(!visibleItems.isNullOrEmpty() && visibleItems[0].currentDate == bufferCalender[0].currentDate){
                coroutineScope.launch {
                    listState.scrollToItem(1)
                }
                nextMonth += 1
                bufferCalender[2] = bufferCalender[1]
                bufferCalender[1] = bufferCalender[0]
                bufferCalender[0] = getPreviousMonth(nextMonth)
                Log.d("abx","length = ${bufferCalender.size} \n nextMonth = $nextMonth \n Buffer Calender - ${bufferCalender.toList()}")
            }*/
        }
    }
}

@Composable
fun CreateCalenderView(toolbarData: ToolbarData) {
    val temp = (1..toolbarData.totalDays).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        var count = 0
        items(week) { item ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${item[0]}",
                    color = if (week[
                                if (toolbarData.daysToBeSkipped + 1 == week.size)
                                    0 else toolbarData.daysToBeSkipped + 1] == item
                    )
                        Color.Cyan else Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            count++
        }

        if (toolbarData.daysToBeSkipped != 7)
            items((0 until toolbarData.daysToBeSkipped).toList()) {}

        items(temp) { item ->
            Box(contentAlignment = Alignment.Center) {
                if (toolbarData.currentDate != null && toolbarData.currentDate == item)
                    Image(
                        painter = painterResource(id = R.drawable.ic_circle),
                        contentDescription = "",
                        Modifier.size(34.dp),
                        colorFilter = ColorFilter.tint(Color.Cyan)
                    )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(11.dp))
                    Text(
                        text = "$item",
                        color = if (toolbarData.currentDate != null && toolbarData.currentDate == item) Color.Black else Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(11.dp))
                }
            }

        }
    }
    Spacer(modifier = Modifier.height(18.dp))
}


fun getNextMonth(nextMonth: Long): ToolbarData {
    var thisMonth = ""
    var totalDaysOfMonth = 0
    var weekDayOfStartingDate = ""

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val preM = LocalDate.now().plusMonths(nextMonth)
        thisMonth = if(LocalDate.now().year == preM.year) months[preM.monthValue - 1] else (months[preM.monthValue - 1]+preM.year)
        totalDaysOfMonth = preM.lengthOfMonth()
        weekDayOfStartingDate =
            LocalDate.of(preM.year, preM.month, 1).dayOfWeek.name
    } else {
        val calender = Calendar.getInstance()
        calender.add(Calendar.MONTH, nextMonth.toInt())

        thisMonth = months[calender.get(Calendar.MONTH) - 1]
        totalDaysOfMonth = calender.getActualMaximum(calender.get(Calendar.MONTH))
        weekDayOfStartingDate = getFirstDayOfWeekOfMonth(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH))
    }

    var left = 0
    for (i in week) {
        if (weekDayOfStartingDate.uppercase() == i.uppercase()) {
            break
        }
        left++
    }

    return ToolbarData(
        monthName = thisMonth,
        totalDays = totalDaysOfMonth,
        currentDate = null,
        daysToBeSkipped = left
    )

}

fun getPreviousMonth(preMonth: Long): ToolbarData {
    var thisMonth = ""
    var totalDaysOfMonth = 0
    var weekDayOfStartingDate = ""

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val preM = LocalDate.now().minusMonths(preMonth)
        thisMonth = if(LocalDate.now().year == preM.year) months[preM.monthValue - 1] else (months[preM.monthValue - 1]+preM.year)
        totalDaysOfMonth = preM.lengthOfMonth()
        weekDayOfStartingDate =
            LocalDate.of(preM.year, preM.month, 1).dayOfWeek.name
    } else {
        val calender = Calendar.getInstance()
        calender.add(Calendar.MONTH, -preMonth.toInt())

        thisMonth = months[calender.get(Calendar.MONTH) - 1]
        totalDaysOfMonth = calender.getActualMaximum(calender.get(Calendar.MONTH))
        weekDayOfStartingDate = getFirstDayOfWeekOfMonth(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH))
    }

    var left = 0
    for (i in week) {
        if (weekDayOfStartingDate.uppercase() == i.uppercase()) {
            break
        }
        left++
    }

    Log.d("abx","previous month - $thisMonth")
    return ToolbarData(
        monthName = thisMonth,
        totalDays = totalDaysOfMonth,
        currentDate = null,
        daysToBeSkipped = left
    )

}

data class ToolbarData(
    var monthName: String,
    val totalDays: Int,
    val currentDate: Int?,
    val daysToBeSkipped: Int
)

fun LazyListState.visibleItems(itemVisiblePercentThreshold: Float) =
    layoutInfo
        .visibleItemsInfo
        .filter {
            visibilityPercent(it) >= itemVisiblePercentThreshold
        }

fun LazyListState.visibilityPercent(info: LazyListItemInfo): Float {
    val cutTop = max(0, layoutInfo.viewportStartOffset - info.offset)
    val cutBottom = max(0, info.offset + info.size - layoutInfo.viewportEndOffset)

    return max(0f, 100f - (cutTop + cutBottom) * 100f / info.size)
}

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

val LazyListState.isFirstItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == 0

enum class ScrollDirection {
    RIGHT, LEFT
}


/*modifier = Modifier
                    .pointerInput(Unit) {
                        Log.d("abx","i am here")
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            Log.d("abx","i am here 2")

                            val (x, y) = dragAmount
                            when {
                                x > 0 -> {
                                    //right
                                    Log.d("abx", "right swipe -> $x")
//                                                if(listStatePast.isFirstItemVisible){
//                                                    stateType = listStateFuture
//                                                    bufferCalenderType = bufferCalenderFuture
//                                                }
                                }
                                x < 0 -> {
                                    //left
                                    Log.d("abx", "left swipe -> $x")
//                                                if(listStatePast.isFirstItemVisible){
//                                                    stateType = listStatePast
//                                                    bufferCalenderType = bufferCalenderPast
//                                                }
                                }
                            }

                            offsetXFuture += dragAmount.x
                            offsetXPast -= dragAmount.x

                            //offsetY += dragAmount.y
                        },
                        *//*onDragEnd = {
                            if(swipeType == "r"){
                                offsetX = width
                            }else{
                                offsetX = -width
                            }
                            android.util.Log.d("abx", "drag end -> ${offsetX}")
                        }*//*
                    )

                }*/