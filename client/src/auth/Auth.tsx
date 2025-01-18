import React from 'react';
import {Link, Switch, Redirect} from "react-router-dom";
import Login from "./Login";
import Register from "./Register";
import {connect, ConnectedProps} from "react-redux";
import {Container, Navbar, NavbarBrand} from "reactstrap";
import {RootState} from "../redux/rootReducer";
import PrivateRoute from './../routing/GuardedRoute'
import './Auth.css'
import {getLoggedIn} from "../redux/authentication/auth.reducer";

type PropsFromRedux = ConnectedProps<typeof connector>


const mapState = (state: RootState) => {
    return {
        loggedIn: getLoggedIn(state.auth)
    }
}


const connector = connect(
    mapState
)


class Auth extends React.Component<PropsFromRedux, any> {


    render() {
        return (
            <>
                <div id={"auth-wrapper"}>
                <Container className={'pt-3'} >
                    <Navbar expand={true} light={true}
                            className="static-top">
                        <NavbarBrand href={"/"} className={"mr-auto"}>RingoDev Stocks</NavbarBrand>
                        <ul className="navbar-nav ml-auto">
                            <Link className={"nav-link"} to={"/login"}>Login</Link>
                            <Link className={"nav-link"} to={"/register"}>Register</Link>
                        </ul>
                    </Navbar>


                    <Switch>
                        <PrivateRoute path={'/login'} component={Login} opposite={true}/>
                        <PrivateRoute path={"/register"} component={Register} opposite={true}/>
                        <Redirect to={'/'}/>
                    </Switch>

                </Container>
                </div>
            </>
        )
    }
}

export default connector(Auth)