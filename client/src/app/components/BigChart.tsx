import React from "react";
import {drawChart, number_format} from "./big-chart";
import {Button, Card, Col} from "reactstrap";
import Chart, {ChartData, ChartOptions, ChartTooltipItem} from 'chart.js'
import {connect, ConnectedProps} from "react-redux";
import {RootState} from "../../redux/rootReducer";
import {getStockData, getStockDataError, getStockDataPending} from "../../redux/stockdata/stockdata.reducer";
import {Dispatch} from "redux";
import Loader from "./Loader";
import {StockData} from "../../redux/stockdata/stockdata.actions";


interface BigChartState {
    timeframe: TimeFrame,
}

type TimeFrame = 'week' | 'month'

class BigChart extends React.Component<PropsFromRedux, BigChartState> {


    chart: Chart | undefined

    constructor(props: PropsFromRedux) {
        super(props);
        this.state = {
            timeframe: 'month',
        }
    }

    render() {

        if (!this.props.stockData.positions || !this.props.stockData.validDates) {
            return (
                <Col className="col-xl-12 col-lg-12">
                    <Loader/>
                </Col>
            )
        }


        return (
            <>
                <Col className="col-xl-6 col-lg-12">
                    <Card style={{backgroundColor: "#172b4d"}} className=" shadow mb-4">
                        <div
                            className="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                            <h6 className="m-0 font-weight-bold text-primary">Earnings Overview</h6>
                            <div className="dropdown no-arrow">
                                <Button className={'mx-2'} onClick={() => this.setTimeframe('week')}>W
                                </Button>
                                <Button className={'mx-2'} onClick={() => this.setTimeframe('month')}>M
                                </Button>
                            </div>
                        </div>
                        <div className="card-body">
                            <div className="chart-area">
                                <canvas id="myBigChart"/>
                            </div>
                        </div>
                    </Card>
                </Col>
            </>
        )
    }

    setTimeframe(timeframe: TimeFrame) {
        this.setState({timeframe})
    }

    componentDidUpdate() {
        if (!this.chart) {
            this.initiateChart()
        } else {
            console.log('updating graph')
            const data = getData(this.props.stockData, this.state.timeframe)
            const datasets = getDatasets(data)
            const labels = getLabels(this.props.stockData, this.state.timeframe)
            const options = getOptions(data)
            const minMax = getMinAndMax(data)
            if (this.chart.options.scales && this.chart.options.scales.yAxes && this.chart.options.scales.yAxes[0] && this.chart.options.scales.yAxes[0].ticks) {
                this.chart.options.scales.yAxes[0].ticks.min = minMax.min;
                this.chart.options.scales.yAxes[0].ticks.max = minMax.max;
            } else {
                this.chart.options = options
            }

            this.chart.data = {datasets, labels}
            this.chart.update({duration: 0, lazy: false, easing: 'linear'})

        }
    }

    componentDidMount() {
        this.initiateChart()
    }

    // todo
    initiateChart() {

        if (this.chart) {
            this.chart.destroy()
        }
        if (this.props.stockData.validDates && this.props.stockData.positions) {
            console.log("Chart was initiated")
            const data = getData(this.props.stockData, this.state.timeframe)
            const datasets = getDatasets(data)
            const labels = getLabels(this.props.stockData, this.state.timeframe)
            const ChartData: ChartData = {datasets, labels}
            const options = getOptions(data)
            this.chart = drawChart(ChartData, options);
        }
    }
}

type PropsFromRedux = ConnectedProps<typeof connector>

function getMinAndMax(data: number[]) {
    return {
        min: Math.min(...data),
        max: Math.max(...data)
    }

}

const mapState = (state: RootState) => {
    return {
        stockDataError: getStockDataError(state.stockData),
        stockData: getStockData(state.stockData),
        stockDataPending: getStockDataPending(state.stockData),
    }
}

const mapDispatch = (dispatch: Dispatch) => {
    return {}
}

const connector = connect(
    mapState,
    mapDispatch
)

export default connector(BigChart)

function getOptions(data: number[]): ChartOptions {

    const min = Math.min(...data)
    const max = Math.max(...data)

    return {
        maintainAspectRatio: true,
        legend: {
            display: false
        },
        responsive: true,
        tooltips: {
            backgroundColor: '#f5f5f5',
            titleFontColor: '#333',
            bodyFontColor: '#666',
            bodySpacing: 4,
            xPadding: 12,
            mode: 'nearest',
            intersect: false,
            position: 'nearest',
            callbacks: {
                label: function (tooltipItem: ChartTooltipItem, chart: ChartData) {
                    let datasetLabel = (chart.datasets && tooltipItem.datasetIndex) ? chart.datasets[tooltipItem.datasetIndex].label : '';
                    if (tooltipItem.yLabel === undefined) return "error";
                    return datasetLabel + ': $' + number_format(tooltipItem.yLabel, 0, undefined, ".");
                }
            }
        },
        scales: {
            yAxes: [
                {
                    gridLines: {
                        drawBorder: false,
                        color: 'rgba(29,140,248,0.0)',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        min: min,
                        max: max,
                        suggestedMin: 60,
                        suggestedMax: 125,
                        padding: 20,
                        fontColor: '#9e9e9e'
                    }
                }
            ],

            xAxes: [
                {
                    gridLines: {
                        drawBorder: false,
                        color: 'rgba(29,140,248,0.1)',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        padding: 20,
                        fontColor: '#9e9e9e'
                    }
                }
            ]
        }
    }
}


function getDatasets(data: number[]) {
    return [{
        label: "Earnings",
        lineTension: 0.3,
        backgroundColor: "rgba(78, 115, 223, 0.05)",
        borderColor: "rgba(78, 115, 223, 1)",
        pointRadius: 0,
        pointBackgroundColor: "rgba(78, 115, 223, 1)",
        pointBorderColor: "rgba(78, 115, 223, 1)",
        pointHoverRadius: 3,
        pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
        pointHoverBorderColor: "rgba(78, 115, 223, 1)",
        pointHitRadius: 10,
        pointBorderWidth: 2,
        data: data
    }]
}

function getData(stockData: StockData, timeframe: TimeFrame) {
    const pos = stockData.positions;
    let dates = stockData.validDates;
    if (!pos || !dates) return [];

    if (timeframe === "week") dates = dates.slice(dates.length - 5, dates.length);

    const data = []

    for (let i = 0; i < dates.length; i++) {
        data[dates.length - (1 + i)] = 0;
        for (let j = 0; j < pos.length; j++) {
            if (pos[j].checked) data[dates.length - (1 + i)] += pos[j].history.values[i];
        }
    }
    return data;
}

function getLabels(stockData: StockData, timeframe: TimeFrame) {
    let dates = stockData.validDates;

    if (!dates) return [];

    if (timeframe === "week") dates = dates.slice(dates.length - 5, dates.length);

    const labels = []
    for (let i = 0; i < dates.length; i++) {
        labels[i] = dates[i].shortWeekday;
    }
    return labels
}