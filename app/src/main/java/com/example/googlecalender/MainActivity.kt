package com.example.googlecalender

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.googlecalender.composable.homeScreen.AddNew
import com.example.googlecalender.composable.homeScreen.CreateCollapsingToolbar
import com.example.googlecalender.composable.homeScreen.ToolbarData
import com.example.googlecalender.utils.months
import com.example.googlecalender.utils.week
import java.time.LocalDate
import java.time.MonthDay
import java.time.YearMonth
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContent {
            var thisMonth = 0
            var totalDaysOfMonth = 0
            var currentDate = 0
            var weekDayOfStartingDate = ""

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                thisMonth = YearMonth.now().monthValue
                totalDaysOfMonth = YearMonth.now().lengthOfMonth()
                currentDate = MonthDay.now().dayOfMonth
                weekDayOfStartingDate =
                    LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1).dayOfWeek.name
            } else {
                val calender = Calendar.getInstance()
                thisMonth = calender.get(Calendar.MONTH)
                totalDaysOfMonth = calender.getActualMaximum(calender.get(Calendar.MONTH))
                currentDate = calender.get(Calendar.DAY_OF_MONTH)
                weekDayOfStartingDate =
                    getFirstDayOfWeekOfMonth(calender.get(Calendar.YEAR), thisMonth)
            }

            var left = 0
            for (i in week) {
                if (weekDayOfStartingDate.uppercase() == i.uppercase()) {
                    break
                }
                left++
            }

            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)

            Box(modifier = Modifier.fillMaxSize()) {
                CreateCollapsingToolbar(
                    toolbarData = ToolbarData(
                        monthName = months[thisMonth - 1],
                        totalDays = totalDaysOfMonth,
                        currentDate = currentDate,
                        daysToBeSkipped = left
                    ),
                    metrics.widthPixels.toPx
                )

                AddNew()

            }

        }
    }

    val Number.toPx get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics)

}

fun getFirstDayOfWeekOfMonth(year: Int, month: Int): String {
    val calendar = Calendar.getInstance()
    calendar[Calendar.YEAR] = year
    calendar[Calendar.MONTH] = month - 1
    calendar[Calendar.DAY_OF_MONTH] = 1

    return week[calendar[Calendar.DAY_OF_WEEK]]
}