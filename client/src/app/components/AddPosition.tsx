import React from "react";
import {Form, FormGroup, Label, Input, Button, Card, Col, CardBody, CardHeader} from 'reactstrap';
import {connect, ConnectedProps} from "react-redux";
import {RootState} from "../../redux/rootReducer";
import {getStockList, getStockListError, getStockListPending} from "../../redux/stocklist/stocklist.reducer";

import {Dispatch} from "redux";
import {fetchStockList} from "../../redux/stocklist/stocklist.actions";
import {getAddPositionError, getAddPositionPending} from "../../redux/stockdata/stockdata.reducer";
import {addPositionFetchStockData} from "../../redux/stockdata/stockdata.actions";
import {getLoggedIn} from "../../redux/authentication/auth.reducer";


interface AddPositionState {
    stockSign: string,
    date: number,
    error: string | null,
    quantity: number
}

class AddPosition extends React.Component<PropsFromRedux, AddPositionState> {

    constructor(props: PropsFromRedux) {
        super(props);
        this.state = {
            stockSign: '',
            date: 0,
            error: null,
            quantity: 0
        }
    }

    setError(error: string) {
        console.log("setting error to " + error)
        this.setState({error})
    }

    setStockSign(stockSign: string) {
        this.setState({stockSign})
    }

    setDate(date: number) {
        this.setState({date})
    }

    setQuantity(quantity: number) {
        this.setState({quantity})
    }

    render() {
        const stockList = this.props.stockList ? this.props.stockList : []
        return (
            <>
                <Col className="col-xl-12 col-lg-12">
                    <Card className="card shadow mb-4">
                        <CardHeader>
                            Add a Position
                        </CardHeader>
                        <CardBody>
                            <Form className={"p-2"}>
                                <FormGroup>
                                    <Label for="stockName">Stock</Label>
                                    <Input onChange={(event => this.setStockSign(event.target.value))}
                                           style={{textTransform: "uppercase"}}
                                           list={"data-list"}
                                           id="stockName"
                                           name="stock"
                                           placeholder="AAPL"/>
                                    <datalist id="data-list">
                                        {stockList.map((stockSign, index) => (
                                            <option key={index}>{stockSign}</option>
                                        ))}
                                    </datalist>
                                </FormGroup>
                                <FormGroup>
                                    <Label for="positionDat">Buy-Date</Label>
                                    <Input onChange={(event => this.setDate(Date.parse(event.target.value)))}
                                           id="positionDat"
                                           type="date"
                                           name="date"
                                           placeholder="password placeholder"/>
                                </FormGroup>
                                <FormGroup>
                                    <Label for="positionQuantity">Quantity</Label>
                                    <Input onChange={(event => this.setQuantity(event.target.valueAsNumber))}
                                           id="positionQuantity" type="number" name="quantity"
                                           placeholder={"Enter an amount"}/>
                                </FormGroup>
                                <Button onClick={this.submit}>Submit</Button>
                            </Form>
                        </CardBody>
                    </Card>
                </Col>
            </>
        )
    }

    submit = () => {
        if (!this.props.stockList.includes(this.state.stockSign.toUpperCase())) {
            this.setError("stock does not exist");
            return;
        }
        if (this.state.date < 0) {
            this.setError("Date is too far in the past");
            return;
        }
        if (this.state.quantity < 0) {
            this.setError("Quantity is too low");
            return;
        }

        const position = {
            stock: this.state.stockSign.toUpperCase(),
            date: this.state.date,
            quantity: this.state.quantity
        }
        if (!this.props.loggedIn) {
        }// redirect;
        else this.props.onAddPosition(position)
    };
}


type PropsFromRedux = ConnectedProps<typeof connector>

const mapState = (state: RootState) => {
    return {
        stockListError: getStockListError(state.stockList),
        stockList: getStockList(state.stockList),
        stockListPending: getStockListPending(state.stockList),
        addPositionError: getAddPositionError(state.stockData),
        addPositionPending: getAddPositionPending(state.stockData),
        loggedIn: getLoggedIn(state.auth)
    }
}

const mapDispatch = (dispatch: Dispatch) => {
    return {
        onFetchStockList: () => {
            // @ts-ignore
            dispatch(fetchStockList())
        },
        onAddPosition: (position: { stock: string, date: number, quantity: number }) => {
            // @ts-ignore
            dispatch(addPositionFetchStockData(position))
        }
    }
}

const connector = connect(
    mapState,
    mapDispatch
)

export default connector(AddPosition)