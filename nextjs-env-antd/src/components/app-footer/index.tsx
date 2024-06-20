import React, { memo, useContext } from 'react';

import { AppFooterWrapper } from './style';
import { ThemeContext } from '@/common/context';

export default memo(function AppFooter() {
  const theme = useContext(ThemeContext)
  return (
    <AppFooterWrapper theme={theme}>
     @2024 Next.js
    </AppFooterWrapper>
  );
});
