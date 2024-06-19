import React, {memo, useContext} from 'react';

import {ArticleProfileWrapper} from "./style"

import ArticleRContainer from '../article-r-container';
import {ThemeContext} from '@/common/context';
import {Image} from 'antd';

export default memo(function ArticleProfile() {

    const theme = useContext(ThemeContext)

    return (
        <ArticleRContainer title="技术交流" icon="iconfont icon-qunzu" color="#0984e3">
            <ArticleProfileWrapper theme={theme}>
                <div className='avatar'>
                    <Image
                        alt='个人头像'
                        src="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2F4d%2F63%2Fbd%2F4d63bd0b3bf8cc9aa0dc3e1111646b1c.jpeg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1645933458&t=ff8baa3927894aab55fca094fc1957b8"
                        width={70}
                        height={70}/>
                    <p className='self-desc'>
                        xxx
                    </p>
                </div>
                <div className="github-social">
                    <i className='iconfont icon-github-fill'></i>
                    <a href="https://www.baidu.com">&nbsp;Github Repo</a>
                </div>
                <div className="qq-social">
                    <i className='iconfont icon-QQ'></i>
                </div>
                <div className="wx-social">
                    <i className='iconfont icon-weixin'></i>
                </div>
                <div className="qq-group-social">
                    <i className='iconfont icon-qunzu'></i>
                </div>
            </ArticleProfileWrapper>
        </ArticleRContainer>
    );
});
