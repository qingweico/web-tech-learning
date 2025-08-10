import { createI18n } from 'vue-i18n';

const messages = {
    en: {
        message: {
            Confirm: 'Confirm',
            Cancel: 'Cancel'
        }
    },
    zh: {
        message: {
            Confirm: '确认',
            Cancel: '取消'
        }
    }
};

const i18n = createI18n({
    // 设置默认语言
    locale: 'zh',
    messages
});

export default i18n;
