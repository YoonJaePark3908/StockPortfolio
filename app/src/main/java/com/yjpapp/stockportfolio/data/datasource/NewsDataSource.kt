package com.yjpapp.stockportfolio.data.datasource

import com.yjpapp.stockportfolio.data.model.NewsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedInputStream
import java.net.URL
import javax.inject.Inject

val mkNewsRssUrl = "https://www.mk.co.kr/rss/30100041/"
class NewsDataSource @Inject constructor(
    private val parser: XmlPullParser
) {
    suspend fun getNkFinanceNewsList(): MutableList<NewsData> {
        val newsDataList = mutableListOf<NewsData>()

        try {
            val url = URL(mkNewsRssUrl)
            val bis = BufferedInputStream(withContext(Dispatchers.IO) { url.openStream() } )
            parser.setInput(bis, null)

            var tag = ""
            var isItemTag = false

            // getEventType()는
            // 문서 시작/끝, 태그 시작/끝, 태그의 내용을 표시하는 값을 표시한다.
            var parserEvent = parser.eventType
            var newsData = NewsData()
            while (parserEvent != XmlPullParser.END_DOCUMENT) { //문서가 끝날때까지 동작
                when (parserEvent) {
                    XmlPullParser.START_TAG -> { // TAG 시작. 예시) <title> 태그
                        tag = parser.name
                        if (tag == "item") { // news 들만 가져오기 위한 boolean 확인
                            isItemTag = true
                        }
                    }

                    XmlPullParser.TEXT -> { // TAG 안의 문자열. 예시) <title> 과 </title> 사이의 문자열
                        if (isItemTag) {
                            if (tag == "title") {
                                newsData.title = parser.text
                            }
                            if (tag == "link") {
                                newsData.link = parser.text
                            }
                            if (tag == "description") {
                                newsData.description = parser.text
                            }
                            if (tag == "pubDate") {
                                newsData.pubDate = parser.text
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> { // TAG 종료. 예시) </title> 태그
                        tag = parser.name
                        if (tag == "item") { // news 태그 종료확인
                            newsDataList.add(newsData)
                            isItemTag = false
                            //news data 초기화
                            newsData = NewsData()
                        }
                        tag = "" // tag 값 초기화
                    }
                }
                // 다음 TAG 로 이동
                // 예시) </title>의 다음 Tag 인 <link>로 이동.
                parserEvent = parser.next()
            }
            return newsDataList
        } catch (e: Exception) {
            return newsDataList
        }
    }
}