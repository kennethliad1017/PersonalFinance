package com.differentshadow.personalfinance.ui


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.ui.customanimation.colorbuttons.BellColorButton
import com.differentshadow.personalfinance.ui.customanimation.colorbuttons.ButtonBackground
import com.differentshadow.personalfinance.ui.customanimation.colorbuttons.CalendarAnimation
import com.differentshadow.personalfinance.ui.customanimation.colorbuttons.ColorButtonAnimation
import com.differentshadow.personalfinance.ui.customanimation.colorbuttons.GearColorButton
import com.differentshadow.personalfinance.ui.customanimation.colorbuttons.PlusColorButton

@Stable
data class WiggleButtonItem(
    @DrawableRes val backgroundIcon: Int,
    @DrawableRes val icon: Int,
    var isSelected: Boolean,
    @StringRes val description: Int,
    val animationType: ColorButtonAnimation = BellColorButton(
        tween(500),
        background = ButtonBackground(R.drawable.plus)
    ),
)

@Stable
data class Item(
    @DrawableRes val icon: Int,
    var isSelected: Boolean,
    @StringRes val description: Int,
    val animationType: ColorButtonAnimation = BellColorButton(
        tween(500),
        background = ButtonBackground(R.drawable.plus)
    ),
)

val wiggleButtonItems = listOf(
    WiggleButtonItem(
        icon = R.drawable.outline_favorite,
        backgroundIcon = R.drawable.favorite,
        isSelected = false,
        description = R.string.Heart,
    ),
    WiggleButtonItem(
        icon = R.drawable.outline_energy_leaf,
        backgroundIcon = R.drawable.energy_savings_leaf,
        isSelected = false,
        description = R.string.Leaf
    ),
    WiggleButtonItem(
        icon = R.drawable.outline_water_drop,
        backgroundIcon = R.drawable.water_drop_icon,
        isSelected = false,
        description = R.string.Drop
    ),
    WiggleButtonItem(
        icon = R.drawable.outline_circle,
        backgroundIcon = R.drawable.circle,
        isSelected = false,
        description = R.string.Circle
    ),
    WiggleButtonItem(
        icon = R.drawable.baseline_laptop,
        backgroundIcon = R.drawable.laptop,
        isSelected = false,
        description = R.string.Laptop
    ),
)

val dropletButtons = listOf(
    Item(
        icon = R.drawable.home,
        isSelected = false,
        description = R.string.Home
    ),
    Item(
        icon = R.drawable.bell,
        isSelected = false,
        description = R.string.notification
    ),
    Item(
        icon = R.drawable.message_buble,
        isSelected = false,
        description = R.string.Message
    ),
    Item(
        icon = R.drawable.heart,
        isSelected = false,
        description = R.string.Heart
    ),
    Item(
        icon = R.drawable.person,
        isSelected = false,
        description = R.string.Person
    ),
)

val colorButtons = listOf(
    Item(
        icon = R.drawable.outline_home,
        isSelected = true,
        description = R.string.Home,
        animationType = BellColorButton(
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 20f),
            background = ButtonBackground(
                icon = R.drawable.circle_background,
                offset = DpOffset(2.5.dp, 3.dp)
            ),
        )
    ),
    Item(
        icon = R.drawable.chart_pie_simple,
        isSelected = false,
        description = R.string.notification,
        animationType = BellColorButton(
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 20f),
            background = ButtonBackground(
                icon = R.drawable.rectangle_background,
                offset = DpOffset(1.dp, 2.dp)
            ),
        )
    ),
    Item(
        icon = R.drawable.rounded_rect,
        isSelected = false,
        description = R.string.Plus,
        animationType = PlusColorButton(
            animationSpec = spring(
                dampingRatio = 0.3f,
                stiffness = Spring.StiffnessVeryLow
            ),
            background = ButtonBackground(
                icon = R.drawable.polygon_background,
                offset = DpOffset(1.6.dp, 2.dp)
            ),
        )
    ),
    Item(
        icon = R.drawable.wallet,
        isSelected = false,
        description = R.string.Calendar,
        animationType = CalendarAnimation(
            animationSpec = spring(
                dampingRatio = 0.3f,
                stiffness = Spring.StiffnessVeryLow
            ),
            background = ButtonBackground(
                icon = R.drawable.quadrangle_background,
                offset = DpOffset(1.dp, 1.5.dp)
            ),
        )
    ),
    Item(
        icon = R.drawable.gear,
        isSelected = false,
        description = R.string.Settings,
        animationType = GearColorButton(
            animationSpec = spring(
                dampingRatio = 0.3f,
                stiffness = Spring.StiffnessVeryLow
            ),
            background = ButtonBackground(
                icon = R.drawable.gear_background,
                offset = DpOffset(2.5.dp, 3.dp)
            ),
        )
    )
)