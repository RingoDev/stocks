import React from "react";
import {Card, CardBody, Col, Row} from "reactstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCalendar, faClipboardList, faDollarSign} from "@fortawesome/free-solid-svg-icons";

export default class Stats extends React.Component {


    render() {

        return (
            <>
                <Col xl={3} md={6} className="mb-4">
                    <Card className=" border-left-primary shadow h-100 py-2">
                        <CardBody>
                            <Row noGutters={true} className="align-items-center">
                                <Col className="col mr-2">
                                    <div
                                        className="text-xs font-weight-bold text-primary text-uppercase mb-1">Earnings
                                        (Monthly)
                                    </div>
                                    <div
                                        className="h5 mb-0 font-weight-bold text-gray-800">$40,000
                                    </div>
                                </Col>
                                <Col className="col-auto">
                                    <FontAwesomeIcon icon={faCalendar}/>
                                </Col>
                            </Row>
                        </CardBody>
                    </Card>
                </Col>

                <Col xl={3} md={6} className="mb-4">
                    <Card className="border-left-success shadow h-100 py-2">
                        <CardBody>
                            <Row noGutters={true} className="align-items-center">
                                <Col className=" mr-2">
                                    <div
                                        className="text-xs font-weight-bold text-success text-uppercase mb-1">Earnings
                                        (Annual)
                                    </div>
                                    <div
                                        className="h5 mb-0 font-weight-bold text-gray-800">$215,000
                                    </div>
                                </Col>
                                <Col className="col-auto">
                                    <FontAwesomeIcon icon={faDollarSign}/>
                                </Col>
                            </Row>
                        </CardBody>
                    </Card>
                </Col>
                <Col xl={3} md={6} className="mb-4">
                    <Card className=" border-left-success shadow h-100 py-2">
                        <CardBody>
                            <Row noGutters={true} className="align-items-center">
                                <Col className=" mr-2">
                                    <div
                                        className="text-xs font-weight-bold text-success text-uppercase mb-1">Earnings
                                        (Annual)
                                    </div>
                                    <div
                                        className="h5 mb-0 font-weight-bold text-gray-800">$215,000
                                    </div>
                                </Col>
                                <Col className="col-auto">
                                    <FontAwesomeIcon icon={faDollarSign}/>
                                </Col>
                            </Row>
                        </CardBody>
                    </Card>
                </Col>

                <Col xl={3} md={6} className="mb-4">
                    <Card  className="border-left-info shadow h-100 py-2">
                        <CardBody>
                            <Row noGutters={true} className="align-items-center">
                                <Col className="col mr-2">
                                    <div
                                        className="text-xs font-weight-bold text-info text-uppercase mb-1">Tasks
                                    </div>
                                    <Row noGutters={true} className="align-items-center">
                                        <Col className="col-auto">
                                            <div
                                                className="h5 mb-0 mr-3 font-weight-bold text-gray-800">50%
                                            </div>
                                        </Col>
                                        <Col>
                                            <div className="progress progress-sm mr-2">
                                                <div className="progress-bar bg-info"
                                                     role="progressbar" style={{width: "50%"}}
                                                     aria-valuenow={50} aria-valuemin={0}
                                                     aria-valuemax={100}/>
                                            </div>
                                        </Col>
                                    </Row>
                                </Col>
                                <div className="col-auto">
                                    <FontAwesomeIcon icon={faClipboardList}/>
                                </div>
                            </Row>
                        </CardBody>
                    </Card>
                </Col>
            </>
        )
    }
}