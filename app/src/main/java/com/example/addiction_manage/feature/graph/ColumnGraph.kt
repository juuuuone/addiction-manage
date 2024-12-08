package com.example.addiction_manage.feature.graph

import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.DarkRed
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.White
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.rememberLegendItem
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawContext
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasureContext
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerValueFormatter
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.HorizontalPosition
import com.patrykandpatrick.vico.core.common.VerticalPosition
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlin.random.Random

@Composable
fun ColumnGraph(
    unit: String,
    data: List<Pair<String, Int>>,
) {
    val stepSize = 1.0f
    val modelProducer = remember { CartesianChartModelProducer() }
    val soju = stringResource(id = R.string.soju)
    val alcohol = stringResource(id = R.string.alcohol)
    val no_alcohol = stringResource(id = R.string.no_alcohol)

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(x = (1..data.size).toList(), y = data.map { it.second })
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    chartColors.map { color ->
                        rememberLineComponent(
                            color = color,
                            thickness = 5.dp
                        )
                    }
                )
            ),
            startAxis = rememberStartAxis(
                label = rememberStartAxisLabel(),
                guideline = null,
                valueFormatter = { value, _, _ ->
                    when {
                        unit == soju && value.toInt() == 0 -> no_alcohol
                        unit == soju && value.toInt() == 1 -> alcohol
                        else -> "${value.toInt()}$unit"
                    }
                },
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
                    val index = value.toInt() - 1
                    if (index in data.indices) {
                        val date = data[index].first
                        date.substring(5).replace("-", "/")
                    } else ""
                },
                sizeConstraint = BaseAxis.SizeConstraint.Auto(),
            )
        ),
        marker = rememberDefaultCartesianMarker(
            valueFormatter = remember { XYValueFormatter() },
            label = rememberTextComponent(
                color = Black,
                textSize = 16.sp,
            ),
            indicator = rememberShapeComponent(
                shape = Shape.Rectangle,
                color = Black,
            ),
            indicatorSize = 8.dp,
            labelPosition = DefaultCartesianMarker.LabelPosition.AbovePoint,
            guideline = null,
        ),
        modelProducer = modelProducer,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(10.dp)),
    )
}

@Composable
fun rememberStartAxisLabel() =
    rememberAxisLabelComponent(
        color = Black,
        padding = Dimensions.of(horizontal = 8.dp, vertical = 2.dp),
        margins = Dimensions.of(all = 4.dp),
        )

@Composable
fun rememberBottomAxisLabel() =
    rememberAxisLabelComponent(
        color = Black,
    )

class XYValueFormatter : CartesianMarkerValueFormatter {
    override fun format(
        context: CartesianDrawContext,
        targets: List<CartesianMarker.Target>
    ): CharSequence {
        val result = StringBuilder()

        for (target in targets) {
            when (target) {
                is ColumnCartesianLayerMarkerTarget -> {
                    for (column in target.columns) {
                        val y = column.entry.y.toInt()
                        result.append("$y" + "번")
                    }
                }
            }
        }

        return result.toString()
    }
}

private val chartColors = listOf(MediumBlue)


