import React, { memo, useCallback, useState } from 'react';
import type { NextPage } from "next"
import { Empty, Pagination } from "antd"

import { ArticleWrapper } from "@/styles/article"
import {
  fetchArticleCategoryList,
  fetchArticleList,
  fetchArticleRecentList,
  fetchArticleTagList
} from '@/services/modules/article';

import ArticleCard from "@/components/article-card"
import ArticleMusic from '@/components/article-music';
import ArticleCategory from '@/components/article-category';
import ArticleRecent from '@/components/article-recent';
import ArticleTagWall from '@/components/article-tag-wall';
import ArticleProfile from '@/components/article-profile';
import { useRouter } from 'next/router';

interface SearchParams {
  keyword: string,
  ll_category: string,
  ll_title: string
}

const Article: NextPage = memo( function MyArticle(props: any) {
  const router = useRouter()
  const routerJump = (id: string) => {
    router.push(`/article/detail/${id}`).then(r => {})
  }

  const {
    categoryList,
    categoryCounts,
    recentList,
    tagList
  } = props

  const [pageNum, setPageNum] = useState(1)
  const [articleTotal, setArticleTotal] = useState(props.articleTotal)
  const [articleList, setArticleList] = useState(props.articleList)
  const resetArticleList = useCallback(async (
    page: number,
    keyword?: string,
    ll_category?: string,
    ll_title?: string) => {
    const articleData: any = await fetchArticleList({
      pageNum: page,
      pageSize: 4,
      keyword,
      ll_category,
      ll_title
    })
    setArticleList(articleData.data)
    setArticleTotal(articleData.total)
  }, [])
  const pageChange = useCallback(async (page: number) => {
    setPageNum(page)
    await resetArticleList(page)
  }, [resetArticleList])

  const searchArticle = useCallback(({ keyword, ll_category, ll_title } : SearchParams) => {
    resetArticleList(pageNum, keyword, ll_category, ll_title).then(r => {})
  }, [pageNum, resetArticleList])
  return (
    <ArticleWrapper>
      {
        articleList && articleList.length ?
        <div className="article-left">
          {
            articleList && articleList.map((item: any) => {
              return (
                <div key={item.ll_id} onClick={() => routerJump(item.ll_id)}>
                  <ArticleCard
                    ll_title={item.ll_title}
                    ll_introduce={item.ll_introduce}
                    ll_cover={item.ll_cover}
                    ll_createdTime={item.ll_createdTime}
                    ll_tags={item.ll_tags}
                    ll_category={item.ll_category} />
                </div>
              )
            })
          }
          <Pagination
            onChange={pageChange}
            current={pageNum}
            pageSize={4}
            showTotal={(total) => `共 ${total} 篇`}
            total={articleTotal} />
        </div> :
        <Empty
            style={{ flex: 3, height: '100vh', lineHeight: '100vh', color: '#888' }}
            description="抱歉 这里现在什么内容都没有～"/>
      }
      <div className="article-right">
        <ArticleMusic />
        <ArticleCategory
          searchArticle={searchArticle}
          categoryCounts={categoryCounts}
          categoryList={categoryList} />
        <ArticleRecent recentList={recentList} />
        <ArticleTagWall tagList={tagList} searchArticle={searchArticle} />
        <ArticleProfile />
      </div>
    </ArticleWrapper>
  )
})

Article.getInitialProps = async () => {
  // get data...
  const articleData: any = await fetchArticleList({ pageNum: 1, pageSize: 4 })
  const categoryData: any = await fetchArticleCategoryList()
  const recentData: any = await fetchArticleRecentList()
  const tagData: any = await fetchArticleTagList()

  return {
    articleList: articleData.data,
    articleTotal: articleData.total,
    categoryList: categoryData.data,
    categoryCounts: categoryData.counts,
    recentList: recentData.data,
    tagList: tagData.data
  }
}

export default Article;
