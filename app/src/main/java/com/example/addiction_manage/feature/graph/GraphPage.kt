package com.example.addiction_manage.feature.graph

import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.addiction_manage.feature.calendar.BottomAppBarComponent
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.DarkRed
import com.example.addiction_manage.ui.theme.PurpleGrey40
import com.example.addiction_manage.ui.theme.White
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.rememberLegendItem
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawContext
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasureContext
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.Decoration
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.common.Defaults
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.drawContext
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlin.random.Random

@Composable
fun GraphPage(
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
    navigateToGraph: () -> Unit,
    navigateToMyPage: () -> Unit,
    selectedItem :Int,
    navController: NavController,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBarComponent(
                navigateUp = { navController.navigateUp() },
                navigateToMyPage = navigateToMyPage,
            )
        },
        bottomBar = {
            BottomAppBarComponent(
                navigateToCalendar = navigateToCalendar,
                navigateToHome = navigateToHome,
                navigateToStatistic = navigateToStatistic,
                navigateToGraph = navigateToGraph,
                selectedItem=selectedItem,
                isCalendarPage = true,
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            LineGraph()
        }
    }
}

@Composable
fun LineGraph(
    unit: String = "번",
    threshold: Float? = null
) {
    val stepSize: Float = 1.0f
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(x = listOf(1, 2, 3, 4, 5, 6, 7), y = List(7) { Random.nextInt(1, 7) })
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lines = chartColors.map { color ->
                    rememberLineSpec(shader = DynamicShader.color(color), backgroundShader = null)
                },
            ),
            startAxis = rememberStartAxis(
                label = rememberStartAxisLabel(),
                guideline = null,
                tickLength = 5.dp,
                valueFormatter = { value, _, _ -> "${value.toInt()}" + unit },
                itemPlacer = AxisItemPlacer.Vertical.step(
                    { extraStore -> stepSize },
                    shiftTopLines = true
                ),
                sizeConstraint = BaseAxis.SizeConstraint.Auto(),
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside,
                verticalLabelPosition = VerticalAxis.VerticalLabelPosition.Center
            ),
            bottomAxis = rememberBottomAxis(
                label = rememberBottomAxisLabel(),
                itemPlacer = AxisItemPlacer.Horizontal.default(
                    offset = 10,
                    shiftExtremeTicks = false
                ),
                guideline = null,
                valueFormatter = { value, _, _ ->
                    ("${value.toInt()}DAY")
                },
                sizeConstraint = BaseAxis.SizeConstraint.Auto(),
            ),
            legend = rememberLegend(), // 범례
        ),
        modelProducer = modelProducer,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Black),
    )
}

@Composable
fun rememberStartAxisLabel() =
    rememberAxisLabelComponent(
        color = White,
//        background = rememberShapeComponent(shape = Shape.rounded(4.dp)),
        padding = Dimensions.of(horizontal = 8.dp, vertical = 2.dp),
        margins = Dimensions.of(all = 4.dp),
    )

@Composable
fun rememberBottomAxisLabel() =
    rememberAxisLabelComponent(
        color = White,
    )


@Composable
private fun rememberLegend() =
    rememberHorizontalLegend<CartesianMeasureContext, CartesianDrawContext>(
        items = chartColors.mapIndexed { index, chartColor ->
            rememberLegendItem(
                icon = rememberShapeComponent(Shape.Pill, chartColor),
                label =
                rememberTextComponent(
                    color = White,
                    textSize = 12.sp,
                    typeface = Typeface.MONOSPACE,
                ),
                labelText = listOf("Smoking", "Alcohol", "Caffeine")[index]
            )
        },
        iconSize = 8.dp,
        iconPadding = 8.dp,
        spacing = 4.dp,
        padding = Dimensions.of(top = 8.dp),
    )

private val chartColors = listOf(White)


