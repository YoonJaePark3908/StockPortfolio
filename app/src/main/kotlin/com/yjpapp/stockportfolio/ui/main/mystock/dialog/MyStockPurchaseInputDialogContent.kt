package com.yjpapp.stockportfolio.ui.main.mystock.dialog

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.extension.getSerializableExtraData
import com.yjpapp.stockportfolio.ui.common.StockConfig
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_666666
import com.yjpapp.stockportfolio.ui.common.theme.Color_E52B4E
import com.yjpapp.stockportfolio.ui.common.theme.Color_F1F1F1
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF
import com.yjpapp.stockportfolio.ui.search.CompanySearchActivity
import com.yjpapp.stockportfolio.util.StockUtils

data class MyStockPurchaseInputDialogData(
    val id: Int = -1,
    val stockPriceInfo: StockPriceData = StockPriceData(),
//    val purchaseDate: String = "",
    val purchasePrice: String = "",
    val purchaseCount: String = "",
)

@Composable
internal fun MyStockPurchaseInputDialogContent(
    isInsert: Boolean = false,
    dialogData: MyStockPurchaseInputDialogData = MyStockPurchaseInputDialogData(),
    onDismissRequest: (data: MyStockPurchaseInputDialogData, isComplete: Boolean) -> Unit
) {
    val numberPattern = Regex("^\\d+\$")
    val context = LocalContext.current
    var rememberStockPriceInfo by remember { mutableStateOf(dialogData.stockPriceInfo) }
//    var purchaseDateText by remember { mutableStateOf(dialogData.purchaseDate) }
    var purchasePriceText by remember { mutableStateOf(TextFieldValue(text = dialogData.purchasePrice)) }
    var purchaseCountText by remember { mutableStateOf(dialogData.purchaseCount) }
    val stockSearchActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val stockPriceInfo = result.data?.getSerializableExtraData("stockPriceInfo", StockPriceData::class.java)
                if (stockPriceInfo is StockPriceData) {
                    rememberStockPriceInfo = stockPriceInfo
                }
            }
        }
    )
    Dialog(onDismissRequest = { onDismissRequest(dialogData, false) }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .padding(PaddingValues(start = 20.dp, end = 20.dp))
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    if (isInsert) {
                        Row(
                            modifier = Modifier.clickable {
                                val intent = Intent(context, CompanySearchActivity::class.java)
                                stockSearchActivityResultLauncher.launch(intent)
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.IncomeNoteFragment_SubjectName),
                                fontSize = 16.sp,
                                color = Color(0xff666666)
                            )
                            BasicTextField(
                                modifier = Modifier.padding(start = 10.dp),
                                value = rememberStockPriceInfo.itmsNm,
                                onValueChange = { },
                                singleLine = true,
                                readOnly = true,
                                enabled = false,
                                decorationBox = { innerTextField ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .drawWithContent {
                                                drawContent()
                                                drawLine(
                                                    color = Color(0x1a000000),
                                                    start = Offset(
                                                        x = 0f,
                                                        y = size.height - 1.dp.toPx(),
                                                    ),
                                                    end = Offset(
                                                        x = size.width,
                                                        y = size.height - 1.dp.toPx(),
                                                    ),
                                                    strokeWidth = 1.dp.toPx(),
                                                )
                                            },
                                    ) {
                                        Box {
                                            if (rememberStockPriceInfo.itmsNm.isEmpty()) {
                                                Text(
                                                    text = stringResource(id = R.string.EditIncomeNoteDialog_SubjectName_Hint),
                                                    style = TextStyle(fontSize = 14.sp, color = Color_666666)
                                                )
                                            }
                                            innerTextField()
                                        }
                                        Spacer(modifier = Modifier.height(5.dp))
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.MyStock_Purchase_Average),
                            fontSize = 16.sp,
                            color = Color_666666
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            value = purchasePriceText,
                            textStyle = TextStyle(fontSize = 14.sp, color = Color_222222),
                            onValueChange = {
                                if (it.text.isEmpty()) {
                                    purchasePriceText = TextFieldValue(text = it.text)
                                } else if (it.text.last().toString().matches(numberPattern)) {
                                    val commaInsertedText = StockUtils.getNumInsertComma(it.text)
                                    purchasePriceText = TextFieldValue(text = commaInsertedText, selection = TextRange(commaInsertedText.length))
                                } else if (!it.text.last().toString().matches(numberPattern)) {
                                    Toast.makeText(context, context.getString(R.string.Msg_Only_Number), Toast.LENGTH_SHORT).show()
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawWithContent {
                                            drawContent()
                                            drawLine(
                                                color = Color(0x1a000000),
                                                start = Offset(
                                                    x = 0f,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                end = Offset(
                                                    x = size.width,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                strokeWidth = 1.dp.toPx(),
                                            )
                                        },
                                ) {
                                    Box {
                                        if (purchasePriceText.text.isEmpty()) {
                                            Text(
                                                text = stringResource(id = R.string.MyStockInputDialog_Purchase_Price_Hint),
                                                style = TextStyle(fontSize = 14.sp, color = Color_666666)
                                            )
                                        }
                                        Row {
                                            if (purchasePriceText.text.isNotEmpty()) {
                                                Text(
                                                    text = StockConfig.koreaMoneySymbol,
                                                    style = TextStyle(fontSize = 14.sp, color = Color_222222)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.MyStock_Holding_Quantity),
                            fontSize = 16.sp,
                            color = Color(0xff666666)
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            value = purchaseCountText,
                            onValueChange = {
                                if (it.isEmpty()) {
                                    purchaseCountText = ""
                                } else if (it.matches(numberPattern)) {
                                    purchaseCountText = it
                                } else if (!it.matches(numberPattern)) {
                                    Toast.makeText(context, context.getString(R.string.Msg_Only_Number), Toast.LENGTH_SHORT).show()
                                } },
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawWithContent {
                                            drawContent()
                                            drawLine(
                                                color = Color(0x1a000000),
                                                start = Offset(
                                                    x = 0f,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                end = Offset(
                                                    x = size.width,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                strokeWidth = 1.dp.toPx(),
                                            )
                                        },
                                ) {
                                    Box {
                                        if (purchaseCountText.isEmpty()) {
                                            Text(
                                                text = stringResource(id = R.string.MyStockInputDialog_Purchase_Count_Hint),
                                                style = TextStyle(fontSize = 14.sp, color = Color_666666)
                                            )
                                        }
                                        innerTextField()
                                    }
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(50.dp)
                            .background(color = Color_F1F1F1),
                        onClick = { onDismissRequest(MyStockPurchaseInputDialogData(), false) }) {
                        Text(
                            text = stringResource(id = R.string.Common_Cancel),
                            color = Color_222222,
                            fontSize = 15.sp
                        )
                    }
                    TextButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(50.dp)
                            .background(color = Color_E52B4E),
                        onClick = {
                            if (purchaseCountText.isEmpty() ||
//                                purchaseDateText.isEmpty() ||
                                purchasePriceText.text.isEmpty() ||
                                rememberStockPriceInfo.itmsNm.isEmpty()
                            ) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.MyStockInputDialog_Error_Message),
                                    Toast.LENGTH_LONG
                                ).show()
                                return@TextButton
                            }
                            if (purchaseCountText.toBigDecimal().equals(0)) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.MyStockInputDialog_Error_Message_Purchase_Count),
                                    Toast.LENGTH_LONG
                                ).show()
                                return@TextButton
                            }
                            onDismissRequest(
                                MyStockPurchaseInputDialogData(
                                    stockPriceInfo = rememberStockPriceInfo,
//                                    purchaseDate = purchaseDateText,
                                    purchasePrice = purchasePriceText.text,
                                    purchaseCount = purchaseCountText
                                ),
                                true
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.Common_Complete),
                            color = Color_FFFFFF,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MyStockPurchaseInputDialogContent(onDismissRequest = { data, isComplete -> })
}
