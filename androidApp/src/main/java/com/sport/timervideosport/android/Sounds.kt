package com.sport.timervideosport.android

enum class Sounds(
                  val resId: Int,
                  val title: String
) {
    BEEP(R.raw.ship39s_horn, "Пароход"),
    BELL(R.raw.the_whistle_of_a_ship_steamer, "Пароход 2"),
    DIGITAL(R.raw.the_beep_is_pressed_and_won39t_let_go, "Гудок авто"),
    DIGITAL_TWO(R.raw.train_whistle_version_2, "Гудок авто 2"),
    DOG_BARKING(R.raw.barking_dog, "лай"),
    HOWL_WOLF(R.raw.howl_wolf, "вой")
}