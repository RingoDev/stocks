import {FETCH_STOCK_LIST_ERROR,FETCH_STOCK_LIST_SUCCESS,FETCH_STOCK_LIST_PENDING} from './stocklist.types';

interface initial_state {

}
const INITIAL_STATE:{
    pending:boolean,
    stockList:string[],
    error:{}|undefined
} = {
    pending: false,
    stockList:[],
    error:undefined
};

const reducer = (state = INITIAL_STATE, action: { type: string, error?:{},stockList?:string[]}) => {

    switch (action.type) {

        case FETCH_STOCK_LIST_PENDING:

            return {
                ...state,
                pending: true
            };

        case FETCH_STOCK_LIST_SUCCESS:

            console.log("action",action)

            return {
                ...state,
                pending: false,
                stockList: action.stockList? action.stockList : []
            };

        case FETCH_STOCK_LIST_ERROR:


            return {
                ...state,
                pending: false,
                error:action.error
            };

        default:
            return state;

    }

};

export type  {
    INITIAL_STATE
}

export const getStockList = (state:typeof INITIAL_STATE) => state.stockList;
export const getStockListPending = (state:typeof INITIAL_STATE) => state.pending;
export const getStockListError = (state:typeof INITIAL_STATE) => state.error;


export default reducer;