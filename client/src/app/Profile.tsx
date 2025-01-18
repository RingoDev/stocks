import React from "react";
import {Card, Row, Col, Form, Input, FormGroup, Container} from "reactstrap";

type ProfileProps = {}

type ProfileState = {}


export default class Profile extends React.Component<ProfileProps, ProfileState> {


    render() {
        return (
            <>
                <div>
                    <Container fluid={true}>
                        <span className="mask bg-gradient-success opacity-8"/>
                        <Container fluid={true} className="d-flex align-items-center">
                            <Row>
                                <Col lg={7} md={10}>
                                    <h1 className="display-2 text-white">Profile</h1>
                                    <p className="text-white mt-0 mb-5">This is your profile page. You can see the
                                        progress you've made with your
                                        work and manage your projects or assigned tasks</p>
                                    <a href="/" className="btn btn-info">Edit profile</a>
                                </Col>
                            </Row>
                        </Container>
                    </Container>
                    <Container fluid={true} className="mt-5">
                        <Row>
                            <Col xl="4" className="order-xl-2 mb-5">
                                {/*<user-card></user-card>*/}
                            </Col>
                            <Col xl="8" className="order-xl-1">
                                <Card>
                                    <Row align-v="center" slot="header">
                                        <Col xs={8}>
                                            <h3 className="mb-0">Edit profile </h3>
                                        </Col>
                                        <Col xs={4} className="text-right">
                                            <a href="/" className="btn btn-sm btn-primary">Settings</a>
                                        </Col>
                                    </Row>

                                    <Form>
                                        <h6 className="heading-small text-muted mb-4">User information</h6>

                                        <div className="pl-lg-4">
                                            <Row>
                                                <Col lg="6">
                                                    <Input
                                                        type="text"
                                                        label="Username"
                                                        placeholder="Username"
                                                    >
                                                    </Input>
                                                </Col>
                                                <Col lg="6">
                                                    <Input
                                                        type="email"
                                                        label="Email address"
                                                        placeholder="mike@email.com"
                                                    >
                                                    </Input>
                                                </Col>
                                            </Row>
                                            <Row>
                                                <Col lg="6">
                                                    <Input
                                                        type="text"
                                                        label="First Name"
                                                        placeholder="First Name"
                                                    >
                                                    </Input>
                                                </Col>
                                                <Col lg="6">
                                                    <Input
                                                        type="text"
                                                        label="Last Name"
                                                        placeholder="Last Name"
                                                    >
                                                    </Input>
                                                </Col>
                                            </Row>
                                        </div>
                                        <hr className="my-4"/>

                                        <h6 className="heading-small text-muted mb-4">Contact information</h6>

                                        <div className="pl-lg-4">
                                            <Row>
                                                <Col md="12">
                                                    <Input
                                                        type="text"
                                                        label="Address"
                                                        placeholder="Home Address"
                                                    >
                                                    </Input>
                                                </Col>
                                            </Row>
                                            <Row>
                                                <Col lg="4">
                                                    <Input
                                                        type="text"
                                                        label="City"
                                                        placeholder="City"
                                                    >
                                                    </Input>
                                                </Col>
                                                <Col lg="4">
                                                    <Input
                                                        type="text"
                                                        label="Country"
                                                        placeholder="Country"
                                                    >
                                                    </Input>
                                                </Col>
                                                <Col lg="4">
                                                    <Input
                                                        label="Postal Code"
                                                        placeholder="ZIP Code"
                                                    >
                                                    </Input>
                                                </Col>
                                            </Row>
                                        </div>

                                        <hr className="my-4"/>
                                        <h6 className="heading-small text-muted mb-4">About me</h6>
                                        <div className="pl-lg-4">
                                            <FormGroup label="About Me" label-class="form-control-label"
                                                       className="mb-0"
                                                       label-for="about-form-textaria">
                                                <Input type={'textarea'} rows="4"
                                                       id="about-form-textaria"
                                                       placeholder="A few words about you ..."/>
                                            </FormGroup>
                                        </div>
                                    </Form>
                                </Card>
                            </Col>
                        </Row>
                    </Container>
                </div>

            </>
        )
    }
}