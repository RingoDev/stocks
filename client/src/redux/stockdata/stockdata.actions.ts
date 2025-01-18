import {
    ADD_POSITION_ERROR,
    ADD_POSITION_PENDING,
    ADD_POSITION_SUCCESS,
    FETCH_STOCK_DATA_ERROR,
    FETCH_STOCK_DATA_PENDING,
    FETCH_STOCK_DATA_SUCCESS,
    REMOVE_POSITION_ERROR,
    REMOVE_POSITION_PENDING,
    REMOVE_POSITION_SUCCESS
} from './stockdata.types';
import {Dispatch} from "redux";
import {AxiosResponse, AxiosError} from 'axios'
import {checkErrorResponse} from "../errorHandling";
import {axiosInstance} from "../../axios/axiosConfig";

export class StockData {
    positions: {
        id: number;
        buyValue: number;
        currentValue: number;
        date: number;
        history: {
            stockSign: string;
            values: number[];
        };
        quantity: number;
        stock: string;
        checked: boolean;
    }[] | undefined;
    validDates:
        {
            date: number;
            fullWeekday: string;
            shortWeekday: string;
        }[] | undefined;
}


function fetchStockDataPending() {
    return {
        type: FETCH_STOCK_DATA_PENDING
    }
}

function fetchStockDataSuccess(stockData: StockData) {
    return {
        type: FETCH_STOCK_DATA_SUCCESS,
        stockData: stockData
    }
}

function fetchStockDataError(error: AxiosError) {
    return {
        type: FETCH_STOCK_DATA_ERROR,
        error: error
    }
}


export const fetchStockData = () => {
    return (dispatch: Dispatch) => {
        dispatch(fetchStockDataPending());
        axiosInstance.get('/user/stock')
            .then(res => {
                console.log(res)
                console.log(res.data)
                    dispatch(fetchStockDataSuccess(res.data as StockData))

            })
            .catch((err: AxiosError) => {
                    console.log(err.toJSON())
                    checkErrorResponse(err,dispatch);
                    dispatch(fetchStockDataError(err));
                }
            )

    }
}



export const addPositionFetchStockData = ( position: { stock: string, date: number, quantity: number }) => {
    return (dispatch: Dispatch) => {
        dispatch(postPositionPending());
        axiosInstance.post('/user/addPosition', position)
            .then(res => {
                dispatch(fetchStockDataPending());
                axiosInstance.get( '/user/stock', {
                }).then(res => {
                    console.log(res)
                    console.log(res.data)
                    dispatch(fetchStockDataSuccess(res.data as StockData))
                    dispatch(postPositionSuccess())
                }).catch((err: AxiosError) => {
                    checkErrorResponse(err,dispatch);
                    dispatch(fetchStockDataError(err));
                })
            })
            .catch((err: AxiosError) => {
                    checkErrorResponse(err,dispatch);
                    dispatch(postPositionError(err))
                }
            )
    }
}
//todo automatically respond from backend with updated userData or atleast flush immediately
export const removePositionFetchStockData = (positionId: { id: number }) => {
    return (dispatch: Dispatch) => {
        dispatch(removePositionPending());
        axiosInstance.post( '/user/removePosition', positionId)
            .then((res: AxiosResponse) => {
                dispatch(fetchStockDataPending());
                axiosInstance.get( '/user/stock').then((res: AxiosResponse) => {
                        dispatch(fetchStockDataSuccess(res.data as StockData))
                    dispatch(removePositionSuccess())
                }).catch((err: AxiosError) => {
                    checkErrorResponse(err,dispatch);
                    dispatch(fetchStockDataError(err));
                })
            })
            .catch((err: AxiosError) => {
                    checkErrorResponse(err,dispatch);
                    dispatch(removePositionError(err))
                }
            )
    }
}

function removePositionPending() {
    return {
        type: REMOVE_POSITION_PENDING
    }
}

function removePositionSuccess() {
    return {
        type: REMOVE_POSITION_SUCCESS,
    }
}

function removePositionError(error: AxiosError) {
    return {
        type: REMOVE_POSITION_ERROR,
        error: error
    }
}

function postPositionPending() {
    return {
        type: ADD_POSITION_PENDING
    }
}

function postPositionSuccess() {
    return {
        type: ADD_POSITION_SUCCESS,
    }
}

function postPositionError(error: AxiosError) {
    return {
        type: ADD_POSITION_ERROR,
        error: error
    }
}
