package com.wqz.allinone.act.diary.data

import com.wqz.allinone.R

/**
 * 选项数据
 * Created by Wu Qizhen on 2024.12.31
 */
object OptionData {
    val weatherOptions = listOf(
        Option(1, "晴", R.drawable.weather_sunny),
        Option(2, "多云", R.drawable.weather_cloudy),
        Option(3, "阴", R.drawable.weather_overcast_sky),
        Option(4, "小雨", R.drawable.weather_small_rain),
        Option(5, "中雨", R.drawable.weather_moderate_rain),
        Option(6, "大雨", R.drawable.weather_heavy_rain),
        Option(7, "暴雨", R.drawable.weather_rainstorm),
        Option(8, "阵雨", R.drawable.weather_shower),
        Option(9, "雷阵雨", R.drawable.weather_thundershower),
        Option(10, "雪", R.drawable.weather_light_snow),
        Option(11, "小雪", R.drawable.weather_light_snow),
        Option(12, "中雪", R.drawable.weather_moderate_snow),
        Option(13, "大雪", R.drawable.weather_heavy_snow),
        Option(14, "冰雹", R.drawable.weather_sleet),
        Option(15, "雾", R.drawable.weather_fog),
        Option(16, "霾", R.drawable.weather_haze),
        Option(17, "沙尘暴", R.drawable.weather_sand_blowing),
        Option(18, "暴风雪", R.drawable.weather_blizzard)
    )

    val moodOptions = listOf(
        Option(1, "微笑", R.drawable.mood_smile),
        Option(2, "快乐", R.drawable.mood_happy),
        Option(3, "大笑", R.drawable.mood_big_laugh),
        Option(4, "亲吻", R.drawable.mood_kiss),
        Option(5, "喜爱", R.drawable.mood_flirty),
        Option(6, "疑惑", R.drawable.mood_confused),
        Option(7, "惊讶", R.drawable.mood_surprised),
        Option(8, "酷炫", R.drawable.mood_cool),
        Option(9, "晕眩", R.drawable.mood_daze),
        Option(10, "哭泣", R.drawable.mood_crying),
        Option(11, "生病", R.drawable.mood_sick),
        Option(12, "震惊", R.drawable.mood_shocked),
        Option(13, "吐舌", R.drawable.mood_sticking_out_tongue),
        Option(14, "天使", R.drawable.mood_angel),
        Option(15, "捂脸", R.drawable.mood_mask),
        Option(16, "哭笑", R.drawable.mood_laughing_and_crying),
        Option(17, "难过", R.drawable.mood_crying_and_fussing),
        Option(18, "流汗", R.drawable.mood_sweating),
        Option(19, "去世", R.drawable.mood_passed_away)
    )
}