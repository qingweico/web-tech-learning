import { NextPage } from 'next';
import React, { memo, useContext, useMemo, useState } from 'react';
import { Affix } from "antd"
import "highlight.js/styles/github-dark.css"
import MarkdownNavbar from 'markdown-navbar';

import {
  ArticleDescWrapper,
  ArticleDetailWrapper,
  ArticleDetailContent,
  ArticleDetailOutLine
} from "@/styles/detail"
import { fetchArticleById } from '@/services/modules/article';
import { ThemeContext } from '@/common/context';
import { formatTime } from '@/utils/format';
import { useSelector } from 'react-redux';
import { useRouter } from 'next/router';


const ArticleDetail: NextPage = memo(function MyArticleDetail(props: any) {
  // redux hook
  const { userInfo } = useSelector((state: any) => {
    return {
      userInfo: state.getIn(["login", "userInfo"])
    }
  })
  const { articleDetail } = props
  console.log(articleDetail.ll_content_html)
  const [articleComments, setArticleComments] = useState(props.articleComments)
  const [total, setTotal] = useState(props.total)
  const [content, setContent] = useState("")

  const theme = useContext(ThemeContext)
  const router = useRouter()
  const articleId = useMemo(() => router.query.id, [router])


  return (
    <>
      <ArticleDescWrapper theme={theme}>
        <h2>{articleDetail.ll_title}</h2>
        <p>最后编辑时间: {formatTime(articleDetail.ll_updatedTime)}</p>
      </ArticleDescWrapper>
      <ArticleDetailWrapper theme={theme}>
        <div className='article-container'>
          <ArticleDetailContent dangerouslySetInnerHTML={{__html: articleDetail.ll_content_html}}  theme={theme} />
        </div>
        <Affix offsetTop={55}>
          <ArticleDetailOutLine theme={theme}>
            <MarkdownNavbar ordered={false} headingTopOffset={0} source={articleDetail.ll_content} />
          </ArticleDetailOutLine>
        </Affix>
      </ArticleDetailWrapper>
    </>
  );
});
// 获取初始化
ArticleDetail.getInitialProps = async (ctx) => {
  const articleDetail: any = await fetchArticleById({ ll_id: ctx.query.id })
  return {
    articleDetail: articleDetail.data,
  }
}

export default ArticleDetail
