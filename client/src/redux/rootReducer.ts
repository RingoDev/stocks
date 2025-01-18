import {combineReducers} from 'redux';

//Contains Authentication Logic
import authReducer from './authentication/auth.reducer';

//Contains the Fetching Logic for List of available Stocks
import stockListReducer from './stocklist/stocklist.reducer';

//Contains the Fetching Logic for the user specific StockData
import stockDataReducer from './stockdata/stockdata.reducer';


import type {INITIAL_STATE as authState} from "./authentication/auth.reducer";
import type {INITIAL_STATE as stockListState} from "./stocklist/stocklist.reducer";
import type {INITIAL_STATE as stockDataState} from "./stockdata/stockdata.reducer";


const rootReducer = combineReducers({

    auth: authReducer,
    stockList: stockListReducer,
    stockData: stockDataReducer

});

export interface RootState {
    auth: authState,
    stockList:typeof stockListState,
    stockData:typeof stockDataState
}


export default rootReducer;