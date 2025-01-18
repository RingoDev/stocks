import {
    FETCH_STOCK_DATA_SUCCESS,
    FETCH_STOCK_DATA_PENDING,
    FETCH_STOCK_DATA_ERROR,
    ADD_POSITION_PENDING, ADD_POSITION_SUCCESS, ADD_POSITION_ERROR,
    REMOVE_POSITION_SUCCESS, REMOVE_POSITION_PENDING, REMOVE_POSITION_ERROR
} from './stockdata.types';
import {StockData} from "./stockdata.actions";
import { AxiosError } from 'axios';


const INITIAL_STATE = {
    fetchStockDataPending: false,
    stockData: new StockData(),
    fetchStockDataError: {},
    isLoaded: false,
    addPositionPending: false,
    addPositionError: {},
    removePositionPending: false,
    removePositionError: {}

};

const reducer = (state = INITIAL_STATE,
                 action: { type: string, error?: AxiosError, stockData?: StockData }) => {
    console.log(action)

    switch (action.type) {
        case FETCH_STOCK_DATA_PENDING:
            return {
                ...state,
                fetchStockDataPending: true
            };

        case FETCH_STOCK_DATA_SUCCESS:
            console.log("Fetched stock data", action.stockData)
            return {
                ...state,
                isLoaded: true,
                fetchStockDataPending: false,
                stockData: action.stockData
            };

        case FETCH_STOCK_DATA_ERROR:
            return {
                ...state,
                fetchStockDataPending: false,
                error: action.error
            };

        case ADD_POSITION_PENDING:
            return {
                ...state,
                addPositionPending: true,
            };
        case ADD_POSITION_SUCCESS:
            return {
                ...state,
                addPositionPending: false,
            };
        case ADD_POSITION_ERROR:
            return {
                ...state,
                addPositionError: action.error,
                addPositionPending: false
            };

        case REMOVE_POSITION_PENDING:
            return {
                ...state,
                removePositionPending: true,
            };
        case REMOVE_POSITION_SUCCESS:
            return {
                ...state,
                removePositionPending: false,
            };
        case REMOVE_POSITION_ERROR:
            return {
                ...state,
                removePositionError: action.error,
                removePositionPending: false
            }

        default:
            return state;
    }

};

export type  {
    INITIAL_STATE
}


export const getStockData = (state: typeof INITIAL_STATE) => state.stockData;
export const getStockDataPending = (state: typeof INITIAL_STATE) => state.fetchStockDataPending;
export const getStockDataError = (state: typeof INITIAL_STATE) => state.fetchStockDataError;
// export const getStockDataIsLoaded = (state:typeof INITIAL_STATE) => state.isLoaded;

export const getAddPositionPending = (state: typeof INITIAL_STATE) => state.addPositionPending;
export const getAddPositionError = (state: typeof INITIAL_STATE) => state.addPositionError;

export const getRemovePositionPending = (state: typeof INITIAL_STATE) => state.removePositionPending;
export const getRemovePositionError = (state: typeof INITIAL_STATE) => state.removePositionError;

export default reducer;