import React from "react";
import {Button, Card, Col, Table} from 'reactstrap';
import {connect, ConnectedProps} from "react-redux";
import {RootState} from "../../redux/rootReducer";
import {

    getStockData,
    getStockDataError, getStockDataPending
} from "../../redux/stockdata/stockdata.reducer";
import { getLoggedIn} from "../../redux/authentication/auth.reducer";
import {Dispatch} from "redux";
import {removePositionFetchStockData} from "../../redux/stockdata/stockdata.actions";
import Loader from "./Loader";

interface PositionsState {

}

class Positions extends React.Component<PropsFromRedux, PositionsState> {

    deletePosition(id: number) {
        if (!this.props.loggedIn) {
        }//redirect
        else this.props.onDeletePosition({id})
    }

    render() {

        if (!this.props.stockData.positions)
            return (
                <Col className="col-xl-12 col-lg-12">
                    <Loader/>
                </Col>
            )

        return (
            <>
                <Col className="col-xl-6 col-lg-12">
                    <Card className="card shadow mb-4">
                        <Table>
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Stock</th>
                                <th>Current Value</th>
                                <th>Quantity</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.props.stockData.positions.map((position, index) => {
                                return (

                                    <tr key={position.id}>
                                        <th scope={"row"}>{index}</th>
                                        <td>{position.stock}</td>
                                        <td>{position.currentValue}</td>
                                        <td>{position.quantity}</td>
                                        <td><Button onClick={() => this.deletePosition(position.id)}>Delete</Button>
                                        </td>
                                    </tr>
                                )
                            })}
                            </tbody>
                        </Table>
                    </Card>
                </Col>
            </>
        )
    }
}

type PropsFromRedux = ConnectedProps<typeof connector>

const mapState = (state: RootState) => {
    return {
        loggedIn: getLoggedIn(state.auth),
        stockDataError: getStockDataError(state.stockData),
        stockData: getStockData(state.stockData),
        stockDataPending: getStockDataPending(state.stockData)
    }

}

const mapDispatch = (dispatch: Dispatch) => {
    return {
        onDeletePosition: (positionId: { id: number }) => {
            // @ts-ignore
            dispatch(removePositionFetchStockData(positionId))
        }
    }
}

const connector = connect(
    mapState,
    mapDispatch
)

export default connector(Positions)