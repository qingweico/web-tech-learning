const state = {
    sum: 0
}
const actions = {
    plus: (state, value) => {
        state.commit('PLUS', value)
    }, minus: (state, value) => {
        state.commit('MINUS', value)
    }
}
const mutations = {
    PLUS: (state, value) => {
        state.sum += value
    }, MINUS: (state, value) => {
        state.sum -= value
    }
}
const getters = {
    x10(state) {
        return state.sum * 10
    }
}


export default {
    namespaced: true,
    state, mutations, actions, getters
}
