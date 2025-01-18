import AppNav from "./App-Nav";
import Dashboard from "./Dashboard";
import React from "react";
import {Switch} from "react-router-dom";
import Logout from "../auth/Logout";
import Profile from "./Profile";
import PrivateRoute from './../routing/GuardedRoute'
import {connect, ConnectedProps} from "react-redux";
import {RootState} from "../redux/rootReducer";
import {getStockList, getStockListError, getStockListPending} from "../redux/stocklist/stocklist.reducer";
import {Dispatch} from "redux";
import {fetchStockList} from "../redux/stocklist/stocklist.actions";
import {fetchStockData} from "../redux/stockdata/stockdata.actions";
import {getStockData, getStockDataError, getStockDataPending} from '../redux/stockdata/stockdata.reducer';
import {getLoggedIn} from "../redux/authentication/auth.reducer";
import './Userspace.css';
import {logout} from "../redux/authentication/auth.actions";
import {Container} from "reactstrap";


interface UserspaceState {

}

class Userspace extends React.Component<PropsFromRedux, UserspaceState> {

    shouldComponentUpdate(nextProps: Readonly<PropsFromRedux>, nextState: Readonly<any>, nextContext: any): boolean {
        return false
    }

    componentDidMount() {
        if (!this.props.loggedIn) {
            this.props.onLogout();// dispatching a logout event
        }
        else {
            this.props.onFetchStockList()
            this.props.onFetchStockData()
        }
    }

    render() {

        return (
            <>
                <div id="userspace-wrapper" className="d-flex flex-column">
                    <div id="content">
                        <AppNav/>
                        <Switch>
                            <PrivateRoute path={'/dashboard'} component={Dashboard}/>
                            <PrivateRoute path={"/logout"} component={Logout}/>
                            <PrivateRoute path={'/profile'} component={Profile}/>
                            <PrivateRoute path={'/'} component={Dashboard}/>
                        </Switch>

                    </div>
                    <footer className="sticky-footer">
                        <Container className="my-auto">
                            <div className="copyright text-center my-auto">
                                <span>&copy; <a rel={'noopener'} href={'https://www.ringodev.com'}>RingoDev</a></span>
                            </div>
                        </Container>
                    </footer>
                </div>
                <div id={'userspace-background'}/>
            </>
        )
    }
}


type PropsFromRedux = ConnectedProps<typeof connector>

const mapState = (state: RootState) => {
    return {
        stockListError: getStockListError(state.stockList),
        stockList: getStockList(state.stockList),
        stockListPending: getStockListPending(state.stockList),
        stockDataError: getStockDataError(state.stockData),
        stockData: getStockData(state.stockData),
        stockDataPending: getStockDataPending(state.stockData),
        loggedIn: getLoggedIn(state.auth)
    }
}

const mapDispatch = (dispatch: Dispatch) => {
    return {
        onFetchStockList: () => {
            // @ts-ignore
            dispatch(fetchStockList())
        },
        onFetchStockData: () => {
            // @ts-ignore
            dispatch(fetchStockData())
        },
        onLogout: () => {
            // @ts-ignore
            dispatch(logout())
        }
    }
}

const connector = connect(
    mapState,
    mapDispatch
)

export default connector(Userspace)