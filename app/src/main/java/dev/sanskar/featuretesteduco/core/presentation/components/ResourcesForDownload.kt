package dev.sanskar.featuretesteduco.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.sanskar.featuretesteduco.core.domain.models.CourseResource
//import dev.sanskar.featuretesteduco.core.domain.util.AndroidDownloader
import dev.sanskar.featuretesteduco.core.domain.util.AndroidDownloader
import dev.sanskar.featuretesteduco.ui.theme.Grey400
import dev.sanskar.featuretesteduco.ui.theme.Grey800
import dev.sanskar.featuretesteduco.R

@Composable
fun ResourcesForDownload(
    courseResource: CourseResource
) {
    Column {
        for (i in courseResource.resourceUrl.indices) {
            ResourceItem2(
                resourceName = courseResource.resourceName[i],
                resourceUrl = courseResource.resourceUrl[i],
                resourceSize = courseResource.resourceSize[i],
                fileType = courseResource.fileType[i]
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ResourceItem2(
    resourceName: String,
    resourceUrl: String,
    resourceSize: Double,
    fileType: String
) {
    val context = LocalContext.current
    val downloader = AndroidDownloader(context = context)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FileTypeBox(fileType)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = resourceName,
                    color = Grey800,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = ".$fileType", color = Grey400)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "$resourceSize Mb", color = Grey400)
                }
            }
        }
        IconButton(onClick = { downloader.downloadFile(resourceUrl) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = null,
                tint = Grey400
            )
        }
    }
}
@Composable
fun FileTypeBox(
    fileType: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = Color(0XFFFF6363))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center

    ) {
        Text(text = ".${fileType}", color = Color.White)
    }
}
