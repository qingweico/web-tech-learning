import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export const JddCustomListMixin = {
    // extends: JeecgListMixin,
    mixins: [JeecgListMixin],
    name: 'JddCustomListMixin',
    data : {
        booleanTypeOptions : [{ key: 0, display_name: '是' }, { key: 1, display_name: '否' }],
       
    },
    computed: {
    //     booleanTypeKeyValue : this.booleanTypeOptions.reduce((acc, cur) => {
    //         acc[cur.key] = cur.display_name
    //         return acc
    //       }, {})
    }
}