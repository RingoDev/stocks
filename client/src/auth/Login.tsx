import {Form, FormGroup, Label, Input, Button, Card, CardBody, Container, Row, Col, Alert} from "reactstrap";
import {login} from "../redux/authentication/auth.actions";
import {connect, ConnectedProps} from "react-redux";
import {Dispatch} from "redux";
import {RootState} from "../redux/rootReducer";
import React from "react";
import {getLoggedIn, getLoginError} from "../redux/authentication/auth.reducer";


const mapState = (state: RootState) => {
    return {
        loggedIn: getLoggedIn(state.auth),
        loginError: getLoginError(state.auth)
    }
}

const mapDispatch = (dispatch: Dispatch) => {

    return {
        // @ts-ignore
        onLogin: (credentials: { email: string, password: string }) => dispatch(login(credentials)),
    }
}

const connector = connect(
    mapState,
    mapDispatch
)

type PropsFromRedux = ConnectedProps<typeof connector>
type LoginState = {
    email: string,
    password: string,
    error: string | null
}

class Login extends React.Component<PropsFromRedux, LoginState> {

    constructor(props: PropsFromRedux) {
        super(props);
        this.state = {
            email: '',
            password: '',
            error: null
        }

    }

    setError(newError: string) {
        this.setState({error: newError})
    }

    setEmail(newEmail: string) {
        this.setState({email: newEmail})
    }

    setPassword(newPassword: string) {
        this.setState({password: newPassword})
    }

    submitForm = () => {
        if (this.state.email === "" || this.state.password === "") {
            this.setError("Fields are required");
            return;
        }
        this.props.onLogin({email: this.state.email, password: this.state.password});
    };

    render() {
        console.log("login render", this.props)
        let errorMsg = ''
        if (this.props.loginError && this.props.loginError.response) {
            if (this.props.loginError.response.status === 401) errorMsg = 'Wrong Credentials';
            else if (this.props.loginError.response.status === 403) errorMsg = 'Your Email Address is not Verified';
        } else errorMsg = 'An unknown Error occurred!'
        return (
            <>
                <Container className="pt-lg-5 pt-md-5">
                    <Row className="justify-content-center text-center mb-7">
                        <Col xl={5} lg={6} md={8} className="px-5">
                            <h1>Welcome!</h1>
                            <p className="text-lead">Login and view your financial history!</p>
                        </Col>
                    </Row>
                </Container>
                <Container className={'mt-5 px-sm-5 px-3'}>
                    <Row className={'justify-content-center'}>
                        <Col lg={5} md={7}>
                            <Card>
                                <CardBody>
                                    <Form>
                                        <h2>Login</h2>
                                        <FormGroup>
                                            <Label for="exampleEmail">Email</Label>
                                            <Input type="email" name="email" id="exampleEmail"
                                                   placeholder="with a placeholder"
                                                   onChange={e => this.setEmail(e.target.value)}/>
                                        </FormGroup>
                                        <FormGroup className={"pb-1"}>
                                            <Label for="examplePassword">Password</Label>
                                            <Input type="password" name="password" id="examplePassword"
                                                   placeholder="password placeholder"
                                                   onChange={e => this.setPassword(e.target.value)}/>
                                        </FormGroup>
                                        {this.props.loginError ? <Alert style={{textAlign:"center"}} className={"my-2"} color={"danger"}>{errorMsg}</Alert> : <></>}
                                        <Button style={{margin:"0 auto",display:"block"}} className={"mt-3"} onClick={this.submitForm}>Login</Button>
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

export default connector(Login)