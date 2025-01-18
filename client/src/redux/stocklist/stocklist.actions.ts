import {FETCH_STOCK_LIST_ERROR, FETCH_STOCK_LIST_PENDING, FETCH_STOCK_LIST_SUCCESS} from './stocklist.types';
import {Dispatch} from "redux";
import  { AxiosError } from 'axios'
import {checkErrorResponse} from "../errorHandling";
import {axiosInstance} from "../../axios/axiosConfig";

function fetchStockListPending() {
    return {
        type: FETCH_STOCK_LIST_PENDING
    }
}

function fetchStockListSuccess(stockList: string[]) {
    return {
        type: FETCH_STOCK_LIST_SUCCESS,
        stockList: stockList
    }
}

function fetchStockListError(error: {}) {
    return {
        type: FETCH_STOCK_LIST_ERROR,
        error: error
    }
}


export const fetchStockList = (token: string) => {
    return (dispatch: Dispatch) => {
        dispatch(fetchStockListPending());
        axiosInstance.get('/stocks/list', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(res => {
                console.log(res.data)
                dispatch(fetchStockListSuccess(res.data as string[]))
            })
            .catch((error:AxiosError) => {
                checkErrorResponse(error,dispatch)
                dispatch(fetchStockListError(error));
            })

    }
}
