@file:OptIn(ExperimentalMaterialApi::class)

package com.yjpapp.stockportfolio.ui.main.mystock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DialogType
import com.yjpapp.stockportfolio.ui.common.componant.LoadingWidget
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_666666
import com.yjpapp.stockportfolio.ui.main.MainViewModel
import com.yjpapp.stockportfolio.util.StockUtils

/**
 * MyStock UI Compose 분리
 * @author Yoon Jae-park
 * @since 2023.02
 */
@Composable
@ExperimentalMaterialApi
internal fun MyStockScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val myStockUiState by viewModel.myStockUiState.collectAsStateWithLifecycle()
    
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            TotalPriceWidget(
                myStockViewModel = viewModel
            )
        }
        item { Spacer(modifier = Modifier.size(10.dp)) }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "보유 주식",
                        fontWeight = FontWeight.Bold,
                        color = Color_222222,
                        fontSize = 17.sp,
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    if (myStockUiState.myStockInfoList.isNotEmpty()) {
                        Text(
                            text = "기준 일자 : ${StockUtils.convertYYYY_MM_DD(myStockUiState.myStockInfoList.first().basDt)}",
                            fontWeight = FontWeight.Light,
                            color = Color_666666,
                            fontSize = 13.sp,
                        )
                    }
                }
                Row {
                    IconButton(
                        modifier = Modifier.size(18.dp),
                        enabled = myStockUiState.myStockInfoList.isNotEmpty(),
                        onClick = { viewModel.refreshStockCurrentPriceInfo() }
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(id = R.drawable.ic_refresh),
                            contentDescription = "새로고침",
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    IconButton(
                        modifier = Modifier.size(18.dp),
                        onClick = { viewModel.showMainDialog(DialogType.InsertPurchaseInput) }
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.icon_add),
                            contentDescription = "추가하기",
                            tint = Color_222222
                        )
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.size(20.dp)) }
        if (myStockUiState.isLoading) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingWidget()
                }
            }
        } else {
            items(items = myStockUiState.myStockInfoList) { myStockData ->
                MyStockListItemWidget(
                    viewModel = viewModel,
                    myStockData = myStockData,
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun Preview() {
    MyStockScreen()
}