package com.yjpapp.stockportfolio.function.mystock

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.dialog.CommonOneBtnDialog
import com.yjpapp.stockportfolio.common.dialog.CommonTwoBtnDialog
import com.yjpapp.stockportfolio.common.theme.Color_80000000
import com.yjpapp.stockportfolio.common.theme.Color_FBFBFBFB
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockInputDialog
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockEntity
import dagger.hilt.android.AndroidEntryPoint
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import de.charlex.compose.rememberRevealState
import de.charlex.compose.reset
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 1.5 신규버전 업데이트 기능 (나의 주식)
 * @author Yoon Jae-park
 * @since 2022.02
 */
@AndroidEntryPoint
class MyStockFragment : Fragment() {
    private val myStockViewModel: MyStockViewModel by viewModels()
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setHasOptionsMenu(true)
            setContent {
                Column {
                    TotalPriceComposable()
                    StockListComposable()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_my_stock, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_MyStockFragment_Add -> {
                if (myStockViewModel.myStockInfoList.size >= 10) {
                    Toasty.info(
                        mContext,
                        getString(
                            R.string.MyStockFragment_Notice_Max_List
                        ),
                        Toasty.LENGTH_LONG
                    ).show()
                    return false
                }
                showInputDialog(null)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showInputDialog(
        dialogData: MyStockInputDialog.MyStockInputDialogData?
    ) {
        MyStockInputDialog(
            mContext = mContext,
            myStockInputDialogData = dialogData,
            callBack = object : MyStockInputDialog.CallBack {
                override fun onInputDialogCompleteClicked(
                    dialog: MyStockInputDialog,
                    userInputDialogData: MyStockInputDialog.MyStockInputDialogData
                ) {
                    if (!myStockViewModel.saveMyStock(
                            context = mContext,
                            id = dialogData?.id ?: 0,
                            userInputDialogData = userInputDialogData
                        )
                    ) {
                        Toasty.error(
                            mContext,
                            mContext.getString(R.string.Error_Msg_Normal),
                            Toasty.LENGTH_SHORT
                        ).show()
                        return
                    }
                    dialog.dismiss()
                }
            }).show()
    }

    /**
     * Compose 영역
     */
    @Preview
    @Composable
    private fun TotalPriceComposable() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.common_15dp),
                    end = dimensionResource(id = R.dimen.common_15dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = stringResource(id = R.string.MyStockFragment_Total_Purchase_Price),
                    color = colorResource(id = R.color.color_222222),
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)

                )
                Text(
                    text = "5000000000",
                    color = colorResource(id = R.color.color_222222),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))

            ) {
                Text(
                    text = stringResource(id = R.string.MyStockFragment_Total_Evaluation_Amount),
                    color = colorResource(id = R.color.color_222222),
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = "5000000000",
                    color = colorResource(id = R.color.color_222222),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = stringResource(id = R.string.Common_gains_losses),
                    color = colorResource(id = R.color.color_222222),
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = "5000000000",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = stringResource(id = R.string.Common_GainPercent),
                    color = colorResource(id = R.color.color_222222),
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = "5000000000",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }
            Divider(
                color = colorResource(id = R.color.color_line_1a000000),
                thickness = 1.dp,
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            )
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Preview
    @Composable
    private fun StockListComposable() {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LazyColumn(
            reverseLayout = true,
            state = listState
        ) {
            items(myStockViewModel.myStockInfoList.size) {
                StockListItem(
                    position = it,
                    myStockEntity = myStockViewModel.myStockInfoList[it]
                )
            }
        }
        coroutineScope.launch {
            myStockViewModel.scrollIndex.collect { position ->
                listState.scrollToItem(position)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun StockListItem(
        position: Int,
        myStockEntity: MyStockEntity
    ) {
        val revealSwipeState = rememberRevealState()
        val coroutineScope = rememberCoroutineScope()
        val maxRevealDp = 110.dp
        RevealSwipe(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            maxRevealDp = maxRevealDp,
            backgroundCardEndColor = Color_FBFBFBFB,
            animateBackgroundCardColor = false,
            state = revealSwipeState,
            directions = setOf(
//        RevealDirection.StartToEnd,
                RevealDirection.EndToStart
            ),
//            hiddenContentStart = {
//                Icon(
//                    modifier = Modifier.padding(horizontal = 25.dp),
//                    imageVector = Icons.Outlined.Star,
//                    contentDescription = null,
//                    tint = Color.White
//                )
//            },
            hiddenContentEnd = {
                Column(
                    modifier = Modifier
                        .width(maxRevealDp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .clickable {
                                val dialogData = MyStockInputDialog.MyStockInputDialogData(
                                    id = myStockEntity.id,
                                    subjectName = myStockEntity.subjectName,
                                    purchaseDate = myStockEntity.purchaseDate,
                                    purchasePrice = myStockEntity.purchasePrice,
                                    purchaseCount = myStockEntity.purchaseCount
                                )
                                showInputDialog(dialogData)
                                coroutineScope.launch {
                                    revealSwipeState.reset()
                                }
                            }
                            .fillMaxWidth()
                            .weight(0.333f)
                            .background(color = Color_80000000)
                    ) {
                        Text(
                            text = getString(R.string.Common_Edit),
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.color_ffffff)
                        )
                    }
                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    revealSwipeState.reset()
                                }
                            }
                            .fillMaxWidth()
                            .weight(0.333f)
                            .background(color = colorResource(id = R.color.color_4876c7))
                    ) {
                        Text(
                            text = getString(R.string.Common_Sell),
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.color_ffffff)
                        )
                    }
                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .clickable {
                                CommonTwoBtnDialog(
                                    mContext = mContext,
                                    CommonTwoBtnDialog.CommonTwoBtnData(
                                        noticeText = getString(R.string.Common_Notice_Delete_Check),
                                        leftBtnText = getString(R.string.Common_Cancel),
                                        leftBtnListener = { _: View, dialog: CommonTwoBtnDialog ->
                                            dialog.dismiss()
                                        },
                                        rightBtnText = getString(R.string.Common_Ok),
                                        rightBtnListener = { _: View, dialog: CommonTwoBtnDialog ->
                                            if (!myStockViewModel.deleteMyStock(myStockEntity)) {
                                                Toasty
                                                    .error(
                                                        mContext,
                                                        R.string.Common_Cancel,
                                                        Toasty.LENGTH_SHORT
                                                    )
                                                    .show()
                                                return@CommonTwoBtnData
                                            }
                                            dialog.dismiss()
                                        }
                                    )
                                ).show()
                                coroutineScope.launch {
                                    revealSwipeState.reset()
                                }
                            }
                            .fillMaxWidth()
                            .weight(0.333f)
                            .background(color = colorResource(id = R.color.color_cd4632))
                    ) {
                        Text(
                            text = getString(R.string.Common_Delete),
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.color_ffffff)
                        )
                    }
                }
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                elevation = 0.dp,
                backgroundColor = colorResource(id = R.color.color_background_fbfbfb)
            ) {
                Column(
                    modifier = Modifier
//                    .padding(bottom = 10.dp)
                        .wrapContentHeight()
//                    .fillMaxWidth()
//                    .background(color = colorResource(id = R.color.color_background_fbfbfb))
                ) {

                    Row(
                        Modifier
                            .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Text(
                            text = myStockEntity.subjectName,
                            fontSize = 16.sp,
                            maxLines = 1,
                            color = colorResource(id = R.color.color_222222),
                            modifier = Modifier
                                .weight(0.55f)
                                .padding(top = 10.dp)
                        )
                        Row(
                            modifier = Modifier
                                .weight(0.45f)
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.MyStockFragment_Gain),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                            )
                            //수익
                            Text(
                                text = myStockEntity.purchasePrice,
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666),
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                            //수익 퍼센트
                            Text(
                                text = "(0.93)",
                                fontSize = 12.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                        }
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
                                .weight(0.5f)
                        ) {
                            Text(
                                text = getString(R.string.MyStockFragment_Purchase_Date),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666)
                            )

                            Text(
                                text = myStockEntity.purchaseDate,
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .weight(0.5f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "평단가",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )

                            Text(
                                text = myStockEntity.purchasePrice,
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
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
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(
                                text = "매수수량",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666)
                            )

                            Text(
                                text = "270",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "현재가",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )

                            Text(
                                text = "2,600",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}