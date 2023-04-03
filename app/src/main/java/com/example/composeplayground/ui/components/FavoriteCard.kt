package com.example.composeplayground.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeplayground.R
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun FavoriteCard(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.width(100.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.flower),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .aspectRatio(1.0f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "event title event title",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "venue name venue name",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "date date date",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

}

@Preview
@Composable
fun PreviewFavoriteCard() {
    ComposePlaygroundTheme {
        FavoriteCard()
    }
}