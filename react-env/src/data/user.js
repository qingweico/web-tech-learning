import {nanoid} from "nanoid";

const items = []
for (let i = 0; i < 10; i++) {
    const id = nanoid();
    items.push({id, item: id});
}
const user = {
    account: {
        no: 'XS957801', username: 'User01',
    }, address: {
        region: '中华区', road: '人民路', street: '五一街道', number: '999号',
    }, disabled: true
}
const priority = 1
export {items, user, priority};
