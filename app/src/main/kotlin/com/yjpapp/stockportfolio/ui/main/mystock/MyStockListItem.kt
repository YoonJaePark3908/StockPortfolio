package com.yjpapp.stockportfolio.ui.main.mystock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjpapp.data.model.MyStockData
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DialogType
import com.yjpapp.stockportfolio.ui.common.StockConfig
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_4876C7
import com.yjpapp.stockportfolio.ui.common.theme.Color_666666
import com.yjpapp.stockportfolio.ui.common.theme.Color_80000000
import com.yjpapp.stockportfolio.ui.common.theme.Color_CD4632
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF
import com.yjpapp.stockportfolio.ui.main.MainViewModel
import com.yjpapp.stockportfolio.util.StockUtils
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import de.charlex.compose.rememberRevealState
import de.charlex.compose.reset
import kotlinx.coroutines.launch

/**
 * MyStockList Item Compose UI
 * @author Yoon Jae-park
 * @since 2022.02
 */
@ExperimentalMaterialApi
@Composable
internal fun MyStockListItemWidget(
    viewModel: MainViewModel,
    myStockData: MyStockData
) {
    val revealSwipeState = rememberRevealState()
    val coroutineScope = rememberCoroutineScope()
    val maxRevealDp = 110.dp
    RevealSwipe(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        maxRevealDp = maxRevealDp,
        backgroundCardEndColor = Color.White,
        animateBackgroundCardColor = false,
        state = revealSwipeState,
        directions = setOf(
//        RevealDirection.StartToEnd,
            RevealDirection.EndToStart
        ),

        hiddenContentEnd = {
            Card(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .width(maxRevealDp)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(10.dp),
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clickable {
                                viewModel.showMainDialog(DialogType.UpdatePurchaseInput(myStockData = myStockData))
                                coroutineScope.launch {
                                    revealSwipeState.reset()
                                }
                            }
                            .fillMaxWidth()
                            .weight(0.333f)
                            .background(color = Color_80000000)
                    ) {
                        Text(
                            text = stringResource(R.string.Common_Edit),
                            fontSize = 16.sp,
                            color = Color_FFFFFF
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    viewModel.deleteMyStock(
                                        myStockData = myStockData
                                    )
                                }
                                coroutineScope.launch {
                                    revealSwipeState.reset()
                                }
                            }
                            .fillMaxWidth()
                            .weight(0.333f)
                            .background(color = Color_CD4632)
                    ) {
                        Text(
                            text = stringResource(R.string.Common_Delete),
                            fontSize = 16.sp,
                            color = Color_FFFFFF
                        )
                    }
                }
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.White
        ) {
            val purchasePriceNumber =
                StockUtils.getNumDeletedComma(myStockData.purchasePrice).toDouble()
            val currentPriceNumber =
                StockUtils.getNumDeletedComma(myStockData.currentPrice).toDouble()
            val gainPriceNumber = (currentPriceNumber - purchasePriceNumber) * myStockData.purchaseCount
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = myStockData.subjectName,
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = Color_222222,
                        modifier = Modifier
                            .weight(0.50f)
                            .padding(top = 10.dp)
                    )

                    Text(
                        text = "${myStockData.purchaseCount}주",
                        fontSize = 14.sp,
                        maxLines = 1,
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.color_222222),
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .weight(0.50f)
                    )
                }

                Divider(
                    Modifier
                        .padding(10.dp)
                )
                Row(
                    Modifier
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .weight(0.5f),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        //평균단가
                        Text(
                            text = stringResource(R.string.MyStock_Purchase_Average),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_666666,
                        )

                        Text(
                            text = StockUtils.getPriceNum(myStockData.purchasePrice),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color_222222,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.MyStock_Current_Price),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_666666,
                        )

                        Text(
                            text = "${StockConfig.koreaMoneySymbol}${myStockData.currentPrice}",
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_222222,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )

                        Text(
                            text = myStockData.dayToDayPrice,
                            fontSize = 11.sp,
                            maxLines = 1,
                            color = when {
                                myStockData.dayToDayPrice.toInt() > 0 -> Color_CD4632
                                myStockData.dayToDayPrice.toInt() == 0 -> Color_222222
                                else -> Color_4876C7
                            },
                            modifier = Modifier
                                .padding(start = 5.dp)
                        )
                        val zeroNumber = if (myStockData.dayToDayPercent.first().toString() == ".") "0" else ""
                        Text(
                            text = "($zeroNumber${myStockData.dayToDayPercent}%)",
                            fontSize = 11.sp,
                            color = when {
                                myStockData.dayToDayPrice.toInt() > 0 -> Color_CD4632
                                myStockData.dayToDayPrice.toInt() == 0 -> Color_222222
                                else -> Color_4876C7
                            },
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.MyStock_Gain),
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = Color_666666
                    )
                    //수익
                    Text(
                        text = StockUtils.getPriceNum(gainPriceNumber.toInt().toString()),
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = when {
                            gainPriceNumber > 0 -> Color_CD4632
                            gainPriceNumber < 0 -> Color_4876C7
                            else -> Color_222222
                        },
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )

                    //수익 퍼센트
                    val allPurchasePriceNum =
                        (purchasePriceNumber * myStockData.purchaseCount)
                    val allCurrentPriceNum = (currentPriceNumber * myStockData.purchaseCount)
                    val gainPercentNum =
                        StockUtils.calculateGainPercent(allPurchasePriceNum, allCurrentPriceNum)
                    Text(
                        text = "(${StockUtils.getRoundsPercentNumber(gainPercentNum)})",
                        fontSize = 12.sp,
                        maxLines = 1,
                        color = when {
                            gainPriceNumber > 0 -> Color_CD4632
                            gainPriceNumber < 0 -> Color_4876C7
                            else -> Color_222222
                        },
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}