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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    modifier: Modifier = Modifier
) {
    val stepSize = 1.0f
    val modelProducer = remember { CartesianChartModelProducer() }

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
                // 막대색깔인 chatColors는 코드 맨 밑에 있음
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
                        unit == "술" && value.toInt() == 0 -> "금주"  // 술일 때 0이면 금주
                        unit == "술" && value.toInt() == 1 -> "음주"  // 술일 때 1이면 음주
                        else -> "${value.toInt()}$unit"  // 그 외에는 값과 단위 출력
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
//            decorations = listOf(
//                HorizontalLine(
//                    y = { threshold },  // y값 3에서 수평선 추가
//                    line = rememberLineComponent(
//                        color = DarkRed,
//                        thickness = 2.dp,
//                    ),
////                            labelComponent = rememberTextComponent(
////                                color = Black,
////                                textSize = 14.sp,
////                            ),
////                            label = { "목표 : ${threshold.toInt()} " + unit },
//                    horizontalLabelPosition = HorizontalPosition.Start,
//                    verticalLabelPosition = VerticalPosition.Top,
//                    labelRotationDegrees = 0f,
//                )
//            ),
        ),
        marker = rememberDefaultCartesianMarker(
            valueFormatter = remember { XYValueFormatter() },
            label = rememberTextComponent(
                color = Black, // 막대 눌렀을 때 위에 나오는 값(텍스트 색깔)
                textSize = 16.sp,
            ),
            indicator = rememberShapeComponent(
                shape = Shape.Rectangle,
                color = Black, // 막대 눌렀을 때 위에 나오는 직사각형 색깔
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

// color는 y축 텍스트 색깔
@Composable
fun rememberStartAxisLabel() =
    rememberAxisLabelComponent(
        color = Black,
//        background = rememberShapeComponent(shape = Shape.rounded(4.dp)),
        padding = Dimensions.of(horizontal = 8.dp, vertical = 2.dp),
        margins = Dimensions.of(all = 4.dp),

        )

// color는 x축 텍스트 색깔
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

                else -> {
                    result.append("Non-line target\n")
                }
            }
        }

        return result.toString()
    }
}

// 막대 색깔!!
private val chartColors = listOf(MediumBlue)


