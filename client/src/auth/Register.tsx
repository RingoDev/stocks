import React from 'react';
import {Button, Card, CardBody, Col, Container, Form, FormGroup, Input, Label, Row} from "reactstrap";
import {RootState} from "../redux/rootReducer";
import {getLoggedIn} from "../redux/authentication/auth.reducer";
import {Dispatch} from "redux";
import {register} from "../redux/authentication/auth.actions";
import {connect, ConnectedProps} from "react-redux";

type LoginState = {
    email: string,
    password: string,
    username: string,
    error: string | null
}

class Register extends React.Component<PropsFromRedux, LoginState> {
    constructor(props: PropsFromRedux) {
        super(props);
        this.state = {
            email: '',
            password: '',
            error: null,
            username:''
        }
    }

    setError(newError: string) {
        this.setState({error: newError})
    }

    setUsername(username: string) {
        this.setState({username})
    }


    setEmail(newEmail: string) {
        this.setState({email: newEmail})
    }

    setPassword(newPassword: string) {
        this.setState({password: newPassword})
    }

    submitForm = () => {
        if (this.state.email === "" || this.state.password === "" || this.state.username === '') {
            this.setError("Fields are required");
            console.log(this.state.error)
            return;
        }
        this.props.onRegister({username:this.state.username,email: this.state.email, password: this.state.password});
    };

    render() {
        return (
            <>
                <Container className="pt-lg-5 pt-md-5">
                    <Row className="justify-content-center text-center mb-7">
                        <Col xl={5} lg={6} md={8} className="px-5">
                            <h1>Welcome!</h1>
                            <p className="text-lead">Create an Account and keep track of your Portfolio
                                for free!</p>
                        </Col>
                    </Row>
                </Container>
                <Container className={'mt-5 px-sm-5 px-3'}>
                    <Row className={'justify-content-center'}>
                        <Col lg={5} md={7}>
                            <Card>
                                <CardBody>
                                    <Form>
                                        <h2>Register</h2>
                                        <FormGroup>
                                            <Label for="registerUsername">Username</Label>
                                            <Input onChange={(event => this.setUsername(event.target.value))} type="text" name="username" id="registerUsername"
                                                   placeholder="with a placeholder"/>
                                            <Label for="registerEmail">Email</Label>
                                            <Input onChange={(event => this.setEmail(event.target.value))} type="email" name="email" id="registerEmail"
                                                   placeholder="with a placeholder"/>
                                            <Label for="registerPassword">Password</Label>
                                            <Input onChange={(event => this.setPassword(event.target.value))} type="password" name="password" id="registerPassword"
                                                   placeholder="password placeholder"/>
                                        </FormGroup>
                                        <Button onClick={this.submitForm}>Submit</Button>
                                    </Form>
                                </CardBody>
                            </Card>
                        </Col>
                    </Row>
                </Container>

            </>
        )
    }
}

const mapState = (state: RootState) => {
    return {
        loggedIn: getLoggedIn(state.auth)
    }
}

const mapDispatch = (dispatch: Dispatch) => {

    return {
        // @ts-ignore
        onRegister: (credentials: { username:string,email: string, password: string }) => dispatch(register(credentials)),
    }
}

const connector = connect(
    mapState,
    mapDispatch
)

type PropsFromRedux = ConnectedProps<typeof connector>

export default connector(Register)




