package com.prafull.secondshelf.onBoard.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.prafull.secondshelf.R

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.second_shelf_logo_2),
        contentDescription = "App Logo",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
    )
}