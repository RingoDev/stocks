import React from 'react';
import Stats from "./components/Stats";
import BigChart from "./components/BigChart";
import AddPosition from "./components/AddPosition";
import Positions from "./components/Positions";
import Footer from "./components/Footer";
import {Row, Container} from "reactstrap";
import {connect, ConnectedProps} from "react-redux";
import {RootState} from "../redux/rootReducer";
import {Dispatch} from "redux";
import {
    getStockData,
    getStockDataError,
    getStockDataPending
} from "../redux/stockdata/stockdata.reducer";
import {getStockList, getStockListError, getStockListPending} from "../redux/stocklist/stocklist.reducer";

type DashBoardState = {}

class Dashboard extends React.Component<PropsFromRedux, DashBoardState> {

    shouldComponentUpdate(nextProps: Readonly<PropsFromRedux>, nextState: Readonly<DashBoardState>, nextContext: any): boolean {
        return !this.props.stockData;

    }

    render() {

        return (
            <>
                <Container  fluid={true} className={"pt-5 px-md-5"}>
                    <Row className="row">
                        <Stats/>
                    </Row>
                    <Row className="row">
                        <BigChart/>
                        <Positions/>
                    </Row>
                    <Row className="row">
                        <AddPosition/>
                    </Row>
                </Container>
                <Footer/>
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
    }
}

const mapDispatch = (dispatch: Dispatch) => {
    return {
    }
}

const connector = connect(
    mapState,
    mapDispatch
)

export default connector(Dashboard)